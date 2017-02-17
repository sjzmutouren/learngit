package com.mutouren.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.mutouren.common.exception.ExceptionUtils;

public class HttpClientUtils {
	
	private final static String POST = "POST";
	private final static String GET = "GET";
	private final static int defaultConnectTimeout = 5000;
	private final static int defaultReadTimeout = 30000;    	
	private final static int defaultConnectionRequestTimeout = 5000;
	private final static String defaultCharset = "UTF-8";
	
	private String url;
	private String method;
	private HttpClientConnectionManager connectManager;
	private List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
	private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	
	private String urlCharset = defaultCharset;
	
	private int connectTimeout = defaultConnectTimeout;
	private int readTimeout = defaultReadTimeout;
	private int connectionRequestTimeout = defaultConnectionRequestTimeout;
	
	private HttpEntity bodyEntity = null;
	
	public void setUrlCharset(String urlCharset) {
		this.urlCharset = urlCharset;
	}	
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}	
		
	public HttpClientUtils(String url) {
		this(url, POST);
	}	
	public HttpClientUtils(String url, String method) {
		this(url, method, null);
	}	
	public HttpClientUtils(String url, String method, HttpClientConnectionManager connectManager) {
		initCheck(url, method);
		this.url = url.trim();
		this.method = method.trim().toUpperCase();
		this.connectManager = connectManager;
	}
	
	private void initCheck(String url, String method) {
		if(StringUtils.isBlank(url)) {
			throw new IllegalArgumentException("url is blank"); 
		}
		if(StringUtils.isBlank(method)) {
			throw new IllegalArgumentException("method is blank");
		}
		method = method.trim().toUpperCase();
		if (!POST.equals(method) && !GET.equals(method)) {
			throw new IllegalArgumentException("method must is POST or GET");
		}
	}
	
	public void addHeader(String name, String value) {
		this.headers.add(new BasicNameValuePair(name, value));
	}
	
	public void addParameter(String name, String value) {
		this.parameters.add(new BasicNameValuePair(name, value));
	}
	public void setByteArrayBody(byte[] byteArray) {
		bodyEntity = new ByteArrayEntity(byteArray);
	}
	public void setBody(String content, String charset) {
		bodyEntity = new StringEntity(content.toString(), charset);
	}
	public void setBody(String content, String charset, String contentType) {
		StringEntity entity = new StringEntity(content.toString(), charset);
		entity.setContentType(String.format("%s; charset=%s", contentType, charset));
		bodyEntity = entity;
	}
	public void setJsonBody(String content, String charset) {
		StringEntity entity = new StringEntity(content.toString(), charset);
		entity.setContentType(String.format("%s; charset=%s", "application/json", charset));
		bodyEntity = entity;
	}		
	public void setXmlBody(String content, String charset) {
		StringEntity entity = new StringEntity(content.toString(), charset);
		entity.setContentType(String.format("%s; charset=%s", "application/xml", charset));
		bodyEntity = entity;
	}		
	
	public HttpResult execute() throws ClientProtocolException, URISyntaxException, IOException {
		if (GET.equals(this.method)) {
			return execute_get();
		} else if(POST.equals(this.method)) {
			return execute_post();
		} else {
			throw new RuntimeException("HttpClientUtils fatal logic error");
		}
	} 
	
	private HttpResult execute_get() throws URISyntaxException, ClientProtocolException, IOException {
//		URIBuilder ub = new URIBuilder();
//		ub.setPath(url);
//		ub.setParameters(parameters);

		HttpGet httpGet = new HttpGet(createUrl(url, parameters, urlCharset));		
		return getResult(httpGet);
	}
	
	private HttpResult execute_post() throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		if (bodyEntity == null) {
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, urlCharset));
		} else {
			httpPost.setEntity(bodyEntity);
		}
		return getResult(httpPost);
	}
	
	private HttpResult getResult(HttpRequestBase request) throws ClientProtocolException, IOException {
		for(NameValuePair pair : headers) {
			request.addHeader(pair.getName(), pair.getValue());
		}		
		
		CloseableHttpClient httpClient = getHttpClient();
		try {
			CloseableHttpResponse response = httpClient.execute(request);		
			HttpResult result = convertToHttpResult(response);
			response.close();
			return result;
		} finally {
			if ((httpClient != null)&&(connectManager == null)) {
				httpClient.close();
			}	
		}
	}
	
	private HttpResult convertToHttpResult(CloseableHttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		int statusCode = response.getStatusLine().getStatusCode();
		byte[] contentBytes = EntityUtils.toByteArray(entity); 		
		String contentType = ""; 
		String contentEncoding = "";  

        Header headContentType = entity.getContentType();
        if (headContentType != null) {
        	contentType = headContentType.getValue();
        }
        Header headContentEncoding = entity.getContentEncoding();
        if (headContentEncoding != null) {
        	contentEncoding = headContentEncoding.getValue();
        }
		
		return new HttpResult(contentBytes, contentType, contentEncoding, statusCode);
	}
	
	private CloseableHttpClient getHttpClient() {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		if (connectManager != null) {
			httpClientBuilder.setConnectionManager(connectManager);
		}
		
		// only for self signed certification
		if (isSSL()) {
			httpClientBuilder.setSSLSocketFactory(createSSLConnSocketFactory());
		}
		httpClientBuilder.setDefaultRequestConfig(getRequestConfig());
		return httpClientBuilder.build();		
	}
	
	private boolean isSSL() {
		return "https".equals(url.substring(0, 5).trim());
	}
	
	private RequestConfig getRequestConfig() {
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(connectTimeout);
        configBuilder.setSocketTimeout(readTimeout);
        configBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
        configBuilder.setStaleConnectionCheckEnabled(true);
        return configBuilder.build();
	}
	
	public static String createUrl(String url, List<NameValuePair> listPair, String charset) throws UnsupportedEncodingException {
		if (listPair.size() == 0) return url;
		
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		boolean isFirst = true;
		if (url.indexOf("?") > 0) {
			isFirst = false;
		}
		
		for(NameValuePair pair : listPair) {
			if (isFirst) {
				sb.append(String.format("?%s=%s", URLEncoder.encode(pair.getName(), charset), URLEncoder.encode(pair.getValue(), charset)));
				isFirst = false;
			} else {
				sb.append(String.format("&%s=%s", URLEncoder.encode(pair.getName(), charset), URLEncoder.encode(pair.getValue(), charset)));
			}
		}
		
		return sb.toString();
	}
	
