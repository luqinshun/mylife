package com.ep.activiti.logUtil;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("test")
public class Test {

	@RequestMapping(value = "log", method = RequestMethod.GET)
	public void test(HttpServletRequest request) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("s", "你好");
			int i = (int) map.get("s");
		} catch (Exception e) {

			LogUtil log = new LogUtil();

			log.writeLog("error", "消息推送出错", e);

		}
	}

}
