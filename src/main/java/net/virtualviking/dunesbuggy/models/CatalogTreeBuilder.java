package net.virtualviking.dunesbuggy.models;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import net.virtualviking.dunesbuggy.vro.JSONHelper;
import net.virtualviking.dunesbuggy.vro.VROHelper;
import net.virtualviking.dunesbuggy.vro.VroClient;

public class CatalogTreeBuilder {
	private VroClient client;

	public CatalogTreeBuilder(VroClient client) {
		super();
		this.client = client;
	}
	
	public CatalogTreeNode build(String objectKind) throws ClientProtocolException, IOException, HttpException {
		CatalogTreeNode root = new CatalogTreeNode("Catalog", null, "#", null, null);
		Map<String, Object> response = client.get("/vco/api/categories?isRoot=true");
		VROHelper.simplifyAttributes(response);
		this.buildRecursive(objectKind, root, JSONHelper.getListOfComplex(response, "link"));
		return root;
	}
	
	private void buildRecursive(String objectKind, CatalogTreeNode parent, List<Map<String, Object>> data) throws ClientProtocolException, IOException, HttpException {
		for(Map<String, Object> node : data) {
			if(!JSONHelper.getString(node, "rel").equals("down"))
				continue;
			String type = JSONHelper.getString(node, "attributes.type");
			if(!type.startsWith(objectKind))
				continue;
			boolean isFolder = "WorkflowCategory".equals(type);
			String href = isFolder ? "#" : "/workflowdetails/" + JSONHelper.getString(node, "attributes.id");
			CatalogTreeNode here = new CatalogTreeNode(
					JSONHelper.getString(node, "attributes.name"), 
					isFolder ? null : "glyphicon glyphicon-file", 
					href, 
					null, 
					null);
			parent.addNode(here);
			if(isFolder) {
				String id = JSONHelper.getString(node, "attributes.id");
				Map<String, Object> details = client.get("/vco/api/categories/" + id);
				VROHelper.simplifyAttributes(details);
				this.buildRecursive(objectKind, here, JSONHelper.getListOfComplex(details, "relations.link"));
			}
		}
	}
}
