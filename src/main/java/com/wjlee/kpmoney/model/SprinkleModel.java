package com.wjlee.kpmoney.model;

import java.util.Date;

import javax.persistence.Column;
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

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name="tb_sprinkle")
public class SprinkleModel {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seq;
	
	@NotNull
	@Column(columnDefinition="CHAR(3)")
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	private Date regDate;
	
	private String userId;
	private String roomId;
	private Long sprinkleAmt;
	private int receiverCount;
}
