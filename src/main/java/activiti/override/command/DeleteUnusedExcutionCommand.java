package activiti.override.command;

import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * 流程退回后删除回退过程中的历史流程
 * @author zhangqing
 *
 */
public class DeleteUnusedExcutionCommand extends MyCommand<Void> {

	private String processInstanceId;
	
	public DeleteUnusedExcutionCommand(String processInstanceId){
		this.processInstanceId = processInstanceId;
	}
	
	@Override
	public Void execute(CommandContext commandContext) {
		commandContext.getDbSqlSession().delete("deleteUnusedExcutionCommand", processInstanceId);
		return null;
	}

}
