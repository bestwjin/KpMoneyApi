package com.wjlee.kpmoney;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
	
	@Autowired
    private WebApplicationContext ctx;
	
	@Before
    public void setup() {
		// 한글깨짐방지 인코딩설정
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }
	
	@Test
	@Order(1)
	@DisplayName("뿌리기 생성 테스트 - 정상입력")
	public void setSpringkleInfo() throws Exception {
		MvcResult result= mvc.perform(post("/sprinkle/create/120000/5")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))        		
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        
		String content = result.getResponse().getContentAsString();
        final ObjectNode node = new ObjectMapper().readValue(content, ObjectNode.class);
        
        // 세자리수 토큰이 정상적으로 리턴되는가.
        assertEquals(node.get("data").toString().replaceAll("\"", "").length(), 3);
	}
	
	@Test
	@Order(2)
	@DisplayName("뿌리기 생성 테스트 - 금액 0원입력시")
	public void setSpringkleInfoWithZeroAmt() throws Exception {
		MvcResult result= mvc.perform(post("/sprinkle/create/0/5")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))        		
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        
		String content = result.getResponse().getContentAsString();
        final ObjectNode node = new ObjectMapper().readValue(content, ObjectNode.class);
        
        // 세자리수 토큰이 정상적으로 리턴되는가.
        assertEquals(node.get("data").toString().replaceAll("\"", "").length(), 3);
	}
}

