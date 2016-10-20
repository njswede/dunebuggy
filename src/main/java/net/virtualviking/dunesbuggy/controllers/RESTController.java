package net.virtualviking.dunesbuggy.controllers;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.virtualviking.dunesbuggy.models.CatalogTreeBuilder;
import net.virtualviking.dunesbuggy.models.CatalogTreeNode;

@RestController()
public class RESTController extends AbstractController {
	@RequestMapping(value="/rest/catalogtree", method=RequestMethod.GET, produces="application/json")
	public CatalogTreeNode produceCatalogTree() throws JsonProcessingException, ClientProtocolException, IOException, HttpException {
		return new CatalogTreeBuilder(this.getVro()).build("Workflow");
	}
}
