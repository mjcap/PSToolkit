package com.capbpm.bpm.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BPMRestClientBaseTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	/*
	 * create client instance with connect timeout = 0
	 *                             read timeout    = 0
	 *                             authentication  = false
	 *                             
	 * should pass                            
	 */
	public void testBPMRestClientBase() {
		BPMRestClientBase rc = new BPMRestClientBase();
		assertEquals("connectTimeout must be 0", 0, rc.getConnectTimeout());
		assertEquals("readTimeout must be 0", 0, rc.getReadTimeout());
		assertEquals("authenticate must be false", false, rc.authenticate());
	}

	@Test
	/*
	 * create client instance with connect timeout = 1
	 *                             read timeout    = 2
	 *                             authentication  = false
	 *                             
	 * should pass                            
	 */
	public void testBPMRestClientBaseIntInt() {
		BPMRestClientBase rc = new BPMRestClientBase(1,2);
		assertEquals("connectTimeout must be 1", 1, rc.getConnectTimeout());
		assertEquals("readTimeout must be 2", 2, rc.getReadTimeout());
		assertEquals("authenticate must be false", false, rc.authenticate());
	}

	
	@Test
	/*
	 * create client instance with connect timeout = 3
	 *                             read timeout    = 4
	 *                             username        = joe
	 *                             password        = fabietz
	 *                             authentication  = true
	 * should pass                           
	 */
	public void testBPMRestClientBaseIntIntStringString() {
		BPMRestClientBase rc = new BPMRestClientBase(3,4,"joe","fabietz");
		assertEquals("connectTimeout must be 3", 3, rc.getConnectTimeout());
		assertEquals("readTimeout must be 4", 4, rc.getReadTimeout());
		assertEquals("userName must be joe", "joe", rc.getUsername());
		assertEquals("passWord must be fabietz", "fabietz", rc.getPassword());
		assertEquals("authenticate must be true", true, rc.authenticate());
	}


	
	@Test
	/*
	 * create instance that throws MalformedURLException
	 * 
	 * should pass (MalformedURLException will be thrown)
	 * 
	 */
	public void testLocalLoadRequestNonHmacThrowsMalformedURLException() throws MalformedURLException, IOException {
		
		thrown.expect(MalformedURLException.class);
		
		BPMRestClientBase rc = new BPMRestClientBase();
		rc.localLoadRequestNonHmac("htt://google.com", "POST");

	}
	
	@Test
	/*
	 * create instance that throws IOException
	 * 
	 * should pass (IOException will be thrown)
	 */
	public void testLocalLoadRequestNonHmacThrowsIOException() throws MalformedURLException, IOException {
		
		thrown.expect(IOException.class);
		
		BPMRestClientBase rc = new BPMRestClientBase();
		rc.localLoadRequestNonHmac("http://google.com", "PUT");

	}	

	@Test
	/*
	 * create instance that authenticates on username=Max
	 *                                       password=passw0rd
	 *                                       
	 * and POSTs:
	 * 
	 *  http://bpm.capbpm.com:9080/rest/bpm/wle/v1/service/SPX@getDeptsForUser?action=start&params={"userName":"Max"}
	 *  
	 * if successful will return: 
	 * 
	 *   {"status":"200",
	 *    "data":{"serviceStatus":"end",
	 *            "key":"@149",
	 *            "step":"End",
	 *            "data":{"depts":{"selected":[], <------------i'm calling this dataSubObj below
	 *                             "items":["Meat","Bakery","Deli","Grocery"],
	 *                             "@metadata":{"objectID":"b14c5836-74c8-416f-86d3-061dafd5a983","dirty":true,"shared":false}
	 *                            }
     *                    },
     *             "actions":null
     *           }
     *    } 
     *    
     *  verify success by making sure all keys are present 
     *                      AND
     *  items has 4 elements:  Meat, Bakery, Deli, Grocery
     *  
     *  should pass
	 */
	public void testLocalLoadRequestNonHmacReturnsJson() {
	    String url="http://bpm.capbpm.com:9080/rest/bpm/wle/v1/service/SPX@getDeptsForUser?action=start&params={\"userName\":\"Max\"}";

		
		String userName ="Max";
		String passWord = "passw0rd";

		try {
			BPMRestClientBase rc = new BPMRestClientBase(3000, 30000, userName, passWord);
			
			String retval = rc.localLoadRequestNonHmac(url,"POST");
			
			JSONObject jo = new JSONObject(retval);
			assertEquals("json response must have key=status", true, jo.has("status"));
			assertEquals("json response must have key=data", true, jo.has("data"));
			
			JSONObject dataObj = new JSONObject(jo.getString("data"));
			assertEquals("json key=data must have value with key=serviceStatus", true, dataObj.has("serviceStatus"));
			assertEquals("json key=data must have value with key=key", true, dataObj.has("key"));
			assertEquals("json key=data must have value with key=step", true, dataObj.has("step"));
			assertEquals("json key=data must have value with key=data", true, dataObj.has("data"));
			assertEquals("json key=data must have value with key=actions", true, dataObj.has("actions"));
			
			JSONObject dataSubObj = new JSONObject(dataObj.getString("data"));
			assertEquals("json subkey=data must have value with key=depts", true, dataSubObj.has("depts"));
			
			JSONObject depts = new JSONObject(dataSubObj.getString("depts"));
			assertEquals("json key=depts must have value with key=selected", true, depts.has("selected"));
			assertEquals("json key=depts must have value with key=items", true, depts.has("items"));
			assertEquals("json key=depts must have value with key=@metadata", true, depts.has("@metadata"));
			
			JSONArray items = depts.getJSONArray("items");
			assertEquals("json key=items must have 4 elements", 4, items.length());
			assertEquals("json key=items[0] must be Meat", "Meat", (String)items.getString(0));
			assertEquals("json key=items[1] must be Bakery", "Bakery", (String)items.getString(1));
			assertEquals("json key=items[2] must be Deli", "Deli", (String)items.getString(2));
			assertEquals("json key=items[3] must be Grocery", "Grocery", (String)items.getString(3));
			
			
		} catch (Exception e) {
			fail("JSON not returned");
			e.printStackTrace();
		}
	}

	@Test
	/**
	 *  create instance that does NO authentication and POSTs:
	 *  
	 *     http://httpbin.org/post (see http://stackoverflow.com/questions/5725430/http-test-server-that-accepts-get-post-calls
	 *                              for list of free services available at http://httpbin.org)
	 *     
	 *  if successful should return:
	 *  
	 *   {  "args": {},   
	 *      "data": "",   
	 *      "files": {},   
	 *      "form": {},   
	 *      "headers": {   "Accept": "",     
	 *                     "Content-Type": "application/json",     
	 *                     "Host": "httpbin.org",     
	 *                     "User-Agent": "Java/1.6.0_65"  
	 *                  },   
	 *      "json": null,   
	 *      "origin": "65.25.6.188",   
	 *      "url": "http://httpbin.org/post"
	 *   }  
	 *   
	 *   verify success by making sure the following keys are present:  args
	 *                                                                  data
	 *                                                                  files
	 *                                                                  form
	 *                                                                  headers
	 *                                                                  json
	 *                                                                  origin
	 *                                                                  url
	 *                                                                  
	 *  NOTE:  the json returned by http://httpbin.org/post is completely out of our control.  
	 *         so there is no telling if/when the json content will change.
	 *  
	 */
	public void testLocalLoadRequestNonHmacNoAuthenticationReturnsJson() {
	    String url="http://httpbin.org/post";

		try {
			BPMRestClientBase rc = new BPMRestClientBase(3000, 30000);
			
			String retval = rc.localLoadRequestNonHmac(url,"POST");
			
			JSONObject jo = new JSONObject(retval);
			assertEquals("json response must have key=args", true, jo.has("args"));
			assertEquals("json response must have key=data", true, jo.has("data"));
			assertEquals("json response must have key=files", true, jo.has("files"));
			assertEquals("json response must have key=form", true, jo.has("form"));
			assertEquals("json response must have key=headers", true, jo.has("headers"));
			assertEquals("json response must have key=json", true, jo.has("json"));
			assertEquals("json response must have key=origin", true, jo.has("origin"));
			assertEquals("json response must have key=url", true, jo.has("url"));
			
		} catch (Exception e) {
			fail("JSON not returned");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadInputStreamToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetResponseHttpsURLConnection() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetResponseHttpURLConnection() {
		fail("Not yet implemented");
	}

	@Test
	public void testJsonToObject() {
		fail("Not yet implemented");
	}

}
