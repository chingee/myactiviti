package activiti.controllor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import activiti.controllor.RestControllor;

@RestController
public class ModelControllor extends RestControllor implements ModelDataJsonConstants {

	protected static Log log = LogFactory.getLog(ModelControllor.class);

	protected ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public String getStencilset() {
		InputStream stencilsetStream = this.getClass().getClassLoader()
				.getResourceAsStream("stencilset/stencilset.json");
		String json = "";
		try {
			json = IOUtils.toString(stencilsetStream, "utf-8");
		} catch (Exception e) {
			throw new ActivitiException("Error while loading stencil set", e);
		} finally {
			IOUtils.closeQuietly(stencilsetStream);
		}
		return json;
	}

	@RequestMapping(value = "/model/json/{modelId}", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getEditorJson(@PathVariable String modelId) {
		ObjectNode modelNode = null;
		Model model = repositoryService.getModel(modelId);
		if (model != null) {
			try {
				if (!StringUtils.isEmpty(model.getMetaInfo())) {
					JsonNode jsonNode = objectMapper.readTree(model.getMetaInfo());
					modelNode = (ObjectNode) jsonNode;
				} else {
					modelNode = objectMapper.createObjectNode();
					modelNode.put(MODEL_NAME, model.getName());
					modelNode.put(MODEL_REVISION, model.getVersion());
				}
				modelNode.put(MODEL_ID, model.getId());
				ObjectNode editorJsonNode = (ObjectNode) objectMapper
						.readTree(new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
				modelNode.set("model", editorJsonNode);
			} catch (Exception e) {
				log.error("Error creating model JSON", e);
				throw new ActivitiException("Error creating model JSON", e);
			}
		}
		return modelNode;
	}

	@RequestMapping(value = "/model/querylist")
	public ModelAndView queryModels() {
		List<Model> models = repositoryService.createModelQuery().orderByCreateTime().desc().list();
		ModelAndView mv = new ModelAndView("querymodels");
		mv.getModel().put("flows", models);
		return mv;
	}

	@RequestMapping(value = "/model/create", method = RequestMethod.POST)
	public ModelAndView createModel(@RequestParam Map<String, String> map) throws UnsupportedEncodingException {
		String key = map.get("key");
		String name = map.get("name");
		String description = map.get("description");

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.set("stencilset", stencilSetNode);

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(MODEL_NAME, name);
		modelObjectNode.put(MODEL_REVISION, 1);
		modelObjectNode.put(MODEL_DESCRIPTION, description);
		modelObjectNode.put("process_id", key); // 唯一标识符
		editorNode.set("properties", modelObjectNode);

		Model modelData = repositoryService.newModel();
		modelData.setKey(key);
		modelData.setName(name);
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);
		repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

		return queryModels();
	}

	@RequestMapping(value = "/model/delete/{modelId}", method = RequestMethod.POST)
	public String deleteModel(@PathVariable("modelId") String modelId) {
		repositoryService.deleteModel(modelId);
		return "model/querylist";
	}

	@RequestMapping(value = "/model/save/{modelId}", method = RequestMethod.PUT)
	public Map<String, Object> saveModel(@PathVariable("modelId") String modelId,
			@RequestBody MultiValueMap<String, String> values) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView();
		Model model = repositoryService.getModel(modelId);
		if (!StringUtils.isEmpty(model.getDeploymentId())) {
			Model modelData = repositoryService.newModel();
			modelData.setKey(model.getKey());
			modelData.setName(model.getName());
			modelData.setMetaInfo(model.getMetaInfo());
			modelData.setVersion(model.getVersion() + 1);

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), repositoryService.getModelEditorSource(modelId));
			repositoryService.addModelEditorSourceExtra(modelData.getId(),
					repositoryService.getModelEditorSourceExtra(modelId));

			mv.getModel().put("lastUpdated", false);
			return mv.getModel();
		}
		String name = values.getFirst("name");
		String description = values.getFirst("description");
		String jsonXml = values.getFirst("json_xml");
		String svgXml = values.getFirst("svg_xml");
		if (svgXml.indexOf("url(\"#") > 0) { // IE绘制，火狐没有这种情况
			svgXml = svgXml.replace("url(\"#", "url(#").replace("\")\"", ")\"");
			log.warn(" IE  diagram. 生成 svg_xml 有误，需要转换");
		}
		model.setName(name);
		log.debug(description);
		log.debug(jsonXml);
		repositoryService.saveModel(model);
		repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes("utf-8"));
		repositoryService.addModelEditorSourceExtra(model.getId(), svgXml.getBytes("utf-8"));

		mv.getModel().put("lastUpdated", true);
		return mv.getModel();
	}

	@RequestMapping(value = "/model/deploy/{modelId}", method = RequestMethod.POST)
	public String deployModel(@PathVariable("modelId") String modelId) throws JsonProcessingException, IOException {

		Model model = repositoryService.getModel(modelId);
		if (!StringUtils.isEmpty(model.getDeploymentId())) {
			Model modelData = repositoryService.newModel();
			modelData.setKey(model.getKey());
			modelData.setName(model.getName());
			modelData.setMetaInfo(model.getMetaInfo());
			modelData.setVersion(model.getVersion() + 1);

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), repositoryService.getModelEditorSource(modelId));
			repositoryService.addModelEditorSourceExtra(modelData.getId(),
					repositoryService.getModelEditorSourceExtra(modelId));

			return "model/querylist";
		}

		String data = new String(repositoryService.getModelEditorSource(modelId), "UTF-8");
		ObjectNode modelNode = (ObjectNode) objectMapper.readTree(data);
		BpmnModel bModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bModel, "UTF-8");

		String deploymentName = model.getKey() + "$" + model.getId();
		String processName = deploymentName + ".bpmn20.xml";

		Deployment deployment = repositoryService.createDeployment().name(deploymentName)
				.addString(processName, new String(bpmnBytes, "UTF-8")).deploy();

		model.setDeploymentId(deployment.getId());
		repositoryService.saveModel(model);
		return "processdefinition/querylist";
	}

}
