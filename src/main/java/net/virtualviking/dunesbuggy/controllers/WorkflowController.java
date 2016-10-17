package net.virtualviking.dunesbuggy.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.virtualviking.dunesbuggy.vro.JSONHelper;
import net.virtualviking.dunesbuggy.vro.VroClient;

@Controller
public class WorkflowController extends AbstractController {
	@RequestMapping(value="/workflows", method=RequestMethod.GET) 
	public String listWorkflows(Model model) throws ClientProtocolException, IOException, HttpException {
		List<Map<String, Object>> list = JSONHelper.getListOfComplex(this.getVro().get("/vco/api/workflows"), "link");
		List<Map<String, String>> content = new ArrayList<>();
		for(Map<String, Object> workflow : list) {
			String name = null;
			String id = null;
			String version = null;
			for(Map<String, Object> attr : JSONHelper.getListOfComplex(workflow, "attributes")) {
				String aName = (String) attr.get("name");
				if(aName.equals("name"))
					name = (String) attr.get("value");
				if(aName.equals("version"))
					version = (String) attr.get("value");
				if(aName.equals("id"))
					id = (String) attr.get("value");
			}
			Map<String, String> wf = new HashMap<>();
			wf.put("name", name);
			wf.put("id", id);
			wf.put("version", version);
			content.add(wf);
		}
		model.addAttribute("workflows", content);
		return "dashboard";
	}
	
	@RequestMapping(value="/workflowstokens", method=RequestMethod.GET) 
	public String listWorkflowTokens(Model model) throws ClientProtocolException, IOException, HttpException {
		model.addAttribute("tokens", this.getVro().get("/vco/api/workflows/{id}/executions"));
		return "workflows";
	}
}
