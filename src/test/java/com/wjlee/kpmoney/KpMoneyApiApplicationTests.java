package com.wjlee.kpmoney;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjlee.kpmoney.service.SprinkleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class KpMoneyApiApplicationTests {
	
	String[] userIdList = new String[] {"123456", "1234567", "1234568", "1234569", "1234570", "1234571", "1234572"};
	String[] roomIdList = new String[] {"roomA", "roomB", "roomC", "roomD", "roomE", "roomF", "roomG"};
	String token = "";
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
    private WebApplicationContext ctx;
	
	@Autowired
	private SprinkleService sprinkleService;
	
	@Before
    public void setup() throws Exception {
		// 한글깨짐방지 인코딩설정
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }
	
	@Test
	@Order(1)
	@DisplayName("뿌리기 - 정상입력")
	public void setSpringkleInfo() throws Exception {
		MvcResult result= mvc.perform(post("/sprinkle/create/120000/5")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))        		
                .andExpect(status().isOk())
                .andReturn();
		
		// 세자리수 토큰이 정상적으로 리턴되는가 -> 토큰이 정상적으로 생성되었다면 3자리 알파벳
		String content = result.getResponse().getContentAsString();
        final ObjectNode node = new ObjectMapper().readValue(content, ObjectNode.class);
        
        String get_token = node.get("data").toString().replaceAll("\"", "");        
        log.debug("받은 토큰 : " + get_token);
        assertEquals(get_token.length(), 3);              
	}
	
	@Test
	@Order(2)
	@DisplayName("뿌리기 - 금액 0원입력시")
	public void setSpringkleInfoWithZeroAmt() throws Exception {
		MvcResult result= mvc.perform(post("/sprinkle/create/0/5")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))        		
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("E006"))) // 뿌리기 금액은 1원이상 입력해야함.
                .andReturn();        		
	}
	
	@Test
	@Order(3)
	@DisplayName("뿌리기 - 인원 0명 입력시")
	public void setSpringkleInfoWithZeroReceiver() throws Exception {
		MvcResult result= mvc.perform(post("/sprinkle/create/120000/0")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))        		
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("E007"))) // 뿌리기 금액은 1원이상 입력해야함.
                .andReturn();        		
	}
	
	@Test
	@Order(4)
	@DisplayName("뿌리기 - 1명당 1원이상을 받아갈수 있어야함.")
	public void cantSrinkleDeviceUnder1() throws Exception {
		// 1원을 5명으로 나누면 1원이상 받아갈수 없으니 에러리턴.
		MvcResult result= mvc.perform(post("/sprinkle/create/1/5")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))        		
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("E008"))) 
                .andReturn();        		
	}
	
	@Test
	@Order(5)
	@DisplayName("받기 - 정상 받기 후 금액리턴.")
	public void isReceiveAvailable() throws Exception {
		MockHttpServletRequestBuilder builder =
	              MockMvcRequestBuilders.put("/sprinkle/pickup/" + this.token)
	                                    .characterEncoding("UTF-8")
	                                    .header("X-USER-ID", userIdList[1])			
	                                    .header("X-ROOM-ID", roomIdList[0]);

		MvcResult result= mvc.perform(builder)        		
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("S000")))
                .andExpect(jsonPath("$.data").value(IsNull.notNullValue()))
                .andReturn();        		
	}
	
	@Test
	@Order(6)
	@DisplayName("받기 - 본인이 뿌리기한 건은 받을 수 없음.")
	public void isReceiveAvailableOneSelf() throws Exception {
		MockHttpServletRequestBuilder builder =
	              MockMvcRequestBuilders.put("/sprinkle/pickup/" + this.token)
	                                    .characterEncoding("UTF-8")
	                                    .header("X-USER-ID", userIdList[0])			
	                                    .header("X-ROOM-ID", roomIdList[0]);

		MvcResult result= mvc.perform(builder)        		
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("E002")))
                .andExpect(jsonPath("$.data", is("")))
                .andReturn();        		
	}
	
	@Test
	@Order(7)
	@DisplayName("조회 - 정상조회")
	public void getSprinkleInfo() throws Exception {
		MockHttpServletRequestBuilder builder =
	              MockMvcRequestBuilders.get("/sprinkle/" + this.token)
	                                    .characterEncoding("UTF-8")
	                                    .header("X-USER-ID", userIdList[0])			
	                                    .header("X-ROOM-ID", roomIdList[0]);

		MvcResult result= mvc.perform(builder)        		
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("S000")))                
                .andReturn();        		
	}
	
	@Test
	@Order(7)
	@DisplayName("조회 - 뿌린사람 자신만 조회가능")
	public void getSprinkleInfoByOneSelf() throws Exception {
		MockHttpServletRequestBuilder builder =
	              MockMvcRequestBuilders.get("/sprinkle/" + this.token)
	                                    .characterEncoding("UTF-8")
	                                    .header("X-USER-ID", userIdList[1])			
	                                    .header("X-ROOM-ID", roomIdList[0]);

		MvcResult result= mvc.perform(builder)        		
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("E000")))                
                .andReturn();        		
	}
	
	// 뿌리기 토큰 생성
	@BeforeEach
	public void createSprinkleTokens() throws Exception {
		MvcResult result= mvc.perform(post("/sprinkle/create/120000/5")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userIdList[0])		
                .header("X-ROOM-ID", roomIdList[0]))        		
                .andExpect(status().isOk())
                .andReturn();
		
		String content = result.getResponse().getContentAsString();
        final ObjectNode node = new ObjectMapper().readValue(content, ObjectNode.class);
        this.token = node.get("data").toString().replaceAll("\"", "");        
	}
}

