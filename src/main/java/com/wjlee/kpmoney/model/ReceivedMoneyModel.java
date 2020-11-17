package com.wjlee.kpmoney.model;

import javax.persistence.Entity;

@Entity
public class ReceivedMoneyModel {
	private Long rcvAmt;
	private String rcvUserId;
	public Long getRcvAmt() {
		return rcvAmt;
	}
	public void setRcvAmt(Long rcvAmt) {
		this.rcvAmt = rcvAmt;
	}
	public String getRcvUserId() {
		return rcvUserId;
	}
	public void setRcvUserId(String rcvUserId) {
		this.rcvUserId = rcvUserId;
	}
}
