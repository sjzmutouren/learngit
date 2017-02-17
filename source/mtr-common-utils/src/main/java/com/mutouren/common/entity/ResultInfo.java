package com.mutouren.common.entity;


public class ResultInfo<T> {
	
	//public static final String SUCCESS = "0";
	
	private int code;
	private String message;
	private T info;	
	
	public ResultInfo() {
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getInfo() {
		return info;
	}

	public void setInfo(T info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return String.format("code:%s,message:%s,info:%s", code, message, info);
	}
}
