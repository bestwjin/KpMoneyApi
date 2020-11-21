package com.wjlee.kpmoney.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name="tb_distrbt")
public class DistrbtModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seq;
	
	private int sprinkleSeq;
	
	private String token;
	private Long rcvAmt;
	private String rcvRoomId;
	private String rcvUserId;
	private boolean receivedYn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	private Date regDate;
	
	
	public DistrbtModel(SprinkleModel sprinkleModel) {
		this.sprinkleSeq = sprinkleModel.getSeq();
		this.token = sprinkleModel.getToken();
		this.rcvRoomId = sprinkleModel.getRoomId(); 
	}
}
