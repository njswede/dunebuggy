package net.virtualviking.dunebuggy.models;

public class BackendInfo {
	private final String url;
	
	private final String user;

	public BackendInfo(String url, String user) {
		super();
		this.url = url;
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}
}
