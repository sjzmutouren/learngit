package com.mutouren.common.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtils {
	public static String beanToJson(Object bean) {		
		return JSON.toJSONString(bean);
	}
}
