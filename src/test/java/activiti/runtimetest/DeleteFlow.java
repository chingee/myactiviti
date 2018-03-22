package activiti.runtimetest;

import org.junit.Test;

import activiti.ActivitiTest;
import activiti.override.command.DeleteEndHistoricActivityInstanceCommand;

public class DeleteFlow extends ActivitiTest{

	@Test
	public void test(){
//		runtimeService.deleteProcessInstance("92501", "错误");
		DeleteEndHistoricActivityInstanceCommand cmd = 
				new DeleteEndHistoricActivityInstanceCommand("usertask1", "92617");
		managementService.executeCommand(cmd);
	}
	
}
