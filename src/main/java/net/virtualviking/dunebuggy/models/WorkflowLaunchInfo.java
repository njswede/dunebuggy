package net.virtualviking.dunebuggy.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.virtualviking.dunebuggy.vro.JSONHelper;

public class WorkflowLaunchInfo {
	private String workflowId;
	
	private Map<String, WorkflowParameter> parameters;
	
	public WorkflowLaunchInfo() {
	}

	public WorkflowLaunchInfo(String workflowId, Map<String, WorkflowParameter> parameters) {
		super();
		this.workflowId = workflowId;
		this.parameters = parameters;
	}
	
	public static WorkflowLaunchInfo fromJSON(Map<String, Object> json) {
		List<Map<String, Object>> parameters = JSONHelper.getListOfComplex(json, "input-parameters");
		Map<String, WorkflowParameter> outParameters = new HashMap<>();
		if(parameters != null) {
			for(Map<String, Object> parameter : parameters) {
				WorkflowParameter wfp = new WorkflowParameter((String) parameter.get("type"));
				outParameters.put((String) parameter.get("name"), wfp);
			}
		}
		return new WorkflowLaunchInfo(JSONHelper.getString(json, "id"), outParameters);
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public Map<String, WorkflowParameter> getParameters() {
		return parameters;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public void setParameters(Map<String, WorkflowParameter> parameters) {
		this.parameters = parameters;
	} 
}
