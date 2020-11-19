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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.wjlee.kpmoney.model.SprinkleModel;
import com.wjlee.kpmoney.service.SprinkleService;

@RestController
public class SprinkleController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SprinkleService sprinkleService;
	
	
	/**
	 * get sprinkle
	 * @param token
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping(value="/{token}")
	public ResponseEntity<?> getSprinkleMoneyInfo(
										@RequestHeader(value="X-USER-ID") String userId,
										@RequestHeader(value="X-ROOM-ID") String roomId,
										@PathVariable("token") String token) throws ParseException 
	{
		log.debug("Token ==> " + token);
		return sprinkleService.getSprinkleMoneyInfo(token, userId, roomId);	
	}
	
	/**
	 * create sprinkle
	 * @param sprinkleModel
	 * @return
	 */
	@PostMapping(value = "/sprinkle", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public ResponseEntity<?> createSprinkleMoney(
										@RequestHeader(value="X-USER-ID") String userId,
										@RequestHeader(value="X-ROOM-ID") String roomId,
										@RequestBody SprinkleModel sprinkleModel) 
	{
		
		log.debug("입력(금액, 받을사람수) ==> " + sprinkleModel.getSprinkleAmt() + ", " + sprinkleModel.getReceiverCount());
		sprinkleModel.setUserId(userId);
		sprinkleModel.setRoomId(roomId);
		return sprinkleService.createSprinkleMoney(sprinkleModel);
	}
}
