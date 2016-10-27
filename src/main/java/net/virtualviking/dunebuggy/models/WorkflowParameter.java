package net.virtualviking.dunebuggy.models;

public class WorkflowParameter {
	private String type;
	
	private Object value;
	
	public WorkflowParameter() {
	}

	public WorkflowParameter(String type) {
		super();
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
