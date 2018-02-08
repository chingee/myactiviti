package activiti.override.factory.engine;

import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import activiti.override.configuration.MyProcessEngineConfiguration;
import activiti.override.service.MyTaskService;

public class MyProcessEngineImpl extends ProcessEngineImpl {
	
	protected MyTaskService myTaskService;
	
	public MyProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
		super(processEngineConfiguration);
		MyProcessEngineConfiguration my = (MyProcessEngineConfiguration)processEngineConfiguration;
		myTaskService = my.getMyTaskService();
	}

	public MyTaskService getMyTaskService() {
		return myTaskService;
	}

	public void setMyTaskService(MyTaskService myTaskService) {
		this.myTaskService = myTaskService;
	}

}
