package com.mutouren.common.utils;


import java.io.UnsupportedEncodingException;

import com.mutouren.common.exception.ExceptionUtils;

public class HttpResult {
    private int statusCode;
    private String contentType;
    private String contentEncoding;
    private byte[] contentBytes;        
    
    public HttpResult(byte[] contentBytes, String contentType, String contentEncoding, int statusCode) {    	
    	this.contentBytes = contentBytes;
    	this.contentType = contentType;
    	this.contentEncoding = contentEncoding;
    	this.statusCode = statusCode;
    }
    
	public byte[] getContentBytes() {
		return contentBytes;
	}
	public String getContent(String charset) throws UnsupportedEncodingException {
		return new String(contentBytes, charset);
	}	
	public int getStatusCode() {
		return statusCode;
	}
	public String getContentType() {
		return contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}
	
	public boolean isSuccess() {
		return this.getStatusCode() == 200 ? true : false;
	}
	
	public String toString(String charset) {
		try {
			return String.format("statusCode:%s,contentType:%s,content:%s", statusCode, contentType, 
					new String(contentBytes, charset));
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.doUnChecked(e); 
		}
	}
}

