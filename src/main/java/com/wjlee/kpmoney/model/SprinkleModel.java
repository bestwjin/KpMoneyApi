package com.wjlee.kpmoney.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name="tb_sprinkle")
public class SprinkleModel {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int seq;
	
	@NotNull
	@JsonIgnore
	@Column(columnDefinition="CHAR(3)")
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	private Date regDate;

	@JsonIgnore
	private String userId;
	
	@JsonIgnore
	private String roomId;
	
	private Long sprinkleAmt = 0L;
	private Long receivedAmt = 0L;	
	
	@JsonIgnore
	private int receiverCount;
	
	// 받기정보
	@OneToMany
	@JoinColumn(name="sprinkleSeq")
	@Where(clause = "received_yn = true")
	private List<DistrbtModel> distrbtList = new ArrayList<>();
	
	public SprinkleModel(String userId, String roomId, long sprinkleAmt, int receiverCount) {
		this.userId = userId;
		this.roomId = roomId;
		this.sprinkleAmt = sprinkleAmt;
		this.receiverCount = receiverCount;
	}
}
