package com.wjlee.kpmoney.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjlee.kpmoney.model.DistrbtModel;
import com.wjlee.kpmoney.model.SprinkleModel;
import com.wjlee.kpmoney.model.ResponseModel;
import com.wjlee.kpmoney.repository.DistrbtRepository;
import com.wjlee.kpmoney.repository.SprinkleRepository;
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
	
	public ResponseEntity<?> getSprinkleMoneyInfo(String token, String userId, String roomId) throws ParseException {		
		
		// 뿌린사람 자신의 것만 조회할 수 있으며, 그외(다른사람의 뿌리기건, 유효하지 않은 토큰의 경우 조회실패)
		// 조회는 7일이내에만 할 수 있음
		String searchDate = df.format(CommonUtil.addDate(new Date(), Calendar.DATE, -7));
		log.debug("뿌리기정보 조회 기준일자(7일전)={}", searchDate);
		SprinkleModel sprinkleModel = sprinkleRepository.findByTokenAndUserIdAndRegDateGreaterThanEqual(token, userId, df.parse(searchDate));		
				
		if(sprinkleModel == null) {
			return new ResponseEntity<>("데이터가 존재하지 않습니다.", HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(sprinkleModel);
		}
	}	
	
	/**
	 * 입력
	 * @param sprinkleModel
	 * @return
	 */
	@Transactional
	public ResponseEntity<?> createSprinkleMoney(SprinkleModel sprinkleModel) {
		// 토큰생성
		sprinkleModel.setToken(makeToken(sprinkleModel, ""));
		log.debug("생성된 토큰={}", sprinkleModel.getToken());
		SprinkleModel sprikleModel = sprinkleRepository.save(sprinkleModel);
		
		//익셉션으로 핸들링하는게 좋을듯. but 일단 기능구현부터.
		if(!"".equals(sprinkleModel.getToken())) {
			// 분배로직
			createDistrbtInfo(sprinkleModel);
		}		
		return ResponseEntity.ok().body(sprikleModel);
	}
	
	
	/**
	 * 받기
	 * @param token
	 * @param userId
	 * @param roomId
	 * @return
	 */
	public ResponseEntity<ResponseModel> pickupSprinkledMoney(String token, String userId, String roomId) {
		ResponseModel responseModel = new ResponseModel();
		
		// 받기 가능여부 체크
		
		
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
			distrbtRepository.save(distrbtModel);
				
		} else {
			responseModel.setCode(9999);
			responseModel.setMessage("받기 가능한 금액이 없습니다.");			
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
