package com.mutouren.common.utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {	
	
	public static boolean isBlank(String value) {
		return org.apache.commons.lang3.StringUtils.isBlank(value);
	}
	
	public static String byteArr2HexStr(byte[] arrB) {
		int iLen = arrB.length;
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			while (intTmp < 0) {
				intTmp += 256;
			}
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16).toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] hexStr2ByteArr(String strIn) {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i += 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[(i / 2)] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
	
	public static Map<String, String> getParamatersByUrl(String url) {
		Map<String, String> result = new HashMap<String, String>();
		if (isBlank(url)) return result;
		
		 url = url.substring(url.indexOf('?') + 1);
         String paramaters[] = url.split("&");
         for (String param : paramaters) {
             String values[] = param.split("=");
             if (values.length == 2)
            	 result.put(values[0].trim(), values[1].trim());
         }		
		return result;
	}
	
	public static void main(String[] args) {
//		String url = "http://ww.baidu.com? a=12&bbbb=xxx";
//		Map<String, String> map = getParamatersByUrl(url);
//		for(String key : map.keySet()) {
//			System.out.println(key + ":" + map.get(key));
//		}
		
	}
	
}
