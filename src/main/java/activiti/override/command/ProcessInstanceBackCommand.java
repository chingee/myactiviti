package activiti.override.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cmd.CompleteTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.task.Task;
import org.springframework.util.StringUtils;

import activiti.exception.ActivitiRunTimeException;

/**
 * 退回操作的执行类
 * @author zhangqing
 *
 */
public class ProcessInstanceBackCommand extends MyCommand<List<Task>> {

	/**
	 * 驳回条件标记
	 */
	protected static Map<String, Object> BACK_CONDITION;
	
	static {
		BACK_CONDITION = new HashMap<String, Object>();
		BACK_CONDITION.put("pass", "1");
	}
	
	protected CommandContext commandContext;
	
	/**
	 * 当前任务id
	 */
	private String taskId;
	/**
	 * 退回任务节点
	 */
	private String targetActivitiId;
	/**
	 * 退回理由
	 */
	private String deleteReason;
	/**
	 * 流转条件
	 */
	private Map<String, Object> variables;
	/**
	 * 通过的历史流程节点
	 */
	protected List<HistoricActivityInstanceEntity> historicActivityInstanceEntitys;
	/**
	 * 需要标记为退回的节点Id
	 */
	protected Set<String> activityIds = new LinkedHashSet<String>();
	
	public ProcessInstanceBackCommand(String taskId, String targetActivitiId, Map<String, Object> variables, String deleteReason){
		super();
		this.taskId = taskId;
		this.targetActivitiId = targetActivitiId;
		this.deleteReason = deleteReason;
		this.variables = variables;
	}
	
