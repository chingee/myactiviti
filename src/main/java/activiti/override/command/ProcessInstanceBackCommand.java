package activiti.override.command;

import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
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
	 * 当前任务id
	 */
	private String taskId;
	/**
	 * 退回任务节点
	 */
	private String targetActivitiId;
	
	protected CommandContext commandContext;
	
	protected ProcessInstanceBackCommand(){}
	
	public ProcessInstanceBackCommand(String taskId, String targetActivitiId){
		super();
		this.taskId = taskId;
		this.targetActivitiId = targetActivitiId;
	}
	
	@Override
	public List<Task> execute(CommandContext commandContext) {
		this.commandContext = commandContext;
		TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);
		if(task == null){
			throw new ActivitiRunTimeException("当前任务节点不存在");
		}
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
		
		BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
		System.out.println(bpmnModel.getTargetNamespace());
	}

	/**
	 * 直接退回
	 * @param task
	 * @return
	 */
	protected void backToTarget(TaskEntity task) {
		
		String processInstanceId = task.getProcessInstanceId();
		System.out.println(processInstanceId);
		
	}
}
