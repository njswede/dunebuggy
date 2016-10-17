package net.virtualviking.dunesbuggy.controllers;

import java.io.IOException;
import java.util.HashMap;
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

import net.virtualviking.dunesbuggy.vro.VroClient;

@Controller
public class WorkflowController {
	@Autowired
	private VroClient vroClient;
	
	@RequestMapping(value="/workflows", method=RequestMethod.GET) 
	public String listWorkflows() {
		return "workflows";
	}
}
