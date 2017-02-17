package com.mutouren.common.utils;

public class UUIDUtils {
	
	public static String createBase64UUID() {
		String id = java.util.UUID.randomUUID().toString().replace("-", "");
		byte[] arr = StringUtils.hexStr2ByteArr(id);
		id = new String(java.util.Base64.getUrlEncoder().encode(arr));
		return id.replace("=", "");
	}

}
