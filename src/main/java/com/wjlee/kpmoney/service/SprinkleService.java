package com.wjlee.kpmoney.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjlee.kpmoney.model.DistrbtModel;
import com.wjlee.kpmoney.model.ResponseModel;
import com.wjlee.kpmoney.model.SprinkleModel;
import com.wjlee.kpmoney.repository.DistrbtRepository;
import com.wjlee.kpmoney.repository.SprinkleRepository;
import com.wjlee.kpmoney.util.CommonCode;
import com.wjlee.kpmoney.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SprinkleService {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private SprinkleRepository sprinkleRepository;
	
	@Autowired
	private DistrbtRepository distrbtRepository;
	
	/**
	 * 뿌리기건 조회
	 * @param token
	 * @param userId
	 * @param roomId
	 * @return
	 * @throws ParseException
	 * @throws JsonProcessingException
	 */
	public ResponseEntity<ResponseModel> getSprinkleMoneyInfo(String token, String userId, String roomId) throws ParseException, JsonProcessingException {
		ResponseModel responseModel = new ResponseModel();
		ObjectMapper mapper = new ObjectMapper();
		
		// 조회가능 기준일자 -7일		
		String searchDate = df.format(CommonUtil.addDate(new Date(), Calendar.DATE, -7));
		log.debug("뿌리기정보 조회 기준일자(7일전)={}", searchDate);
		
		// 조회조건 -> 뿌린사람자신만, 뿌린지 7일이내건만 조회가능
		SprinkleModel sprinkleModel = sprinkleRepository.findByTokenAndUserIdAndRegDateGreaterThanEqual(token, userId, df.parse(searchDate)).orElse(null);
		
		if(sprinkleModel == null) {
			responseModel.setCode("E000");
			responseModel.setMessage(CommonCode.E000);
			return ResponseEntity.ok().body(responseModel);
		} else {
			// 조회가능건이 있으면 정보를 json형태로 전송.
			responseModel.setData(mapper.writeValueAsString(sprinkleModel));
		}
		
		return ResponseEntity.ok().body(responseModel);
	}	
	
	/**
	 * 입력(뿌리기)
	 * @param sprinkleModel
	 * @return
	 */
	@Transactional
	public ResponseEntity<?> createSprinkleMoney(String userId, String roomId, long sprinkleAmt, int receiverCount) {
		ResponseModel responseModel = new ResponseModel();
		SprinkleModel sprinkleModel = new SprinkleModel(userId, roomId, sprinkleAmt, receiverCount);
		
		/**************************************************
		 * 뿌리기 입력값 검증
		 **************************************************/
		// 뿌리기 금액 검증(1원이상이어야 함)
		if(sprinkleAmt < 1) {
			responseModel.setCode("E006");
			responseModel.setMessage(CommonCode.E006);
			return ResponseEntity.ok().body(responseModel);
		}
		// 뿌릴인원 수 검증
		if(receiverCount < 1) {
			responseModel.setCode("E007");
			responseModel.setMessage(CommonCode.E007);
			return ResponseEntity.ok().body(responseModel);
		}
		// 각 인원은 1원이상을 받아가야함.
		if(sprinkleAmt/receiverCount < 1) {
			responseModel.setCode("E008");
			responseModel.setMessage(CommonCode.E008);
			return ResponseEntity.ok().body(responseModel);
		}
		
		// 토큰생성
		sprinkleModel.setToken(makeToken(sprinkleModel, ""));
		log.debug("생성된 토큰={}", sprinkleModel.getToken());
		String token = sprinkleRepository.save(sprinkleModel).getToken();

		// 금액분배
		if(!"".equals(sprinkleModel.getToken())) createDistrbtInfo(sprinkleModel);
		
		responseModel.setData(token);
		return ResponseEntity.ok().body(responseModel);
	}
	
	
	/**
	 * 받기 aka 줍기
	 * @param token
	 * @param userId
	 * @param roomId
	 * @return
	 */
	public ResponseEntity<ResponseModel> pickupSprinkledMoney(String token, String userId, String roomId) {
		ResponseModel responseModel = new ResponseModel();
		/**************************************************
		 * 받기 가능여부 검증
		 **************************************************/
		SprinkleModel sprinkleModel = sprinkleRepository.findByToken(token).orElse(null);
		
		// 토큰에 해당하는 뿌리기건이 존재하지 않음
		if(sprinkleModel == null) {
			responseModel.setCode("E001");
			responseModel.setMessage(CommonCode.E001);		
			return ResponseEntity.ok().body(responseModel);
		}
		
		// 자신이 뿌리기 한 건은 자신이 받을 수 없음.
		if(sprinkleModel.getUserId().equals(userId)) {
			responseModel.setCode("E002");
			responseModel.setMessage(CommonCode.E002);
			return ResponseEntity.ok().body(responseModel);
		}
		
		// 뿌린지 10분이 지난 요청은 받기 실패
		int compareDate = sprinkleModel.getRegDate().compareTo(CommonUtil.addDate(new Date(), Calendar.MINUTE, -10));
		if(compareDate < 0) {
			responseModel.setCode("E003");
			responseModel.setMessage(CommonCode.E003);
			return ResponseEntity.ok().body(responseModel);
		}
		
		// 뿌리기 호출 대화방과 동일 대화방인유저인지 확인
		if(!sprinkleModel.getRoomId().equals(roomId)) {
			responseModel.setCode("E004");
			responseModel.setMessage(CommonCode.E004);
			return ResponseEntity.ok().body(responseModel);
		}
		
		// 먼저 받은 건이 없는지 확인(뿌리기당 한번만 받을 수 있음)
		if(distrbtRepository.findByTokenAndRcvUserIdAndReceivedYnTrue(token, userId).orElse(null) != null) {
			responseModel.setCode("E005");
			responseModel.setMessage(CommonCode.E005);
			return ResponseEntity.ok().body(responseModel);
		}
		
		// 토큰에 해당하는 '받기가능한'건을 가져옴.
		List<DistrbtModel> distrbtList = distrbtRepository.findByTokenAndReceivedYnFalse(token).orElse(null);
		DistrbtModel distrbtModel = new DistrbtModel();
		
		if(distrbtList != null) {
			// 무작위로 받음
			distrbtModel = distrbtList.get((int) (Math.random()*distrbtList.size())); 
			log.debug("할당된금액={}", distrbtModel.getRcvAmt());
			
			distrbtModel.setRcvUserId(userId);
			distrbtModel.setToken(token);
			distrbtModel.setReceivedYn(true);
			String rcvAmt = String.valueOf(distrbtRepository.save(distrbtModel).getRcvAmt());
			responseModel.setData(rcvAmt);
			
			// 뿌리기테이블에 받은금액을 더해줌.
			sprinkleModel.setReceivedAmt(sprinkleModel.getReceivedAmt()+Long.parseLong(rcvAmt));
			sprinkleRepository.save(sprinkleModel);
			
		} else {
			responseModel.setCode("E001");
			responseModel.setMessage(CommonCode.E001);
			return ResponseEntity.ok().body(responseModel);
		}
		
		return ResponseEntity.ok(responseModel);
	}
	
	/**
	 * 토큰생성 (대소문자 구별 알파벳)
	 * @return
	 */
	public String makeToken(SprinkleModel sprinkleModel, String recurToken) {
		
		if(!"".equals(recurToken) && sprinkleRepository.findByTokenAndRoomId(recurToken, sprinkleModel.getRoomId()).orElse(null) == null) 
			return recurToken; 
		
		StringBuilder sb = new StringBuilder();

		// 대소문자를 구별하여 가능범위 확장
		for(int i=0; i<3; i++) {
			int swicher = (int) (Math.random()*2);
			if(swicher==0) 
				sb.append((char) (Math.random()*26+65));			
			else 
				sb.append((char) (Math.random()*26+97));
		}
		
		// 위 상단 처음의 조건에 의하여 동일한 룸ID에 발급된 동일한 토큰이 있을경우 재생성함(recursive)
		return makeToken(sprinkleModel, sb.toString());
	}
	
	/**
	 * 뿌리기 분배
	 * @param sprinkleModel
	 */
	public void createDistrbtInfo(SprinkleModel sprinkleModel) {
		int rcvCount = sprinkleModel.getReceiverCount();
		long sprinkleAmt = sprinkleModel.getSprinkleAmt();
		
		List<DistrbtModel> distrbtList = new ArrayList<DistrbtModel>();
		
		// 가능 잔액범위내에서 랜덤금액을 생성하여 분배한다.
		while(rcvCount > 0) {
			DistrbtModel distrbtModel = new DistrbtModel(sprinkleModel);
			distrbtModel.setRcvAmt((long)(Math.random()*sprinkleAmt)+1);
			sprinkleAmt -= distrbtModel.getRcvAmt();
			
			// 마지막건은 남은 잔액합산
			if(rcvCount==1) distrbtModel.setRcvAmt(distrbtModel.getRcvAmt() + sprinkleAmt);

			distrbtList.add(distrbtModel);			
			rcvCount--;
		}
		distrbtRepository.saveAll(distrbtList);
	}
}
