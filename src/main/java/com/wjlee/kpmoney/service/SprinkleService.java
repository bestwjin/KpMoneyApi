package com.wjlee.kpmoney.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wjlee.kpmoney.model.SprinkleModel;
import com.wjlee.kpmoney.repository.SprinkleRepository;
import com.wjlee.kpmoney.util.CommonUtil;

@Service
public class SprinkleService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private SprinkleRepository sprinkleRepository;
	
	public ResponseEntity<?> getSprinkleMoneyInfo(String token, String userId, String roomId) throws ParseException {		
		
		// 뿌린사람 자신의 것만 조회할 수 있으며, 그외(다른사람의 뿌리기건, 유효하지 않은 토큰의 경우 조회실패)
		// 조회는 7일이내에만 할 수 있음
		String searchDate = df.format(CommonUtil.addDate(new Date(), Calendar.DATE, -7));
		log.debug("뿌리기정보 조회 기준일자(7일전) ==> " + searchDate);
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
	public ResponseEntity<?> createSprinkleMoney(SprinkleModel sprinkleModel) {
		// 토큰생성
		sprinkleModel.setToken(makeToken(sprinkleModel, ""));
		log.debug("생성된 토큰 ==> : " + sprinkleModel.getToken());
		
		SprinkleModel sprikleModel = sprinkleRepository.save(sprinkleModel); 
		return ResponseEntity.ok().body(sprikleModel);				
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
}
