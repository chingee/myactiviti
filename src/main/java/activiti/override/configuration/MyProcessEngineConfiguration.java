package activiti.override.configuration;

import org.activiti.engine.ProcessEngine;
import org.activiti.spring.SpringProcessEngineConfiguration;

import activiti.override.factory.engine.MyProcessEngineImpl;
import activiti.override.service.MyTaskService;
import activiti.override.service.MyTaskServiceImpl;

/**
 * 重新定义mybatis配置sql
 * 
 * @author zhangqing
 *
 */
public class MyProcessEngineConfiguration extends SpringProcessEngineConfiguration {

	public static final String MY_MYBATIS_MAPPING_FILE = "mybatis/mappings.xml";
	
	private String mappingFile;

	protected MyTaskService myTaskService = new MyTaskServiceImpl(this);

	@Override
	public ProcessEngine buildProcessEngine() {
		init();
		return new MyProcessEngineImpl(this);
	}
	
	/**
	 * 重新定义mybatis配置sql
	 */
//	@Override
//	protected InputStream getMyBatisXmlConfigurationSteam() {
//		if(StringUtils.isEmpty(getMappingFile())){
//			return getResourceAsStream(MY_MYBATIS_MAPPING_FILE);
//		}
//		return getResourceAsStream(getMappingFile());
//	}

	@Override
	protected void initServices() {
		super.initServices();
		initService(myTaskService);
	}
	
	public MyTaskService getMyTaskService() {
		return myTaskService;
	}

	public void setMyTaskService(MyTaskService myTaskService) {
		this.myTaskService = myTaskService;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

}
