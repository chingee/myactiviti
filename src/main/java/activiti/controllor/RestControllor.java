package activiti.controllor;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RestController;

import activiti.override.service.MyTaskService;
import activiti.util.ImageUtil;

@RestController
public class RestControllor {
	
	protected static Log log = LogFactory.getLog(RestControllor.class);
	
	@Resource
	protected RepositoryService repositoryService;
	@Resource
	protected RuntimeService runtimeService;
	@Resource
	protected TaskService taskService;
	@Resource
	protected HistoryService historyService;
	@Resource
	protected ManagementService managementService;
	
	@Resource
	protected MyTaskService myTaskService;

	//生成验证码图片  
//	@RequestMapping("/valicode")
	public void valicode(HttpServletResponse response, HttpSession session) throws Exception{  
	    //利用图片工具生成图片  
	    //第一个参数是生成的验证码，第二个参数是生成的图片  
	    Object[] objs = ImageUtil.createImage();  
	    //将验证码存入Session  
	    session.setAttribute("imageCode",objs[0]);  
	    //将图片输出给浏览器  
	    BufferedImage image = (BufferedImage) objs[1];  
	    response.setContentType("image/png");  
	    OutputStream os = response.getOutputStream();  
	    ImageIO.write(image, "png", os);  
	}
	
}
