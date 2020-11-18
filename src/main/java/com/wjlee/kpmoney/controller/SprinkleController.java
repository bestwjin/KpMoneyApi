package com.wjlee.kpmoney.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wjlee.kpmoney.model.SprinkleModel;
import com.wjlee.kpmoney.service.SprinkleService;

@RestController
public class SprinkleController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SprinkleService sprinkleService;
	
	
	/**
	 * 뿌리기 정보 조회
	 * @param token
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping(value="/{token}")
	public ResponseEntity<?> getSprinkleMoneyInfo(@PathVariable("token") String token) throws ParseException {
		log.debug("Token ==> " + token);
		// x-user-id 부분을 구현해야함.
		return sprinkleService.getSprinkleMoneyInfo(token, "123456");		
	}
	
	/**
	 * 뿌리기 입력(json타입으로 제한)
	 * @param sprinkleModel
	 * @return
	 */
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public ResponseEntity<?> createSprinkleMoney(@RequestBody SprinkleModel sprinkleModel) {
		log.debug("뿌리기 입력(금액, 받을사람수) ==> " + sprinkleModel.getSprinkleAmt() + ", " + sprinkleModel.getReceiverCount());
		
		// 토큰생성 구현해야함.
		sprinkleModel.setToken("DFF");
		sprinkleModel.setUserId("123456");
		return sprinkleService.createSprinkleMoney(sprinkleModel);
	}
}
