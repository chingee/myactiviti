package activiti.override.command;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.interceptor.Command;

import activiti.util.BeanUtil;

public abstract class MyCommand<T> implements Command<T>{

	protected RepositoryService repositoryService;
	protected RuntimeService runtimeService;
	protected TaskService taskService;
	protected HistoryService historyService;
	protected ManagementService managementService;
	
	public MyCommand(){
		repositoryService = BeanUtil.getBean("repositoryService");
		runtimeService = BeanUtil.getBean("runtimeService");
		taskService = BeanUtil.getBean("taskService");
		historyService = BeanUtil.getBean("historyService");
		managementService = BeanUtil.getBean("managementService");
	}
	
}
