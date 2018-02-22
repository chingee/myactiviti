package activiti.override.command;

import java.util.List;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

/**
 * 查询通过的历史流程
 * @author zhangqing
 *
 */
public class FindPassHistoricActivityInstanceCommand implements Command<List<HistoricActivityInstanceEntity>> {

	private String processInstanceId;
	
	protected FindPassHistoricActivityInstanceCommand(){}
	
	public FindPassHistoricActivityInstanceCommand(String processInstanceId){
		this.processInstanceId = processInstanceId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricActivityInstanceEntity> execute(CommandContext commandContext) {
		return commandContext.getDbSqlSession().selectList("selectPassHistoricActivityInstance", processInstanceId);
	}

}
