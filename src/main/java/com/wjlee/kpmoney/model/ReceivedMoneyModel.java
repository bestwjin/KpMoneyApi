package com.wjlee.kpmoney.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_rcv_money")
public class ReceivedMoneyModel {
	
	@Id
	private String token;
	private Long rcvAmt;
	private String rcvUserId;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
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
