package activiti.override.command;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

/**
 * 当流程回退时,添加回退标记,原因
 * @author zhangqing
 *
 */
public class UpdateHistoricTaskInstanceDeleteReasonCommand implements Command<HistoricTaskInstanceEntity> {

	private String taskId;
	private String deleteReason;

	protected UpdateHistoricTaskInstanceDeleteReasonCommand(){}
	
	public UpdateHistoricTaskInstanceDeleteReasonCommand(String taskId, String deleteReason) {
		this.taskId = taskId;
		this.deleteReason = deleteReason;
	}
	
	@Override
	public HistoricTaskInstanceEntity execute(CommandContext commandContext) {
		commandContext.getHistoryManager().recordTaskEnd(taskId, deleteReason);
		return commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(taskId);
	}

}
