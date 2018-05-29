package activiti.override.command;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * 流程退回后删除回退过程中的历史流程
 * @author zhangqing
 *
 */
public class DeleteEndHistoricActivityInstanceCommand extends MyCommand<Void> {

	private String processInstanceId;
	private String endActivitiId;
	
	public DeleteEndHistoricActivityInstanceCommand(String endActivitiId, String processInstanceId){
		this.endActivitiId = endActivitiId;
		this.processInstanceId = processInstanceId;
	}
	
	@Override
	public Void execute(CommandContext commandContext) {
		HistoricActivityInstance historicActivityInstance = commandContext.getHistoricActivityInstanceEntityManager()
				.findHistoricActivityInstance(endActivitiId, processInstanceId);
		if(historicActivityInstance != null){
			commandContext.getDbSqlSession().delete("deleteEndHistoricActivityInstanceByActivityIdAndProcessInstanceId", historicActivityInstance);
		}
		return null;
	}

}
