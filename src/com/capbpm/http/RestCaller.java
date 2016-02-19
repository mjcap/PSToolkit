package com.capbpm.http;


import java.io.*;
import java.net.*;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

import org.apache.commons.codec.binary.Base64;

public class RestCaller
{

		public static void main(String args[]) throws Exception
		{

	//		String url = "http://maps.googleapis.com/maps/api/geocode/xml?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=false";
		//	String url = "https://169.53.3.36:9443/rest/bpm/wle/v1/search/meta/column";
			String url = "https://bpm.capbpm.com:9443/rest/bpm/wle/v1/search/meta/column";
//			String url = "https://bpm.education.capbpm.com:9443/rest/bpm/wle/v1/search/meta/column";
			//String url = "https://169.53.3.36:9080/rest/bpm/wle/v1/search/meta/column";
			
			String userName ="bpmadmin";
			String passWord = "FurYrF4W";
			String param= "";
			String retval = getJsonStringFromRestApiTrustOverride(url,param,userName, passWord);
			System.out.println(retval);
			
			
		}
		public static String getJsonStringFromRestApiTrustOverride(String apiUrl, String query, String userName, String passWord) throws Exception
		{
///
			
			String json="";

			try {
				String webPage = apiUrl;
				String name = userName;
				String password =passWord;

					
				       // Create a trust manager that does not validate certificate chains
			        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			                return null;
			            }
			            public void checkClientTrusted(X509Certificate[] certs, String authType) {
			            }
			            public void checkServerTrusted(X509Certificate[] certs, String authType) {
			            }
			        } };
			        // Install the all-trusting trust manager
			        final SSLContext sc = SSLContext.getInstance("SSL");
			        sc.init(null, trustAllCerts, new java.security.SecureRandom());
			        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			        // Create all-trusting host name verifier
			        HostnameVerifier allHostsValid = new HostnameVerifier() {
			            public boolean verify(String hostname, SSLSession session) {
			                return true;
			            }
			        };

			        // Install the all-trusting host verifier
			        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			        
				String authString = name + ":" + password;
				System.out.println("auth string: " + authString);
				byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
				String authStringEnc = new String(authEncBytes);
				System.out.println("Base64 encoded auth string: " + authStringEnc);

				URL url = new URL(webPage);
				URLConnection urlConnection = url.openConnection();
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();

				System.out.println("*** BEGIN ***");
				System.out.println(result);
				System.out.println("*** END ***");
				json  = result;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


			return json;
		}
		
		
		public static String getJsonStringFromRestApi(String apiUrl, String query, String userName, String passWord) throws Exception
		{
///
			
			String json="";

			try {
				String webPage = apiUrl;
				String name = userName;
				String password =passWord;

			        
				String authString = name + ":" + password;
				System.out.println("auth string: " + authString);
				byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
				String authStringEnc = new String(authEncBytes);
				System.out.println("Base64 encoded auth string: " + authStringEnc);

				URL url = new URL(webPage);
				
				HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			//urlConnection.setRequestProperty("Content-Type", "application/json");
				urlConnection.setRequestMethod("POST");
				
				
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();

				System.out.println("*** BEGIN ***");
				System.out.println(result);
				System.out.println("*** END ***");
				json  = result;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


			return json;
		}
		

}
