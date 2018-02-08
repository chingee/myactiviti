package activiti.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

public class BeanUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanUtil.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getContext() {
        checkApplicationContext();
        return applicationContext;
    }
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }
	
	@SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }
	
	/**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }
	
	private static void checkApplicationContext() {
        Assert.notNull(applicationContext,
                "applicationContext未注入,请在applicationContext.xml中定义BeanUtil");
    }


}