	@Override
	public List<Task> execute(CommandContext commandContext) {
		this.commandContext = commandContext;
		TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);
		if(task == null){
			throw new ActivitiRunTimeException("当前任务节点不存在");
		}
		FindPassHistoricActivityInstanceCommand cmd1 = new FindPassHistoricActivityInstanceCommand(task.getProcessInstanceId());
		historicActivityInstanceEntitys = cmd1.execute(commandContext);
		//退回到当前节点的一个任务, 可能有多个任务
		this.back(task);
		//将退回后经过的历史节点都标记为退回
		for (String activityId : activityIds) {
			MarkBackHistoricActivityInstanceCommand cmd2 = new MarkBackHistoricActivityInstanceCommand(activityId, task.getProcessInstanceId());
			cmd2.execute(commandContext);
		}
		List<Task> tasks = new ArrayList<Task>();
		tasks.addAll(commandContext.getTaskEntityManager().findTasksByProcessInstanceId(task.getProcessInstanceId()));
		return tasks;
	}
	
	/**
	 * 退回
	 * @param task
	 * @return
	 */
	protected void back(TaskEntity task) {
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		ActivityImpl currentActivityImpl = processDefinitionEntity.findActivity(task.getTaskDefinitionKey());
		
		//退回到当前节点的一个任务, 可能有多个任务
		List<ActivityImpl> needBacks = new LinkedList<ActivityImpl>();
		if(StringUtils.isEmpty(targetActivitiId)){
			needBacks.addAll(findBackActivityImpl(currentActivityImpl, null));
		}else{
			needBacks.add(processDefinitionEntity.findActivity(targetActivitiId));
			findOtherPassedActivityImpl(currentActivityImpl, needBacks, null);
		}
		//退回到开始节点, 结束当前流程实例
		if(needBacks == null || needBacks.isEmpty()){
			runtimeService.deleteProcessInstance(task.getProcessInstanceId(), deleteReason);
		}else{
			//找到当前活动的任务
			List<Task> currentTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId())
				.list();
			turnTransition(task.getId(), currentActivityImpl, needBacks);
			List<ActivityImpl> endActivityImpls = findEndActivityImpl(processDefinitionEntity);
			for (Task t : currentTasks) {
				if(t.getId().equals(task.getId())){
					continue;
				}
				ActivityImpl otherActivityImpl = processDefinitionEntity.findActivity(t.getTaskDefinitionKey());
				//判断当前节点是否应该结束
				if(!isNeedBackActiveActivityImpl(otherActivityImpl, needBacks)){
					continue;
				}  
				findOtherPassedActivityImpl(otherActivityImpl, needBacks, null);
				turnTransition(t.getId(), otherActivityImpl, endActivityImpls);
				for (ActivityImpl activityImpl : endActivityImpls) {
					DeleteEndHistoricActivityInstanceCommand cmd = new DeleteEndHistoricActivityInstanceCommand(activityImpl.getId(), task.getProcessInstanceId());
					cmd.execute(commandContext);
				}
			}
		}
	}

	/**
	 * 找到退回的节点
	 * @param activityImpl
	 * @return
	 */
	private List<ActivityImpl> findBackActivityImpl(ActivityImpl activityImpl, Set<String> pvmTransitionIds){
		activityIds.add(activityImpl.getId());
		if(pvmTransitionIds == null){
			pvmTransitionIds = new LinkedHashSet<String>();
		}
		List<ActivityImpl> needBacks = new LinkedList<ActivityImpl>();
		List<PvmTransition> pvmTransitions = activityImpl.getIncomingTransitions();
		if(pvmTransitions != null && !pvmTransitions.isEmpty()){
			for (PvmTransition pvmTransition : pvmTransitions) {
				if(!isPassedActivity(pvmTransition) || pvmTransitionIds.contains(pvmTransition.getId())){
					continue;
				}
				ActivityImpl activityImpl_ = (ActivityImpl)pvmTransition.getSource();
				activityIds.add(activityImpl_.getId());
				ActivityBehavior activityBehavior = activityImpl_.getActivityBehavior();
				//上一个节点为用户任务
				if(activityBehavior instanceof UserTaskActivityBehavior){
					needBacks.add(activityImpl_);
				}else{
					needBacks.addAll(findBackActivityImpl(activityImpl_, pvmTransitionIds));
				}
			}
		}
		return needBacks;
	}
	
	/**
	 * 找到其他要退回时流经的节点
	 * @param activityImpl
	 * @return
	 */
	private void findOtherPassedActivityImpl(ActivityImpl activityImpl, List<ActivityImpl> needBacks, Set<String> pvmTransitionIds){
		if(pvmTransitionIds == null){
			pvmTransitionIds = new LinkedHashSet<String>();
		}
		activityIds.add(activityImpl.getId());
		List<PvmTransition> pvmTransitions = activityImpl.getIncomingTransitions();
		if(pvmTransitions != null && !pvmTransitions.isEmpty()){
			for (PvmTransition pvmTransition : pvmTransitions) {
				if(!isPassedActivity(pvmTransition) || pvmTransitionIds.contains(pvmTransition.getId())){
					continue;
				}
				PvmActivity pvmActivity = pvmTransition.getSource();
				boolean isTarget = false;
				for (ActivityImpl activityImpl_ : needBacks) {
					if(pvmActivity.getId().equals(activityImpl_.getId())){
						isTarget = true;
						break;
					}
				}
				if(isTarget){
					continue;
				}
				pvmTransitionIds.add(pvmTransition.getId());
				activityIds.add(pvmActivity.getId());
				findOtherPassedActivityImpl((ActivityImpl)pvmActivity, needBacks, pvmTransitionIds);
			}
		}
	}
	
	/**
	 * 判断节点是否经过
	 * @param pvmTransition
	 * @return
	 */
	private boolean isPassedActivity(PvmTransition pvmTransition){
		for (HistoricActivityInstanceEntity historicActivityInstanceEntity : historicActivityInstanceEntitys) {
			if(historicActivityInstanceEntity.getActivityId().equals(pvmTransition.getSource().getId())){
				return true;
			}
		}
		return false;
	}
	
	/**
     * 判断其他并行节点是否需要退回
     * @param targetActivityImpl
     * @param needBacks
     * @return
     */
    private boolean isNeedBackActiveActivityImpl(ActivityImpl targetActivityImpl, List<ActivityImpl> needBacks){
    	boolean isNeedBack = false;
    	for (ActivityImpl activityImpl : needBacks) {
    		if(activityImpl.getId().equals(targetActivityImpl.getId())){
    			return true;
    		}
    		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
    		//到了结束节点
    		if(pvmTransitionList == null || pvmTransitionList.isEmpty()){
    			continue;
    		}
    		List<ActivityImpl> destinations = new LinkedList<ActivityImpl>();
    		for (PvmTransition pvmTransition : pvmTransitionList) {
    			destinations.add((ActivityImpl)pvmTransition.getDestination());
			}
    		if(isNeedBackActiveActivityImpl(targetActivityImpl, destinations)){
    			return true;
    		}
		}
    	return isNeedBack;
    }
	
	/**
	 * 流程转向操作
	 * @param currentActivityImpl
	 * @param targetActivityImpls
	 */
	private void turnTransition(String taskId, ActivityImpl currentActivityImpl, List<ActivityImpl> targetActivityImpls){  
        // 清空当前流向  
        List<PvmTransition> oriPvmTransitionList = clearTransition(currentActivityImpl);  
  
        // 创建新流向  
        TransitionImpl newTransition = currentActivityImpl.createOutgoingTransition(); 
        
        for (ActivityImpl targetActivityImpl : targetActivityImpls) {
        	// 设置新流向的目标节点  
        	newTransition.setDestination(targetActivityImpl);  
        	
        	// 执行转向任务  
        	new CompleteTaskCmd(taskId, variables).execute(commandContext);
        	// 删除目标节点新流入  
        	targetActivityImpl.getIncomingTransitions().remove(newTransition);  
        	
        	// 还原以前流向  
        	restoreTransition(currentActivityImpl, oriPvmTransitionList);
        	
		}
    }
	
	/** 
     * 清空指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @return 节点流向集合 
     */  
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {  
        // 存储当前节点所有流向临时变量  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : pvmTransitionList) {  
            oriPvmTransitionList.add(pvmTransition);  
        }  
        pvmTransitionList.clear();  
  
        return oriPvmTransitionList;  
    }
    
    /** 
     * 还原指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @param oriPvmTransitionList 
     *            原有节点流向集合 
     */  
    private void restoreTransition(ActivityImpl activityImpl,  
            List<PvmTransition> oriPvmTransitionList) {  
        // 清空现有流向  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        pvmTransitionList.clear();  
        // 还原以前流向  
        for (PvmTransition pvmTransition : oriPvmTransitionList) {  
            pvmTransitionList.add(pvmTransition);  
        }  
    }
    
    /**
     * 找到流程的结束节点
     * @param processDefinitionEntity
     * @return
     */
    private List<ActivityImpl> findEndActivityImpl(ProcessDefinitionEntity processDefinitionEntity) {  
    	//找到结束节点
		List<ActivityImpl> endActivityImpls = new LinkedList<ActivityImpl>();
		for (ActivityImpl activityImpl : processDefinitionEntity.getActivities()) {  
			List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();  
			if (pvmTransitionList.isEmpty()) {  
				endActivityImpls.add(activityImpl);
				break;
			}  
		}  
		return endActivityImpls;
    }
    
}
