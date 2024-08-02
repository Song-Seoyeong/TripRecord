package com.finalproject.triprecord.board.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Board {
	private int boardNo;
	private String boardType;		// GENERAL, QUESTION, NOTICE
	private int boardWriterNo;
	private String nickname;
	private String boardTitle;
	private String boardContent;
	private Date boardCreateDate;
	private Date boardModifyDate;
	private String boardStatus;
	private String generalType;		// WITH, GIVE, REVIEW
	private String localName;		// 서울, 부산, 대구 등등
	private int boardCount;
	
	private String memberName;
}
