package activiti.override.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

public interface MyTaskService {

	/**
	 * 按流程图的线来进行退回
	 * @param taskId
	 * @return
	 */
	List<Task> back(String taskId, Map<String, Object> variables, String deleteReason);
	
	/**
	 * 退到指定流程图节点
	 * @param taskId
	 * @param targetActivitiId
	 * @return
	 */
	List<Task> back(String taskId, String targetActivitiId, Map<String, Object> variables, String deleteReason);
	
	/**
	 * 校验当前节点是否可以退回到指定节点
	 * @param sourceActivitiId
	 * @param targetActivitiId
	 * @return
	 */
	boolean validateTargetActivitiId(String sourceActivitiId, String targetActivitiId);
	
	/**
	 * 退回后删除指向结束的历史节点
	 * @param endActivitiId
	 * @param processInstanceId
	 */
	void deleteEndHistoricActivityInstance(String endActivitiId, String processInstanceId);
	
}
