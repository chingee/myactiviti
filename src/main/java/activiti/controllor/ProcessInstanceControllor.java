package activiti.controllor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import activiti.graph.spring.MyHistoryProcessInstanceDiagramCmd;

@RestController
public class ProcessInstanceControllor extends RestControllor{
	
	protected static Log log = LogFactory.getLog(ProcessInstanceControllor.class);
	
	@RequestMapping(value="/processinstance/querylist/{processDefinitionId}")
	public ModelAndView queryProcessInstances(@PathVariable("processDefinitionId") String processDefinitionId){
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId)
				.orderByProcessInstanceId().asc().list();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionId(processDefinitionId).singleResult();
		List<HistoricProcessInstance> hislist = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionId(processDefinitionId).orderByProcessInstanceStartTime().desc().list();
		ModelAndView mv = new ModelAndView("queryprocessinstances");
		mv.getModel().put("flows", list);
		mv.getModel().put("hisflows", hislist);
		mv.getModel().put("name", processDefinition.getName());
		return mv;
	}
	
	@RequestMapping("/processinstance/trace/{processInstanceId}")  
	public void showProcessInstanceTracePng (@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response) throws Exception{  
	    response.setContentType("image/png"); 
	    Command<InputStream> cmd = new MyHistoryProcessInstanceDiagramCmd(processInstanceId);
	    OutputStream os = response.getOutputStream();
	    byte [] by = new byte[1024];
	    InputStream is = managementService.executeCommand(cmd);
	    while (IOUtils.read(is, by) != 0) {
			IOUtils.write(by, os); 
		}
	}

}
