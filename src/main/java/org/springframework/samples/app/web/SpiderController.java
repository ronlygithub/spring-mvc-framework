package org.springframework.samples.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.app.service.ISpiderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/spider")
public class SpiderController {
	@Autowired
	private ISpiderService spiderService;
	@RequestMapping(value="/{url}",method=RequestMethod.GET)
	public void get(@PathVariable("url") String url){
//		String content = spiderService.get(url);
	}

}
