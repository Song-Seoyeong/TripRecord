package com.finalproject.triprecord.common.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Review {
	private int replyNo;
	private int boardNo;
	private int reviewWriterNo;
	private String reviewontent;
	private Date reviewCreateDate;
	private Date reviewModifyDate;
	private String reviewyStatus;
}
