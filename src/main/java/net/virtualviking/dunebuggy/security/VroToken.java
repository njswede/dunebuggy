package net.virtualviking.dunebuggy.security;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import net.virtualviking.dunebuggy.vro.VroClient;

public class VroToken implements Authentication {
	
	private static final long serialVersionUID = 3014845492903704270L;
		
	private VroClient vroClient;
	
	private String user;
	
	private String password;
	
	private String tenant;
	
	private String vraUrl;
	
	private static final Log log = LogFactory.getLog(VroToken.class);
	
	public VroToken(String vroUrl, String user, String password) throws ClientProtocolException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException {
		vroClient = new VroClient();
		vroClient.authenticate(vroUrl, user, password, true, true);
		this.user = user;
		this.password = password;
		log.debug("Created new token for user " + user);
	}

	@Override
	public String getName() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return vroClient.getAuthToken();
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.getName();
	}

	@Override
	public boolean isAuthenticated() {
		// If we exist we're authenticated!
		//
		return true;
	}

	@Override
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		throw new IllegalArgumentException("Not settable!");
	}
	
	public VroClient getVraClient() throws HttpException, KeyManagementException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException, URISyntaxException {
		return vroClient;
	}
}
