package com.wjlee.kpmoney.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjlee.kpmoney.model.SprinkleModel;
import com.wjlee.kpmoney.service.SprinkleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/sprinkle")
@RestController
public class SprinkleController {
	
	@Autowired
	private SprinkleService sprinkleService;
	
	/**
	 * 뿌리기 정보 조회
	 * get sprinkle
	 * @param token
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping(value="/{token}")
	public ResponseEntity<?> getSprinkleMoneyInfo(
										@RequestHeader(value="X-USER-ID", required = true) String userId,
										@RequestHeader(value="X-ROOM-ID", required = true) String roomId,
										@PathVariable("token") String token) throws ParseException 
	{
		log.debug("Token={}",token);
		return sprinkleService.getSprinkleMoneyInfo(token, userId, roomId);	
	}
	
	/**
	 * 뿌리기 생성
	 * create sprinkle
	 * @param sprinkleModel
	 * @return
	 */
	@PostMapping(value = "/create")
	public ResponseEntity<?> createSprinkleMoney(
										@RequestHeader(value="X-USER-ID", required = true) String userId,
										@RequestHeader(value="X-ROOM-ID", required = true) String roomId,
										@RequestBody SprinkleModel sprinkleModel) 
	{
		log.debug("'{}'이 뿌리기 요청 ", userId);
		log.debug("입력 금액={} 받을사람수={} ", sprinkleModel.getSprinkleAmt(), sprinkleModel.getReceiverCount());
		sprinkleModel.setUserId(userId);
		sprinkleModel.setRoomId(roomId);
		return sprinkleService.createSprinkleMoney(sprinkleModel);
	}
	
	/**
	 * 받기 aka 줍기
	 * @param userId
	 * @param roomId
	 * @param token
	 * @return
	 */
	@PutMapping(value="/pickup/{token}")
	public ResponseEntity<?> pickupSprinkledMoney(
										@RequestHeader(value="X-USER-ID", required = true) String userId,
										@RequestHeader(value="X-ROOM-ID", required = true) String roomId, 
										@PathVariable("token") String token) 
	{
		log.debug("'{}'이 '{}'토큰으로 받기 요청 ", userId, token);
		return sprinkleService.pickupSprinkledMoney(token, userId, roomId);
	}
}
