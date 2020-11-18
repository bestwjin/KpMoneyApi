package com.wjlee.kpmoney.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@Table(name="tb_sprinkle_money")
public class SprinkleModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)	
	private String token;
	
	private String userId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	private Date regDate;

	private Long sprinkleAmt;
	private int receiverCount;
}
