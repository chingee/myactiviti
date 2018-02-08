package activiti.controllor;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HistoryControllor extends RestControllor{
	
	protected static Log log = LogFactory.getLog(HistoryControllor.class);
	
	@RequestMapping(value="/history/querylist/{processInstanceId}")
	public ModelAndView queryHistoryTasks(@PathVariable("processInstanceId") String processInstanceId){
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

}
