package com.capbpm.bpm.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;



public class BPMRestClientBase {
	
	private boolean authenticate;
	private String username;
	private String password;
	private int connectTimeout;
	private int readTimeout;

	public BPMRestClientBase(){
		connectTimeout = 0;
		readTimeout = 0;
		authenticate = false;
	}
	
	public BPMRestClientBase(int ct, int rt){
		connectTimeout = ct;
		readTimeout = rt;
		authenticate = false;
	}
	
	public BPMRestClientBase(int ct, int rt, String uname, String pword){
		connectTimeout = ct;
		readTimeout = rt;
		username = uname;
		password = pword;
		authenticate = true;
	}

	public String localLoadRequestNonHmac(String urlString, String httpMethod) throws MalformedURLException, IOException{
		String json = "";
		HttpURLConnection urlConnection = null;
		try {

			urlConnection = connectionSetupHttpNonHmac(urlConnection, urlString, httpMethod, connectTimeout, readTimeout, null);
						
			json = getResponse(urlConnection);
			
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally{
			if (urlConnection != null){
			  urlConnection.disconnect();
			}
		}
		return json;
	}
			
	private HttpURLConnection connectionSetupHttpNonHmac(HttpURLConnection urlConnection, String urlString,  
			                                    String httpMethod, Integer connectTimeout, Integer readTimeout, JSONObject payload) throws IOException{

		if (authenticate){
			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication ("Max", "passw0rd".toCharArray());
			    }
			});
		}
		
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection(); 
		
		urlConnection.setRequestProperty("Content-Type", "application/json");
		urlConnection.setRequestMethod(httpMethod);
		urlConnection.setConnectTimeout(connectTimeout);
		urlConnection.setReadTimeout(readTimeout);
		return urlConnection;
	}
	
	public static String readInputStreamToString(InputStream is) throws IOException{
		InputStreamReader isr = new InputStreamReader(is);
		
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		int numCharsRead;
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		String result = sb.toString();
		return result;
	}
	
	
	public String getResponse(HttpsURLConnection urlConnection) throws IOException{
		BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String output;
		String json = "";
		while((output = response.readLine()) != null){
			json =  json + output;
		}
		return json;
	}
	
	public String getResponse(HttpURLConnection urlConnection) throws IOException{
		BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String output;
		String json = "";
		while((output = response.readLine()) != null){
			json =  json + output;
		}
		return json;
	}
	
	private static void setupLocalSSLConnection(HttpsURLConnection connection){
		try{
		TrustManager[] trustAllCerts = { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection
		.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		}catch(Exception e){
			
		}
	}
	
	private static HttpURLConnection writeToOutputStream(HttpURLConnection urlConnection, JSONObject jsonData) throws IOException{
		OutputStream os = urlConnection.getOutputStream();
		os.write(jsonData.toString().getBytes());
		os.flush();
		return urlConnection;
	}
	
	protected static Object jsonToObject(String jsonData, Object obj) throws JsonParseException, JsonMappingException, SecurityException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		obj = mapper.readValue(jsonData, obj.getClass());
		return obj;
	}

	public boolean authenticate() {
		return authenticate;
	}

	public void setAuthenticate(boolean authenticate) {
		this.authenticate = authenticate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
}
