package com.wjlee.kpmoney;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@WebMvcTest(KpMoneyApiApplicationTests.class)
class KpMoneyApiApplicationTests {
	
	@Autowired
	MockMvc mock;

	@Test
	void contextLoads() {
		System.out.println('무');
	}
	
	
}
