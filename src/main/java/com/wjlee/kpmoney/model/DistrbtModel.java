package com.wjlee.kpmoney.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonIgnore
	private int seq;
	
	@JsonIgnore
	private int sprinkleSeq;
	
	@JsonIgnore
	private String token;
	
	private Long rcvAmt = 0L;
	
	@JsonIgnore
	private String rcvRoomId;
	
	private String rcvUserId;
	
	@JsonIgnore
	private boolean receivedYn;
	
	public DistrbtModel(SprinkleModel sprinkleModel) {
		this.sprinkleSeq = sprinkleModel.getSeq();
		this.token = sprinkleModel.getToken();
		this.rcvRoomId = sprinkleModel.getRoomId(); 
	}
}
