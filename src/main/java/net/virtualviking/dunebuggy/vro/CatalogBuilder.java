package net.virtualviking.dunebuggy.vro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import net.virtualviking.dunebuggy.models.CatalogObject;

public class CatalogBuilder {	
	private Log log = LogFactory.getLog(CatalogBuilder.class);
	
	public CatalogBuilder() {
	}
		
	public List<CatalogObject> buildCatalog(VroClient client) throws ClientProtocolException, IOException, HttpException {
		List<CatalogObject> catalog = new ArrayList<>();
		Map<String, Object> response = client.get("/vco/api/categories?isRoot=true");
		VROHelper.simplifyAttributes(response);
		processChildren(client, JSONHelper.getListOfComplex(response, "link"), catalog, null);
		return catalog;
	}

	private void processChildren(VroClient client, List<Map<String, Object>> children, List<CatalogObject> catalog, CatalogObject parent) throws ClientProtocolException, IOException, HttpException {
		for(Map<String, Object> category : children) {
			if(!JSONHelper.getString(category, "rel").equals("down"))
				continue;
			String type = JSONHelper.getString(category, "attributes.type");
			boolean isFolder = "WorkflowCategory".equals(type);
			CatalogObject here = new CatalogObject(
					JSONHelper.getString(category, "attributes.name"),
					JSONHelper.getString(category, "attributes.id"),
					parent,
					isFolder);
			catalog.add(here);
			if(isFolder) {
				String id = JSONHelper.getString(category, "attributes.id");
				Map<String, Object> details = client.get("/vco/api/categories/" + id);
				VROHelper.simplifyAttributes(details);
				this.processChildren(client, JSONHelper.getListOfComplex(details, "relations.link"), catalog, here);
			}
		}
	}
}
