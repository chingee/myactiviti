package activiti.override.command;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

/**
 * 流程退回后删除回退过程中的历史流程
 * @author zhangqing
 *
 */
public class MarkBackHistoricActivityInstanceCommand implements Command<HistoricActivityInstanceEntity> {

	private String activityId;
	private String processInstanceId;
	
	public MarkBackHistoricActivityInstanceCommand(String activityId, String processInstanceId){
		this.activityId = activityId;
		this.processInstanceId = processInstanceId;
	}
	
	@Override
	public HistoricActivityInstanceEntity execute(CommandContext commandContext) {
		HistoricActivityInstanceEntity entity = commandContext.getHistoricActivityInstanceEntityManager()
				.findHistoricActivityInstance(activityId, processInstanceId);
		if(entity != null){
			commandContext.getDbSqlSession().update("markBackHistoricActivityInstanceByActivityIdAndProcessInstanceId", entity);
		}
		return entity;
	}

}
