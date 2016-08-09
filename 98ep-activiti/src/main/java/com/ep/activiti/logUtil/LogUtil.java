package com.ep.activiti.logUtil;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;

import com.alibaba.fastjson.JSON;
import com.ep.activiti.common.SysUrlUtil;

public class LogUtil {

	private final Client client = ClientBuilder.newClient(new ClientConfig());
	private static final String url = "/LogStashProvider.svc/logging";

	public void writeLog(String logType, String title, Exception e) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(System.currentTimeMillis());
			map.put("dateTime", dateTime);
			map.put("logType", logType);
			map.put("title", title);
			map.put("message", getLog(e));
			map.put("ip", getIp());
			map.put("source", "activiti.yangguangqicai.com");
			client.target(SysUrlUtil.getValue("api_url_98ep"))
					.path(url)
					.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(JSON.toJSONString(map),
							MediaType.APPLICATION_JSON_TYPE));
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

	public String getLog(Exception e) {
		String str = "com.ep.activiti";
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] s = e.getStackTrace();
		if (s.length != 0) {
			for (int i = 0; i < s.length; i++) {
				if (i != s.length - 1) {
					if (s[i].toString().indexOf(str) != -1) {
						sb.append(s[i].toString() + "-------");
					}
				}
				if (s[i].toString().indexOf(str) != -1) {
					sb.append(s[i].toString());
				}
			}
		}

		return e.toString() + "-------" + sb;
	}

	public String getIp() throws UnknownHostException {
		String ip = "";
		try {
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost().getHostAddress();
			} else {
				Enumeration e1 = (Enumeration) NetworkInterface
						.getNetworkInterfaces();
				while (e1.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) e1.nextElement();
					if (!ni.getName().equals("eth0")) {
						continue;
					} else {
						Enumeration<?> e2 = ni.getInetAddresses();
						while (e2.hasMoreElements()) {
							InetAddress ia = (InetAddress) e2.nextElement();
							if (ia instanceof Inet6Address)
								continue;
							ip = ia.getHostAddress();
						}
						break;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return ip;
	}

	/**
	 * 判断当前系统是否windows
	 * 
	 * @return
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

}
