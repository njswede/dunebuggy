package net.virtualviking.dunesbuggy.controllers;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.virtualviking.dunesbuggy.models.BackendInfo;
import net.virtualviking.dunesbuggy.vro.VROHelper;

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
		return "workflowdetails";
	}
	
	/*@RequestMapping(value="/dashboard", method=RequestMethod.GET) 
	public String listWorkflows(Model model) throws ClientProtocolException, IOException, HttpException {
		Map<String, Object> result = this.getVro().get("/vco/api/workflows");
		VROHelper.simplifyAttributes(result);
		List<Map<String, Object>> list = JSONHelper.getListOfComplex(result, "link");
		model.addAttribute("workflows", list);
		if(log.isDebugEnabled())
			log.debug(new ObjectMapper().writeValueAsString(list));
		model.addAttribute("catalog", new CatalogBuilder().buildCatalog(this.getVro()));
		return "dashboard";
	}
	
	@RequestMapping(value="/workflowtokens", method=RequestMethod.GET) 
	public String listWorkflowTokens(Model model) throws ClientProtocolException, IOException, HttpException {
		model.addAttribute("tokens", this.getVro().get("/vco/api/workflows/{id}/executions"));
		return "workflowtokens";
	} */
}
