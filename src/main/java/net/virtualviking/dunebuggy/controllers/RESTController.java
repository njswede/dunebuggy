package net.virtualviking.dunebuggy.controllers;

import java.io.IOException;
import java.util.Map;

import javax.swing.table.TableRowSorter;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.virtualviking.dunebuggy.models.CatalogTreeBuilder;
import net.virtualviking.dunebuggy.models.CatalogTreeNode;
import net.virtualviking.dunebuggy.models.TableRow;
import net.virtualviking.dunebuggy.vro.JSONHelper;
import net.virtualviking.dunebuggy.vro.VROHelper;

@RestController()
public class RESTController extends AbstractController {
	@RequestMapping(value="/rest/catalogtree", method=RequestMethod.GET, produces="application/json")
	public CatalogTreeNode produceCatalogTree() throws JsonProcessingException, ClientProtocolException, IOException, HttpException {
		return new CatalogTreeBuilder(this.getVro()).build("Workflow");
	}
	
	@RequestMapping(value="/rest/executions/{id}", method=RequestMethod.GET, produces="application/json")
	public Map<String, Object> produceExecutionList(@PathVariable String id) throws JsonProcessingException, ClientProtocolException, IOException, HttpException {
		return VROHelper.simplifyAttributes(this.getVro().get("/vco/api/workflows/" + id + "/executions"));
	}
	
	@RequestMapping(value="/rest/executions/{workflowId}/{executionId}/logs", method=RequestMethod.GET, produces="application/json")
	public Map<String, Object> produceLogs(@PathVariable String workflowId, @PathVariable String executionId) throws JsonProcessingException, ClientProtocolException, IOException, HttpException {
		return this.getVro().get("/vco/api/workflows/" + workflowId + "/executions/" + executionId + "/logs");
	}
}
