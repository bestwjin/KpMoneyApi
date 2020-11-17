package com.wjlee.kpmoney;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(KpMoneyApiApplicationTests.class)
class KpMoneyApiApplicationTests {
	
	@Autowired
	MockMvc mvc;

	@Test
	void contextLoads() {
	}
	
	@Test
	public void getSpringkleInfo() throws Exception {
		//when
        final ResultActions actions = mvc.perform(get("/", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
	}
}
