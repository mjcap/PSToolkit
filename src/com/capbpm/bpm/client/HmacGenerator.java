package com.capbpm.bpm.client;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONObject;

public class HmacGenerator {
	private String utcTime = null;
	private String hash = null;

	public String getUtcTime() {
		return utcTime;
	}

	public void setUtcTime(String utcTime) {
		this.utcTime = utcTime;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void generateKey(String httpMethod, String uri, String key, JSONObject payload) {
		String hash = "";
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date = new Date();
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			this.setUtcTime(df.format(date));
			
			String contentType = "application/json";

			// Initiate the MAC with HmacSHA256 algorithm
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(
					Base64.decodeBase64(key.getBytes()), "HmacSHA256");

			// Feed the secret key to
			sha256_HMAC.init(secret_key);
	        
	        // String to generate HMAC for a REST GET call
	     	String input = null;
	     	if(httpMethod.equals("GET")){	
	        	input = httpMethod + utcTime + uri;
			} else {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(payload.toString().getBytes("UTF-8")); // Change this to "UTF-16" if needed
		        byte[] digest = md.digest();
		        String payloadHash = Base64.encodeBase64String(digest);
		        
				input = httpMethod + payloadHash + contentType + utcTime + uri;
			}

			// Generate the HMAC value for the string
			hash = Base64.encodeBase64String(sha256_HMAC.doFinal(input
					.getBytes()));
			// hash =
			// Base64.getEncoder().encodeToString((sha256_HMAC.doFinal(input.getBytes())));
		} catch (Exception e) {

		}
		this.setHash(hash);
	}
}
