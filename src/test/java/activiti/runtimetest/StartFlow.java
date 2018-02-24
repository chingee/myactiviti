package activiti.runtimetest;

import org.junit.Test;

import activiti.ActivitiTest;
import activiti.override.command.DeleteUnusedExcutionCommand;

public class StartFlow extends ActivitiTest{

	@Test
	public void test(){
//		myTaskService.back("62580", null);
		DeleteUnusedExcutionCommand cmd = new DeleteUnusedExcutionCommand("62565");
		Void v = managementService.executeCommand(cmd);
		v.toString();
	}
	
}
