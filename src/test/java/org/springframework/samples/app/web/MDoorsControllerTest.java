package org.springframework.samples.app.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.app.service.IMDoorsService;
import org.springframework.samples.app.service.IUserService;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
public class MDoorsControllerTest extends AbstractContextControllerTests{

	private MockMvc mockMvc;
	@Resource
	IMDoorsService mDoorsService;
	@Resource
	IUserService userService;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testInsert(){
		userService.insertUser();
//		mDoorsService.insertBook();
	}
	
//	@Test
	public void testGetMDoors() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/mdoors/1000"))
				.andExpect(request().asyncStarted())				
				.andReturn();

			this.mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk());
			
	}
	
//	@Test	
	public void testDeleteMDoors() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(delete("/mdoors/100027"))
				.andExpect(request().asyncStarted())	
				.andExpect(request().asyncResult("java.lang.Exception: denied"))
				.andReturn();

			this.mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().string("java.lang.Exception: denied"));
			
	}
	
//	@Test
	@Transactional
	public void testException() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(delete("/mdoors/exception/100026"))
				.andExpect(request().asyncStarted())	
				.andExpect(request().asyncResult("denied"))
				.andReturn();

			this.mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().string("denied"));
			
	}
	
	
	
	

}
