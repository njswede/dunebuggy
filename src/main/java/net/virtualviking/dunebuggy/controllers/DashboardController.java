package net.virtualviking.dunebuggy.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.virtualviking.dunebuggy.models.BackendInfo;
import net.virtualviking.dunebuggy.models.WorkflowLaunchInfo;
import net.virtualviking.dunebuggy.models.WorkflowParameter;
import net.virtualviking.dunebuggy.vro.JSONHelper;
import net.virtualviking.dunebuggy.vro.VROHelper;

@Controller
public class DashboardController extends AbstractController {
	
	@Value("${vro.url}")
	private String vroUrl;
	
	private Log log = LogFactory.getLog(DashboardController.class);
	
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public String produceDashboard(Model model) {
		BackendInfo backend = new BackendInfo(vroUrl, this.getUser());
		model.addAttribute("backend", backend);
		return "dashboard";
	}
	
	@RequestMapping(value="/workflowdetails/{id}", method=RequestMethod.GET)
	public String getWorkflowDetails(@PathVariable("id") String id, Model model) throws ClientProtocolException, IOException, HttpException {
		Map<String, Object> wf = this.getVro().get("/vco/api/workflows/" + id);
		model.addAttribute("workflow", wf);
		Map<String, Object> tokens = this.getVro().get("/vco/api/workflows/" + id + "/executions");
		VROHelper.simplifyAttributes(tokens);
		model.addAttribute("tokens", tokens);
		model.addAttribute("launchInfo", WorkflowLaunchInfo.fromJSON(wf));
		return "workflowdetails";
	}
	
	@RequestMapping(value="/workflowdetails", method=RequestMethod.POST)
	public String submitRequest(@ModelAttribute("workflow") WorkflowLaunchInfo workflow) throws ClientProtocolException, IOException, HttpException {
		Map<String, Object> payload = new HashMap<>();
		List<Map<String, Object>> payloadParams = new ArrayList<>();
		payload.put("parameters", payloadParams);
		for(Map.Entry<String, WorkflowParameter> paramEntry : workflow.getParameters().entrySet()) {
			String name = paramEntry.getKey();
			WorkflowParameter param = paramEntry.getValue();
			String type = param.getType();
			Map<String, Object> pp = new HashMap<>();
			Map<String, Object> ppv = new HashMap<>();
			Map<String, Object> ppvv = new HashMap<>();
			ppvv.put("value", param.getValue());
			ppv.put(type, ppvv);
			pp.put("value", ppv);
			pp.put("name", name);
			pp.put("type", type);
			pp.put("scope", "local");
			payloadParams.add(pp);
		}
		this.getVro().postReturnString("/vco/api/workflows/" + workflow.getWorkflowId() + "/executions?sortOrders=-startDate", JSONHelper.encode(payload));
		return "dashboard";
	}
}
