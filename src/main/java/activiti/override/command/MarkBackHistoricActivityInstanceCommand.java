package activiti.override.command;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

/**
 * 流程退回后删除回退过程中的历史流程
 * @author zhangqing
 *
 */
public class MarkBackHistoricActivityInstanceCommand extends MyCommand<HistoricActivityInstanceEntity> {

	private String activityId;
	private String processInstanceId;
	
	public MarkBackHistoricActivityInstanceCommand(String activityId, String processInstanceId){
		this.activityId = activityId;
		this.processInstanceId = processInstanceId;
	}
	
	@Override
	public HistoricActivityInstanceEntity execute(CommandContext commandContext) {
		List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).activityId(activityId).orderByActivityId().desc().list();
		if(!historicActivityInstances.isEmpty()){
			commandContext.getDbSqlSession().update("markBackHistoricActivityInstanceByActivityIdAndProcessInstanceId", historicActivityInstances.get(0));
			return (HistoricActivityInstanceEntity)(historicActivityInstances.get(0));
		}
		return null;
	}

}
