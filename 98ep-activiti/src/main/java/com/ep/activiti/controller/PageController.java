package com.ep.activiti.controller;

import java.net.URLDecoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

	@RequestMapping("/index")
	public String index() {
		String index = "index";
		return index;
	}

	@RequestMapping(value = "{pageName}", method = RequestMethod.GET)
	public ModelAndView toPage(@PathVariable("pageName") String pageName,
			@RequestParam(value = "a", required = false) String a,
			@RequestParam(value = "b", required = false) String b,
			@RequestParam(value = "c", required = false) String c)
			throws Exception {

		ModelAndView mv = new ModelAndView(pageName);
		if (a != null) {

			mv.addObject("a", URLDecoder.decode(a, "UTF-8"));
		}
		if (b != null) {
			mv.addObject("b", URLDecoder.decode(b, "UTF-8"));
		}
		if (c != null) {
			mv.addObject("c", URLDecoder.decode(c, "UTF-8"));
		}
		return mv;

	}

}
