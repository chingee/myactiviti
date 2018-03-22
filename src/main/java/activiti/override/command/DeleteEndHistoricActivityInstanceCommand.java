package activiti.override.command;

import java.util.List;

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
		List<HistoricActivityInstance> endActivityInstances = historyService.createHistoricActivityInstanceQuery()
			.activityId(endActivitiId).processInstanceId(processInstanceId).list();
		if(endActivityInstances != null && !endActivityInstances.isEmpty()){
			commandContext.getDbSqlSession().delete("deleteEndHistoricActivityInstanceByActivityIdAndProcessInstanceId", endActivityInstances.get(0));
		}
		return null;
	}

}
