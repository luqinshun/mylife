package com.ep.activiti.common;

import java.util.Properties;

public class SysUrlUtil {
	private static Properties p = new Properties();

	/**
	 * 读取properties配置文件信息
	 */
	static {
		try {
			p.load(SysUrlUtil.class.getClassLoader().getResourceAsStream(
					"sysUrl.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key得到value的值
	 */
	public static String getValue(String key) {
		return p.getProperty(key);
	}

}
