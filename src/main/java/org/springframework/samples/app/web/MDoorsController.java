package org.springframework.samples.app.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.samples.app.service.IMDoorsService;
import org.springframework.samples.app.vo.MDoors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/mdoors")
public class MDoorsController {
	@Resource
	IMDoorsService mDoorsService;
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Callable<String> getMDoors(@PathVariable("id") final String id,final HttpServletResponse response){		
		return new Callable<String>() {
			
			@Override
			public String call() throws Exception {
				MDoors mDoor = mDoorsService.getMDoorByPrimaryKey(id);
				response.setContentType("text/plain;charset=utf-8");
				String result = mDoor==null?"":JSON.toJSON(mDoor).toString();
				System.out.println(result);
				return result;
			}
		};		
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public @ResponseBody Callable<String> deleteMDoors(@PathVariable("id") final String id){
			return new Callable<String>() {
				@Override
				public String call() throws Exception {
					mDoorsService.delete(id);				
					return "sccuess";
				}
			};
	}
	
	@RequestMapping(value="/exception/{id}")
	public @ResponseBody Callable<String> exception(@PathVariable("id") final String id){
			return new Callable<String>() {
				@Override
				public String call() throws Exception {
					mDoorsService.exception(id);
					return "sccuess";
				}
			};
	}
	
	@ExceptionHandler
	public void handlerException(Exception e, HttpServletResponse response){
		response.setStatus(500);
		try {
			response.getOutputStream().write(e.getMessage().getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
