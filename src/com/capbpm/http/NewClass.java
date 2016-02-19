package com.capbpm.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

public class NewClass {
	
	
	public static String getJsonStringFromRestApiHttpNoMap(String apiUrl, String query,

			String userName, String passWord, String method) throws Exception {

			// /



			String json = "";



			try {

			String webPage = apiUrl;

			String name = userName;

			String password = passWord;



			String authString = name + ":" + password;

			System.out.println("auth string: " + authString);

			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());

			String authStringEnc = new String(authEncBytes);

			System.out.println("Base64 encoded auth string: " + authStringEnc);



			URL url = new URL(webPage);



			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod(method);

			urlConnection.setRequestProperty("Authorization", "Basic "+ authStringEnc);

			urlConnection.setRequestProperty("Content-Type", "application/json");

			 
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

			json = result;



			} catch (MalformedURLException e) {

			e.printStackTrace();

			} catch (IOException e) {

			e.printStackTrace();

			}



			return json;

}
	
public static String getJsonStringFromRestApiHttp(String apiUrl, String query,

			String userName, String passWord, String method, Map<String,String> m) throws Exception {

			// /



			String json = "";



			try {

			String webPage = apiUrl;

			String name = userName;

			String password = passWord;



			String authString = name + ":" + password;

			System.out.println("auth string: " + authString);

			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());

			String authStringEnc = new String(authEncBytes);

			System.out.println("Base64 encoded auth string: " + authStringEnc);



			URL url = new URL(webPage);



			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod(method);

			urlConnection.setRequestProperty("Authorization", "Basic "+ authStringEnc);

			urlConnection.setRequestProperty("Content-Type", "application/json");

			 

			if (m!=null && m.keySet()!=null)

			{



			Iterator<String> keys = m.keySet().iterator();

			if (keys !=null)

			{while (keys.hasNext()) {

			String key = keys.next();

			String val = (String)m.get(key);

			urlConnection.setRequestProperty(key, val);

			}

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

			String result = sb.toString();



			System.out.println("*** BEGIN ***");

			System.out.println(result);

			System.out.println("*** END ***");

			json = result;



			} catch (MalformedURLException e) {

			e.printStackTrace();

			} catch (IOException e) {

			e.printStackTrace();

			}



			return json;

}
	
public static String getJsonStringFromRestApiHttps(String apiUrl, String query,

String userName, String passWord, String method, Map<String,String> m) throws Exception {

// /



String json = "";



try {

String webPage = apiUrl;

String name = userName;

String password = passWord;



String authString = name + ":" + password;

System.out.println("auth string: " + authString);

byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());

String authStringEnc = new String(authEncBytes);

System.out.println("Base64 encoded auth string: " + authStringEnc);



URL url = new URL(webPage);



HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

urlConnection.setRequestMethod(method);

urlConnection.setRequestProperty("Authorization", "Basic "+ authStringEnc);

urlConnection.setRequestProperty("Content-Type", "application/json");

 

if (m!=null && m.keySet()!=null)

{



Iterator<String> keys = m.keySet().iterator();

if (keys !=null)

{while (keys.hasNext()) {

String key = keys.next();

String val = (String)m.get(key);

urlConnection.setRequestProperty(key, val);

}

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

String result = sb.toString();



System.out.println("*** BEGIN ***");

System.out.println(result);

System.out.println("*** END ***");

json = result;



} catch (MalformedURLException e) {

e.printStackTrace();

} catch (IOException e) {

e.printStackTrace();

}



return json;

}

public static void main(String[] args) throws Exception{

	//String json=getJsonStringFromRestApi("https://httpbin.org/get", "", "", "", "GET", new HashMap<String,String>());
	//String json=getJsonStringFromRestApi("https://bpm.capbpm.com:9443/rest/bpm/wle/v1/process/currentlyExecuting", "", "Danny", "passw0rd", "POST", 
	//		                 new HashMap<String,String>());
	
	String json=getJsonStringFromRestApiHttp("http://localhost:8080/crudServiceCassandra/getAppNameSnapshotId?input=https%3A%2F%2Fbpm.capbpm.com%3A9443%2Frest%2Fbpm%2Fwle%2Fv1%2Fexposed%2Fprocess", "", "", "p", "POST", 
            new HashMap<String,String>());	
   System.out.println("json="+json);
}
}