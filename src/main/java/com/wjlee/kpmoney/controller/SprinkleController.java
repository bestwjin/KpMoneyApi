package com.wjlee.kpmoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjlee.kpmoney.model.SprinkleModel;

@RestController
public class SprinkleController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());	
	
	/**
	 * Get Sprinkle Money Information 
	 * @return
	 */
	@GetMapping
	public SprinkleModel getSprinkleMoneyInfo() {
		SprinkleModel sprinkleModel = new SprinkleModel();
		return sprinkleModel;
	}
	
	@PostMapping
	public SprinkleModel createSprinkleMoney() {
		SprinkleModel sprinkleModel = new SprinkleModel();
		
		log.debug("post로 왓어염");		
		return sprinkleModel;
	}
}
