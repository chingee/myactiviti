package activiti.override.command;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
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
	protected static Map<String, String> BACK_CONDITION;
	
	static {
		BACK_CONDITION = new HashMap<String, String>();
		BACK_CONDITION.put("pass", "1");
	}
	
	/**
	 * 当前任务id
	 */
	private String taskId;
	/**
	 * 退回任务节点
	 */
	private String targetActivitiId;
	/**
	 * 流转条件
	 */
	private Map<String, Object> variables;
	
	protected CommandContext commandContext;
	/**
	 * 通过的历史流程节点
	 */
	protected List<HistoricActivityInstanceEntity> historicActivityInstanceEntitys;
	/**
	 * 需要标记为退回的节点Id
	 */
	protected Set<String> activityIds = new LinkedHashSet<String>();
	
	protected ProcessInstanceBackCommand(){}
	
	public ProcessInstanceBackCommand(String taskId, String targetActivitiId, Map<String, Object> variables){
		super();
		this.taskId = taskId;
		this.targetActivitiId = targetActivitiId;
		this.variables = variables;
	}
	
	@Override
	public List<Task> execute(CommandContext commandContext) {
		this.commandContext = commandContext;
		TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);
		if(task == null){
			throw new ActivitiRunTimeException("当前任务节点不存在");
		}
		FindPassHistoricActivityInstanceCommand cmd = new FindPassHistoricActivityInstanceCommand(task.getProcessInstanceId());
		historicActivityInstanceEntitys = cmd.execute(commandContext);
		//退回到当前节点的一个任务, 可能有多个任务
		if(StringUtils.isEmpty(targetActivitiId)){
			back(task);
		}else{
			backToTarget(task);
		}
		
		return taskService.createTaskQuery().taskId(taskId).orderByTaskId().asc().list();
	}
	
	/**
	 * 直接退回
	 * @param task
	 * @return
	 */
	protected void back(TaskEntity task) {
		//找到当前活动的任务
		List<Task> currentTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId())
			.list();
		//找到要退回的上一个节点
		findBackActivityImpl(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
		for (Task t : currentTasks) {
			if(t.getId().equals(task.getId())){
				continue;
			}
		}
	}

	/**
	 * 退回到指定节点
	 * @param task
	 * @return
	 */
	protected void backToTarget(TaskEntity task) {
		String processInstanceId = task.getProcessInstanceId();
		System.out.println(processInstanceId);
		
	}
	
	/**
	 * 找到需要退回的节点
	 * @param processDefinitionId
	 * @param taskDefinitionKey
	 * @return
	 */
	private List<ActivityImpl> findBackActivityImpl(String processDefinitionId, String taskDefinitionKey){
		List<ActivityImpl> needMarkedBackActivityImpls = new LinkedList<ActivityImpl>();
		List<ActivityImpl> needBackUserTasks = new LinkedList<ActivityImpl>();
		
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		ActivityImpl currentActivityImpl = processDefinitionEntity.findActivity(taskDefinitionKey);
		List<PvmTransition> pvmTransitions = currentActivityImpl.getIncomingTransitions();
		for (PvmTransition pvmTransition : pvmTransitions) {
			//判断节点是否经过
			boolean isPassed = false;
			for (HistoricActivityInstanceEntity historicActivityInstanceEntity : historicActivityInstanceEntitys) {
				if(historicActivityInstanceEntity.getActivityId().equals(pvmTransition.getId())){
					isPassed = true;
					break;
				}
			}
			if(!isPassed){
				continue;
			}
			findBackActivityImpl((ActivityImpl)pvmTransition.getSource());
		}
		return null;
	}
	
	/**
	 * 找到退回经过的节点
	 * @param activityImpl
	 * @return
	 */
	private List<ActivityImpl> findBackActivityImpl(ActivityImpl activityImpl){
		List<PvmTransition> pvmTransitions = activityImpl.getIncomingTransitions();
		for (PvmTransition pvmTransition : pvmTransitions) {
			ActivityImpl activityImpl_ = (ActivityImpl)pvmTransition.getSource();
			ActivityBehavior activityBehavior = activityImpl_.getActivityBehavior();
			//上一个节点为用户任务
			if(activityBehavior instanceof UserTaskActivityBehavior){
				
			}else if(activityBehavior instanceof ExclusiveGatewayActivityBehavior){//排他网关
				
			}else if(activityBehavior instanceof ParallelGatewayActivityBehavior){//上一个节点为并行网关
				
			}
		}
		return null;
	}
	
}
