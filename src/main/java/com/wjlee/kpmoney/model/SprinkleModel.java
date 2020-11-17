package com.wjlee.kpmoney.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
public class SprinkleModel {
	
	private String userId;
	private String token;
	private Date sprinkleDate;
	private Long sprinkleAmt;
	
	@OneToMany
	@JoinTable(name="", joinColumns=@JoinColumn(name="token"),
						inverseJoinColumns = @JoinColumn(name="token"))			
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

	public Date getSprinkleDate() {
		return sprinkleDate;
	}

	public void setSprinkleDate(Date sprinkleDate) {
		this.sprinkleDate = sprinkleDate;
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
