package com.wjlee.kpmoney;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class KpMoneyApiApplicationTests {
	
	String[] userIdList = new String[] {"123456"};
	String[] roomIdList = new String[] {"roomA"};
	String token = "rbd";
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	@Order(1)
	@DisplayName("뿌리기 생성 테스트 - 정상입력")
	public void setSpringkleInfo() throws Exception {
		MvcResult result= mvc.perform(post("/sprinkle/create")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0])
                .content("{\"sprinkleAmt\":120000, \"receiverCount\":5}"))        		
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(json("token".length(), is(3)))
                .andReturn();
		//ResultMatcher
        
        String content = result.getResponse().getContentAsString();
        final ObjectNode node = new ObjectMapper().readValue(content, ObjectNode.class);
        
        assertEquals(node.get("token").toString().replaceAll("\"", "").length(), 3);
	}
}

