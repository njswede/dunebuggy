package com.vmware.samples.vrops.rgraph;

public class Sample {
	private long timestamp;
	
	private double value;

	public Sample(long timestamp, double value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
