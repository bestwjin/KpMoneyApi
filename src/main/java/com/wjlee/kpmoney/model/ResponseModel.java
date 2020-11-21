package com.wjlee.kpmoney.model;

import com.wjlee.kpmoney.util.CommonCode;

import lombok.Data;

@Data
public class ResponseModel {
	private String code;
	private String message;
	private String data;
	
	// 기본 결과값 셋팅
	public ResponseModel() {
		this.code = "S000";
		this.message = CommonCode.S000;
		this.data = "";
	}
}
