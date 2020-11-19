package com.wjlee.kpmoney;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(KpMoneyApiApplicationTests.class)
class KpMoneyApiApplicationTests {
	
	String[] userIdList = new String[] {"123456"};
	String[] roomIdList = new String[] {"roomA"};
	String token = "RBD";
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	@DisplayName("create sprinkle info")
	public void setSpringkleInfo() throws Exception {
		
		//when
        final ResultActions actions = mvc.perform(post("/sprinkle")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))
                .andDo(print())
                .andExpect(status().isOk());        
	}

	@Test
	@DisplayName("get sprinkle info")
	public void getSpringkleInfo() throws Exception {
		//when
        final ResultActions actions = mvc.perform(get("/"+token, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])
                .header("X-ROOM-ID", roomIdList[0]))
                .andDo(print())
                .andExpect(status().isOk());        
	}
}
