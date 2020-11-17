package com.wjlee.kpmoney.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_sprinkle_money")
public class SprinkleModel {
	
	@Id
	private String token;
	private String userId;	
	private Date regDate;
	private Long sprinkleAmt;
	
	@OneToMany
	@JoinTable(name="", joinColumns=@JoinColumn(name="token"))			
	private List<ReceivedMoneyModel> receivedMoneyList;
	
	public String getToken() {
		return token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<ReceivedMoneyModel> getReceivedMoneyList() {
		return receivedMoneyList;
	}

	public void setReceivedMoneyList(List<ReceivedMoneyModel> receivedMoneyList) {
		this.receivedMoneyList = receivedMoneyList;
	}
	
	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Long getSprinkleAmt() {
		return sprinkleAmt;
	}

	public void setSprinkleAmt(Long sprinkleAmt) {
		this.sprinkleAmt = sprinkleAmt;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
