package com.capbpm.http;

import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.*;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.capbpm.http.HmacGenerator;

public class BPMRestClient {
	
	public BPMRestClient(){
		
	}

	public static String getJsonStringFromRestApiTrustOverride(String apiUrl,
			String query, String userName, String passWord, String method,
			Map<String, String> headerMap, boolean overRide) throws Exception {
		String json = "";

		try {
			String webPage = apiUrl;
			String name = userName;
			String password = passWord;

			if (overRide)
				setAllTrust();

			String authString = name + ":" + password;
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
	
			URL url = new URL(webPage);

			HttpsURLConnection urlConnection = (HttpsURLConnection) url
					.openConnection();

			urlConnection.setRequestProperty("Authorization", "Basic "
					+ authStringEnc);

			if (headerMap != null) {
				Iterator<String> i = headerMap.keySet().iterator();
				while (i.hasNext()) {
					String k = i.next();
					String v = headerMap.get(k);
					urlConnection.setRequestProperty(k, v);
				}
			}
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			json = sb.toString();


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	private static void setAllTrust() throws NoSuchAlgorithmException,
			KeyManagementException {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };
		// Install the all-trusting trust manager
		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	public static String getJsonStringFromRestApi(String apiUrl, String query,
			String userName, String passWord, String method,
			Map<String, String> m) throws Exception {
		// /

		String json = "";
		json = getJsonStringFromRestApiTrustOverride(apiUrl, query, userName,
				passWord, method, m, false);
		return json;
	}
			
	public static String loadRequestHTTP(String urlString, String uriString, String httpMethod, Integer connectTimeout, Integer readTimeout, Map<String, String> props, JSONObject payload) throws Exception{
		String json = "";
		
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = connectionSetupHttp(urlConnection, urlString, props, httpMethod, connectTimeout, readTimeout, payload);
//			InputStream is = getURLInputStream(urlConnection);
//			json = readInputStreamToString(is);
			json = getResponse(urlConnection);
			if(urlConnection.getResponseCode() != 200){
				json = "{\"errorStatusCode\":\"" + urlConnection.getResponseCode() + "\", \"errorMessage\":\"" + urlConnection.getResponseMessage() + "\"}";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			json = buildErrorResponse(998, "MalformedURLException in rest client.");
		} catch (IOException e) {
			e.printStackTrace();
			json = buildErrorResponse(999, "IOException in rest client.");
		}finally{
			urlConnection.disconnect();
		}
		return json;
	}
	
	public static String loadRequestHTTP(String dataPowerConsumerId, String key, String urlString, String uriString, String httpMethod, Integer connectTimeout, Integer readTimeout) throws Exception{
		String json = "";
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = connectionSetupHttp(urlConnection, dataPowerConsumerId, key, urlString, uriString, httpMethod, connectTimeout, readTimeout, null);
//			InputStream is = getURLInputStream(urlConnection);
//			json = readInputStreamToString(is);
			json = getResponse(urlConnection);
			if(urlConnection.getResponseCode() != 200){
				json = "{\"errorStatusCode\":\"" + urlConnection.getResponseCode() + "\", \"errorMessage\":\"" + urlConnection.getResponseMessage() + "\"}";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			json = buildErrorResponse(998, "MalformedURLException in rest client.");
		} catch (IOException e) {
			e.printStackTrace();
			json = buildErrorResponse(999, "IOException in rest client.");
		}finally{
			urlConnection.disconnect();
		}
		return json;
	}
	
	public static String loadRequestHTTPS(String dataPowerConsumerId, String key, String urlString, String uriString, String httpMethod, Integer connectTimeout, Integer readTimeout) throws Exception{
		String json = "";
		HttpsURLConnection urlConnection = null;
		//HttpURLConnection urlConnection = null;
		try {
			urlConnection = connectionSetupHttps(urlConnection, dataPowerConsumerId, key, urlString, uriString, httpMethod, connectTimeout, readTimeout, null);
			//urlConnection = connectionSetupHttp(urlConnection, dataPowerConsumerId, key, urlString, uriString, httpMethod, connectTimeout, readTimeout, null);
//			InputStream is = getURLInputStream(urlConnection);
//			json = readInputStreamToString(is);
			json = getResponse(urlConnection);
			if(urlConnection.getResponseCode() != 200){
				json = "{\"errorStatusCode\":\"" + urlConnection.getResponseCode() + "\", \"errorMessage\":\"" + urlConnection.getResponseMessage() + "\"}";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			json = buildErrorResponse(998, "MalformedURLException in rest client.");
		} catch (IOException e) {
			e.printStackTrace();
			json = buildErrorResponse(999, "IOException in rest client.");
		}finally{
			urlConnection.disconnect();
		}
		return json;
	}
	
	public static InputStream getURLInputStream(HttpsURLConnection urlConnection) throws IOException{
		return urlConnection.getInputStream();
		
	}
	
	public static String localLoadRequest(String dataPowerConsumerId, String key, String urlString, String uriString, String httpMethod, Integer connectTimeout, Integer readTimeout) throws Exception{
		String json = "";
		HttpsURLConnection urlConnection = null;
		try {
			setupLocalSSLConnection(urlConnection);
			urlConnection = connectionSetupHttps(urlConnection, dataPowerConsumerId, key, urlString, uriString, httpMethod, connectTimeout, readTimeout, null);
//			InputStream is = getURLInputStream(urlConnection);
//			json = readInputStreamToString(is);
			json = getResponse(urlConnection);
			buildErrorResponse(urlConnection.getResponseCode(), urlConnection.getResponseMessage());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			json = buildErrorResponse(998, "MalformedURLException in rest client.");
		} catch (IOException e) {
			e.printStackTrace();
			json = buildErrorResponse(999, "IOException in rest client.");
		} finally{
			urlConnection.disconnect();
		}
		return json;
	}
	
	private static HttpsURLConnection connectionSetupHttps(HttpsURLConnection urlConnection, String dataPowerConsumerId, 
			                                               String key, String urlString, String uriString, String httpMethod, 
			                                               Integer connectTimeout, Integer readTimeout, 
			                                               JSONObject payload) throws IOException{
		HmacGenerator mac = new HmacGenerator();
		mac.generateKey(httpMethod, uriString, key, payload);
		
		URL url = new URL(urlString);
		urlConnection = (HttpsURLConnection) url.openConnection(); 
		urlConnection.setRequestProperty("WMT-SVC-ConsumerId", dataPowerConsumerId);
		urlConnection.setRequestProperty("WMT-SVC-Date", mac.getUtcTime());
		urlConnection.setRequestProperty("WMT-SVC-Hash", mac.getHash());
		urlConnection.setRequestProperty("Content-Type", "application/json");
		urlConnection.setRequestMethod(httpMethod);
		urlConnection.setConnectTimeout(connectTimeout);
		urlConnection.setReadTimeout(readTimeout);
		return urlConnection;
	}
	
	
	private static HttpURLConnection connectionSetupHttp(HttpURLConnection urlConnection,  
			                                              String urlString,  
			                                              Map<String,String> requestProps, 
			                                              String httpMethod, 
			                                              Integer connectTimeout, 
			                                              Integer readTimeout, 
			                                              JSONObject payload) throws IOException{
		
		String propName;
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection(); 
		
		Iterator<String> i = requestProps.keySet().iterator();
		while (i.hasNext()){
			propName = i.next();
			urlConnection.setRequestProperty(propName, requestProps.get(propName));
		}
		urlConnection.setRequestMethod(httpMethod);
		urlConnection.setConnectTimeout(connectTimeout);
		urlConnection.setReadTimeout(readTimeout);
		return urlConnection;
	}
	
	private static HttpURLConnection connectionSetupHttp(HttpURLConnection urlConnection, String dataPowerConsumerId, String key, String urlString, String uriString, String httpMethod, Integer connectTimeout, Integer readTimeout, JSONObject payload) throws IOException{
		HmacGenerator mac = new HmacGenerator();
		mac.generateKey(httpMethod, uriString, key, payload);
		
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection(); 
		urlConnection.setRequestProperty("WMT-SVC-ConsumerId", dataPowerConsumerId);
		urlConnection.setRequestProperty("WMT-SVC-Date", mac.getUtcTime());
		urlConnection.setRequestProperty("WMT-SVC-Hash", mac.getHash());
		urlConnection.setRequestProperty("Content-Type", "application/json");
		urlConnection.setRequestMethod(httpMethod);
		urlConnection.setConnectTimeout(connectTimeout);
		urlConnection.setReadTimeout(readTimeout);
		return urlConnection;
	}
	
	private static String buildErrorResponse(int responseCode, String errorMessage){
		String json = null;
		json = "{\"errorStatusCode\":\"" + responseCode + "\", \"errorMessage\":\"" + errorMessage + "\"}";
		return json;
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
	
	
	public static String getResponse(HttpsURLConnection urlConnection) throws IOException{
		BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String output;
		String json = "";
		while((output = response.readLine()) != null){
			json =  json + output;
		}
		return json;
	}
	
	public static String getResponse(HttpURLConnection urlConnection) throws IOException{
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
	
	public static String saveRequest(String dataPowerConsumerId, String key, String urlString, String uriString, String httpMethod, String jsonData, Integer connectTimeout, Integer readTimeout) throws Exception{
		String json = "";
		HttpsURLConnection urlConnection = null;
		JSONObject payload = new JSONObject(jsonData);
		try {
			urlConnection = connectionSetupHttps(urlConnection, dataPowerConsumerId, key, urlString, uriString, httpMethod, connectTimeout, readTimeout, payload);
			urlConnection.setDoOutput(true);
			writeToOutputStream(urlConnection, payload);
			json = getResponse(urlConnection);
			if(urlConnection.getResponseCode() != 200){
				json = buildErrorResponse(urlConnection.getResponseCode(), urlConnection.getResponseMessage());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			json = buildErrorResponse(998, "MalformedURLException in rest client.");
		} catch (IOException e) {
			e.printStackTrace();
			json = buildErrorResponse(999, "IOException in rest client.");
		}
		finally{
			urlConnection.disconnect();
		}
		return json;
	}
	
	public static String saveRequestLocal(String dataPowerConsumerId, String key, String urlString, String uriString, String httpMethod, String jsonData, Integer connectTimeout, Integer readTimeout) throws Exception{
		String json = "";
//		HttpsURLConnection urlConnection = null;
		HttpURLConnection urlConnection = null;
		JSONObject payload = new JSONObject(jsonData);
		try {
//			setupLocalSSLConnection(urlConnection);
			urlConnection = connectionSetupHttp(urlConnection, dataPowerConsumerId, key, urlString, uriString, httpMethod, connectTimeout, readTimeout, payload);
			urlConnection.setDoOutput(true);
			writeToOutputStream(urlConnection, payload);
			json = getResponse(urlConnection);
			if(urlConnection.getResponseCode() != 200){
				json = buildErrorResponse(urlConnection.getResponseCode(), urlConnection.getResponseMessage());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			json = buildErrorResponse(998, "MalformedURLException in rest client.");
		} catch (IOException e) {
			e.printStackTrace();
			json = buildErrorResponse(999, "IOException in rest client.");
		}
		finally{
			urlConnection.disconnect();
		}
		return json;
	}
	
	public static void main(String[] args){
		//localLoadRequest(String dataPowerConsumerId, String key, String urlString, String uriString, String httpMethod, Integer connectTimeout, Integer readTimeout)
		try {
			String loadRequestHTTPresult = loadRequestHTTP("C[B@4ba65bf9", "qCbco11gWkNnfmDuscsawvyPnk1I6C6kyW0bWp6lK1vMm1mVzXZdF9TfqDkePL3T2iQri59s2aeqfdKXhSdq8A==C[B@4ba65bf9", 
					"http://odm.capbpm.com:8080/crudServiceCassandra/listColumns?input=customer2", 
					"http://odm.capbpm.com:8080/crudServiceCassandra/listColumns?input=customer2", 
					"POST", new Integer(15000), new Integer(15000));
			System.out.println("loadRequestHTTPresult="+loadRequestHTTPresult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
						
			String json=getJsonStringFromRestApi("https://bpm.capbpm.com:9443/rest/bpm/wle/v1/process/currentlyExecuting", "", "Danny", "passw0rd", "POST", 
					                 new HashMap<String,String>());
			System.out.println("json="+json);
			
			String json2=getJsonStringFromRestApi("https://httpbin.org/get", "", "", "", "GET", 
	                 new HashMap<String,String>());
		    System.out.println("json2="+json2);	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
