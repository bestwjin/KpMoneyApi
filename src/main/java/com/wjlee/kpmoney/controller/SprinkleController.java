package com.wjlee.kpmoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjlee.kpmoney.model.SprinkleModel;
import com.wjlee.kpmoney.service.SprinkleService;

@RestController
public class SprinkleController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SprinkleService SprinkleService;  
	
	/**
	 * Get Sprinkle Money Information 
	 * @return
	 */
	@GetMapping
	public SprinkleModel getSprinkleMoneyInfo() {
		SprinkleModel sprinkleModel = new SprinkleModel();
		System.out.println("get is here");
		return sprinkleModel;
	}
	
	@PostMapping(value="/{sprinkleAmt}/{receiverCount}")
	public SprinkleModel createSprinkleMoney(@PathVariable("sprinkleAmt") long sprinkleAmt, @PathVariable("receiverCount") int receiverCount) {
		SprinkleModel sprinkleModel = new SprinkleModel();
		// sprinkleModel = SprinkleService.createSprinkleMoney(sprinkleAmt, receiverCount);		
		return sprinkleModel;
	}
}
