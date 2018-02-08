package activiti.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessOtherService {
	
	protected static Log log = LogFactory.getLog(ProcessOtherService.class);
	
	public boolean isJointTask(String taskId) {
		return false;
	}
	
}
