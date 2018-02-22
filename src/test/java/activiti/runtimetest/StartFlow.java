package activiti.runtimetest;

import org.junit.Test;

import activiti.ActivitiTest;

public class StartFlow extends ActivitiTest{

	@Test
	public void test(){
		myTaskService.back("47625", null);
	}
	
}
