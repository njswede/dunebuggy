package net.virtualviking.dunesbuggy.vro;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.virtualviking.dunesbuggy.Sample;

public class VroClient {
	private HttpClient http;
	private HttpHost vraHost;
	
	private static Log log = LogFactory.getLog(VroClient.class);
	private String token;
	private long tokenExpiration;
	
	private static long expirationTolerance = 120000;
	
	public VroClient(String url, String username, String password, boolean trustSelfSigned, boolean trustInvalid) throws ClientProtocolException, IOException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, HttpException {
		if(trustSelfSigned) {
			SSLContextBuilder sslBld = new SSLContextBuilder();
			if(trustInvalid)
				sslBld.loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						return true;
					}
				});
			else
				sslBld.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			http = HttpClients.custom().
					setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).
					setSSLSocketFactory(new SSLConnectionSocketFactory(sslBld.build(), NoopHostnameVerifier.INSTANCE)).build();
		}
		else
			http = HttpClients.createDefault();
		URI uri = new URI(url);
		vraHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		Map<String, String> login = new HashMap<String, String>();
		login.put("username", username);
		login.put("password", password);
		Map<String, Object> auth = this.post("/suite-api/api/auth/token/acquire", JSONHelper.encode(login));
		token = "vRealizeOpsToken " + auth.get("token");
		tokenExpiration = (Long) auth.get("validity");
		log.info("Successfully authenticated " + username + ". Session expires: " + tokenExpiration);
	}
	
	public String getAuthToken() {
		return token;
	}
	
	public boolean isExpired() {
		return System.currentTimeMillis() + expirationTolerance > tokenExpiration;
	}
	
	public Map<String, Object> post(String uri, String jsonContent) throws ClientProtocolException, IOException, HttpException {
		return this.toMap(this.postReturnString(uri, jsonContent));
	}
	
	public Map<String, Object> post(String uri, Map<String, Object> jsonContent) throws ClientProtocolException, IOException, HttpException {
		return this.post(uri, new ObjectMapper().writeValueAsString(jsonContent));
	}
	
	public String postReturnString(String uri, String jsonContent) throws ClientProtocolException, IOException, HttpException {
		HttpPost post = new HttpPost(uri);
		try {
			StringEntity payload = new StringEntity(jsonContent);
			payload.setContentType("application/json");
			post.setEntity(payload);
			post.setHeader("Accept", "application/json");
			if(token != null)
				post.setHeader("Authorization", token);
			log.debug("POST " + uri);
			return IOUtils.toString(this.checkResponse(http.execute(vraHost, post)).getEntity().getContent(), Charset.defaultCharset());
		} finally {
			post.releaseConnection();
		}	
	}
	
	public Map<String, Object> getPaged(String uri, String pattern, int start, int limit) throws ClientProtocolException, IOException, HttpException {
		uri += "?page=" + start + "&pageSize=" + limit;
		if(pattern != null)
			uri += "&name=" + pattern;
		return this.get(uri);
	}
	
	public Map<String, Object> get(String uri) throws ClientProtocolException, IOException, HttpException {
		return this.toMap(this.getString(uri));
	}
	
	public String getString(String uri) throws UnsupportedOperationException, ClientProtocolException, IOException, HttpException {
		HttpGet get = new HttpGet(sanitizeURL(uri));
		try {
			if(token != null)
				get.setHeader("Authorization", token);
			get.setHeader("Accept", "application/json");
			log.debug("GET " + uri);
			return IOUtils.toString(this.checkResponse(http.execute(vraHost, get)).getEntity().getContent(), Charset.defaultCharset());
		} finally {
			get.releaseConnection();
		}
	}
	
	public byte[] getBinary(String uri, String contentType) throws UnsupportedOperationException, ClientProtocolException, IOException, HttpException {
		HttpGet get = new HttpGet(sanitizeURL(uri));
		try {
			if(token != null)
				get.setHeader("Authorization", token);
			get.setHeader("Accept", contentType);
			log.debug("GET " + uri);
			return IOUtils.toByteArray(this.checkResponse(http.execute(vraHost, get)).getEntity().getContent());
		} finally {
			get.releaseConnection();
		}
	}
	
	private HttpResponse checkResponse(HttpResponse response) throws HttpException, UnsupportedOperationException, IOException {
		int status = response.getStatusLine().getStatusCode();
		log.debug("HTTP status: " + status);
		if(status == 200 || status == 201)
			return response;
		log.debug("Error response from server: " + IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));
		throw new HttpException("HTTP Error: " + response.getStatusLine().getStatusCode() + " Reason: " + response.getStatusLine().getReasonPhrase());
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> toMap(String response) throws JsonParseException, JsonMappingException, UnsupportedOperationException, IOException {
		if(response == null || response.length() == 0) 
			return new HashMap<String, Object>();
		ObjectMapper om = new ObjectMapper();
		return JSONHelper.fixDates(om.readValue(response, Map.class));
	}
	
	private static String sanitizeURL(String url) {
		// The HTTP client doesn't like pipe characters in URLs.
		//
		return url.replace("|", "%7c");
	}
}