//	private static SSLConnectionSocketFactory createSSLConnSocketFactory1()  {
//		try {
//			SSLContext sslContext = SSLContexts.custom().useTLS().build();
//	        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
//	
//	            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
//	                    throws CertificateException {
//	            }
//	
//	            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
//	                    throws CertificateException {
//	            }
//	
//	            public X509Certificate[] getAcceptedIssuers() {
//	                return null;
//	            }
//	        } }, null);	
//	        
//	        return new SSLConnectionSocketFactory(sslContext);
//		} catch (Throwable t) {
//			throw new RuntimeException(t);
//		}
//	}
	
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLConnectionSocketFactory sslFactory = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();

			sslFactory = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
				@Override
				public void verify(String host, SSLSocket ssl) throws IOException {
				}
				@Override
				public void verify(String host, X509Certificate cert) throws SSLException {
				}
				@Override
				public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
				}
			});
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.doUnChecked(e);
		}
		return sslFactory;
	}
		
	public static PoolingHttpClientConnectionManager createConnectionManager(int maxTotal, int defaultMaxPerRoute) {			
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", createSSLConnSocketFactory()).build();
        
		PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		result.setMaxTotal(maxTotal);
		result.setDefaultMaxPerRoute(defaultMaxPerRoute);		
		return result;		
	}
	
	public static void main(String[] args) throws Exception {

	}

}

