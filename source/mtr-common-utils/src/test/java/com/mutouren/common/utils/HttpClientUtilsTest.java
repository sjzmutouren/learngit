package com.mutouren.common.utils;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

@SuppressWarnings("unused")
public class HttpClientUtilsTest {

	public static void main(String[] args) throws Exception {
		for(int i = 0; i < 5; i++) {
			testOneA_get();
		}
		System.out.println("game over!");	
	}
	
	private static void testOneA_get() throws Exception {

		String url = "http://localhost:8080/mtr-web-demo/api/demo";
		
		PoolingHttpClientConnectionManager connection = HttpClientUtils.createConnectionManager(2, 2);
		//PoolingHttpClientConnectionManager connection = null;
		HttpClientUtils b = new HttpClientUtils(url, "get", connection);
		b.addHeader("sessionxId", "1111111111111111111111");
		b.addHeader("connection", "close");
		b.addParameter("myNamexx", "fenghe");
		//b.setBody("id=123&name=fenghe", "gbk", "application/json");
		b.setByteArrayBody("i am china".getBytes());
		
		HttpResult info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("utf-8"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());
		
		info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("utf-8"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());
		
//		info = b.execute();
//		System.out.println("statusCode: " + info.getStatusCode());
//		System.out.println("content: " + info.getContent("utf-8"));
//		System.out.println("contentType: " + info.getContentType());
//		System.out.println("contentEncoding: " + info.getContentEncoding());
//		
//		info = b.execute();
//		System.out.println("statusCode: " + info.getStatusCode());
//		System.out.println("content: " + info.getContent("utf-8"));
//		System.out.println("contentType: " + info.getContentType());
//		System.out.println("contentEncoding: " + info.getContentEncoding());
//		
//		info = b.execute();
//		System.out.println("statusCode: " + info.getStatusCode());
//		System.out.println("content: " + info.getContent("utf-8"));
//		System.out.println("contentType: " + info.getContentType());
//		System.out.println("contentEncoding: " + info.getContentEncoding());		
		
//		connection.close();
			
	}
	
	private static void testOne() throws Exception {
		//String url = "https://sso.mutouren.com:8443/mtr-web-demo/api/demo";
		//String url = "https://192.168.1.104:8443/mtr-web-demo/api/demo";
		String url = "http://sso.mutouren.com:8080/mtr-web-demo/api/demo";
		//String url = "https://accounts.ctrip.com/member/login.aspx";
		// http://localhost:8080/mtr-web-demo/api/demo?myName=fenghe%E4%B8%AD%E5%9B%BD
		//String url = "https://www.baidu.com";
		
		PoolingHttpClientConnectionManager connection = HttpClientUtils.createConnectionManager(2, 2);
		//PoolingHttpClientConnectionManager connection = null;
		HttpClientUtils b = new HttpClientUtils(url, "get", connection);
//		b.setUrlCharset("gbk");
		//b.addHeader("sessionxId", "1111111111111111111111");
		//b.addHeader("connection", "close");
		//b.addParameter("myNamexx", "how are you中国");
		//b.setBody("id=123&name=fenghe", "gbk", "application/json");
//		b.setByteArrayBody("i am china".getBytes());
		HttpResult info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("utf-8"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());		
		
		System.out.println("game over!");
		
	}
	
	private static void testTwo() throws Exception {
		//String url = "https://sso.mutouren.com:8443/mtr-web-demo/api/demo";
		String url = "http://sso.mutouren.com:8080/mtr-web-demo/api/demo?delaytime=1000";

		PoolingHttpClientConnectionManager connection = HttpClientUtils.createConnectionManager(10, 2);
		//PoolingHttpClientConnectionManager connection = null;
		HttpClientUtils b = new HttpClientUtils(url, "get", connection);
		HttpResult info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("gbk"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());	
		Thread.sleep(20000);
		b = new HttpClientUtils(url, "get", connection);
		info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("gbk"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());
		Thread.sleep(20000);
		b = new HttpClientUtils(url, "get", connection);
		info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("gbk"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());
		Thread.sleep(20000);
		b = new HttpClientUtils(url, "get", connection);
		info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("gbk"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());
		Thread.sleep(120000);
		b = new HttpClientUtils(url, "get", connection);
		info = b.execute();
		System.out.println("statusCode: " + info.getStatusCode());
		System.out.println("content: " + info.getContent("gbk"));
		System.out.println("contentType: " + info.getContentType());
		System.out.println("contentEncoding: " + info.getContentEncoding());			
		
		System.out.println("game over!");
		connection.close();
	}
	
	private static void testThree() throws Exception {
		String url = "https://sso.mutouren.com:8443/mtr-web-demo/api/demo?delaytime=200";
		//String url = "http://sso.mutouren.com:8080/mtr-web-demo/api/demo?delaytime=200";
		PoolingHttpClientConnectionManager connection = HttpClientUtils.createConnectionManager(10, 2);
		int size = 10;
		Thread[] arrThread = new Thread[size];
		for(int i = 0; i < size; i++) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() { 
					HttpClientUtils b = new HttpClientUtils(url, "get", connection);					
					try {
						System.out.println(Thread.currentThread().getId() + "begin: ");
						HttpResult info = b.execute();	
						System.out.println(Thread.currentThread().getId() + "statusCode: " + info.getStatusCode());
						System.out.println(Thread.currentThread().getId() + "content: " + info.getContent("gbk"));
						System.out.println(Thread.currentThread().getId() + "contentType: " + info.getContentType());
						System.out.println(Thread.currentThread().getId() + "contentEncoding: " + info.getContentEncoding());						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			arrThread[i] = t;
			t.start();
		}
		
		for(int i = 0; i < size; i++) {
			arrThread[i].join();
		}
		
		System.out.println("game over!");
		connection.close();		
		Thread.sleep(2000);
		System.out.println("game over! 20000");
	}	

}
