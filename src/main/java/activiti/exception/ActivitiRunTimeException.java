package activiti.exception;

public class ActivitiRunTimeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ActivitiRunTimeException(String error){
		super("自定义跳转流程时出错: " + error);
	}

}
