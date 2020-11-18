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
	
	public ResponseEntity<?> getSprinkleMoneyInfo(String token, String userId) throws ParseException {
		
		// x-user-id 구현 및 where 조건 추가 필요.
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
	
	public ResponseEntity<?> createSprinkleMoney(SprinkleModel sprinkleModel) {
		SprinkleModel sprikleModel = sprinkleRepository.save(sprinkleModel); 
		return ResponseEntity.ok().body(sprikleModel);				
	}
}
