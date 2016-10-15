package com.vmware.samples.vrops.rgraph.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.vmware.samples.vrops.rgraph.vrops.VropsClient;

@Configuration
public class VropsConfig {
	@Value("${vrops.url}")
	private String vropsUrl;
	
	@Value("${vrops.user}")
	private String vropsUser;
	
	@Value("${vrops.password}")
	private String vropsPassword;
	
	@Bean
	@Scope("singleton")
	VropsClient vropsClient() throws KeyManagementException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException, URISyntaxException, HttpException {
		return new VropsClient(vropsUrl, vropsUser, vropsPassword, true);
	}
}
