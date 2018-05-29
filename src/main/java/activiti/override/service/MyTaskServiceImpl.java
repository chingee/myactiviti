package activiti.override.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import activiti.override.command.ProcessInstanceBackCommand;

public class MyTaskServiceImpl extends ServiceImpl implements MyTaskService {

	protected static Log log = LogFactory.getLog(MyTaskServiceImpl.class);
	
	public MyTaskServiceImpl() {

	}

	public MyTaskServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
		super(processEngineConfiguration);
	}
	
	@Override
	public List<Task> back(String taskId, Map<String, Object> variables, String deleteReason) {
		log.debug("处理的taskId:" + taskId);
		return commandExecutor.execute(new ProcessInstanceBackCommand(taskId, null, variables, deleteReason));
	}

	@Override
	public List<Task> back(String taskId, String targetActivitiId, Map<String, Object> variables, String deleteReason) {
		log.debug("处理的taskId:" + taskId + ",处理的targetActivitiId:" + targetActivitiId);
		return commandExecutor.execute(new ProcessInstanceBackCommand(taskId, targetActivitiId, variables, deleteReason));
	}

	@Override
	public boolean validateTargetActivitiId(String sourceActivitiId, String targetActivitiId) {
		// TODO Auto-generated method stub
		return false;
	}

}
