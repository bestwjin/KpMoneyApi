package com.wjlee.kpmoney.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@Table(name="tb_sprinkle_money")
public class SprinkleModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int seq;
	
	@Column(columnDefinition="CHAR(3)")
	private String token;
	
	private String userId;
	private String roomId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	private Date regDate;

	private Long sprinkleAmt;
	private int receiverCount;
}
