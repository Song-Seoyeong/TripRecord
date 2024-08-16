package com.finalproject.triprecord.board.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Question {
	private int boardNo;
	private int boardWriterNo;
	private String boardTitle;
	private String boardContent;
	private Date boardCreateDate;
	private String boardStatus;
	
	private String questionAnswer;
	private String questionSuccess;
	private int questionNo;
	private int questionPwd;
	private String generalType;
	
	private String memberName;
}
