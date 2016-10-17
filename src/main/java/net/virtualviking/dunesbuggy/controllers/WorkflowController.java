package com.vmware.samples.vrops.rgraph.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vmware.samples.vrops.rgraph.vrops.VropsClient;

@Controller
public class GraphController {
	@Autowired
	private VropsClient vropsClient;
	
	@RequestMapping(value="/graphs/gauge", method=RequestMethod.GET) 
	public String produceGauge(
			@RequestParam String resourceKind, 
			@RequestParam String resourceName, 
			@RequestParam String statKey,
			@RequestParam double min, 
			@RequestParam double max, 
			@RequestParam(required=false, defaultValue="100") double redStart,
			@RequestParam(required=false, defaultValue="100") double greenEnd,
			@RequestParam(required=false, defaultValue="") String label,
			@RequestParam(required=false, defaultValue="") String unit,
			Model model) throws ClientProtocolException, IOException, HttpException {
		HashMap<String, Object> conf = new HashMap<String, Object>();
		
		// Handle missing/misconfigured greenEnd gracefully.
		//
		if(greenEnd > redStart)
			greenEnd = redStart;
		conf.put("min", min);
		conf.put("max", max);
		conf.put("value", vropsClient.getLatestStatByName(resourceKind, resourceName, statKey).getValue());
		conf.put("label", label);
		conf.put("unit", unit);
		conf.put("redStart", redStart);
		conf.put("greenEnd", greenEnd);
		model.addAttribute("config", conf);
		return "gauge";
	}
}
