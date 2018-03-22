package activiti.controllor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TaskControllor extends RestControllor{
	
	protected static Log log = LogFactory.getLog(TaskControllor.class);
	
	@RequestMapping(value="/task/querylist/{processInstanceId}")
	public ModelAndView queryTasks(@PathVariable("processInstanceId") String processInstanceId){
		List<Task> tasks = taskService.createTaskQuery()
				.processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
		List<Execution> executions = runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId).list();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if(processInstance == null){
			List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
			ModelAndView mv = new ModelAndView("queryhistorys");
			mv.getModel().put("historictasks", historicTaskInstances);
			mv.getModel().put("historicactivitys", historicActivityInstances);
			mv.getModel().put("name", historicProcessInstance.getProcessDefinitionName());
			mv.getModel().put("processInstanceId", processInstanceId);
			return mv;
		}
		ModelAndView mv = new ModelAndView("querytasks");
		mv.getModel().put("tasks", tasks);
		mv.getModel().put("executions", executions);
		mv.getModel().put("processInstanceId", processInstanceId);
		mv.getModel().put("processDefinitionId", processInstance.getProcessDefinitionId());
		return mv;
	}
	
	@RequestMapping(value="/task/complete/{processInstanceId}")
	public String complete(@PathVariable("processInstanceId") String processInstanceId, 
			@RequestParam(value="taskId") String taskId, 
			@RequestParam Map<String, String> json){
		Map<String, Object> variables = new HashMap<>();
		variables.putAll(json);
		taskService.complete(taskId, variables);
		return "task/querylist/"+processInstanceId;
	}
	
	@RequestMapping(value="/task/back/{processInstanceId}")
	public String back(@PathVariable("processInstanceId") String processInstanceId, 
			@RequestParam(value="taskId") String taskId, 
			@RequestParam Map<String, String> json){
		Map<String, Object> variables = new HashMap<>();
		variables.putAll(json);
		myTaskService.back(taskId, variables, "退回");
		return "task/querylist/"+processInstanceId;
	}
	
	@RequestMapping(value="/task/backTo/{processInstanceId}")
	public String back(@PathVariable("processInstanceId") String processInstanceId, 
			@RequestParam(value="taskId") String taskId, 
			@RequestParam(value="toTaskId") String toTaskId, 
			@RequestParam Map<String, String> json){
		Map<String, Object> variables = new HashMap<>();
		variables.putAll(json);
		myTaskService.back(taskId, toTaskId, variables, "退回");
		return "task/querylist/"+processInstanceId;
	}

}
