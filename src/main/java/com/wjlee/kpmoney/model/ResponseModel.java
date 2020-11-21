package com.wjlee.kpmoney.model;

import java.util.Map;

import lombok.Data;

@Data
public class ResponseModel {
	private int code;
	private String message;
	private Map<String, Object> data;
	
	public ResponseModel() { 
		this.code = 1000;
		this.message = "정상처리되었습니다."; 
	}
}
