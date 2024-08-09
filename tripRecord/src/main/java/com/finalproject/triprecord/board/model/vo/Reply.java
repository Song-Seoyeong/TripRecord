package com.finalproject.triprecord.board.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Reply {
	private int replyNo;
	private int boardNo;
	private int replyWriterNo;
	private String nickname; 
	private String replyContent;
	private Date replyCreateDate;
	private Date replyModifyDate;
	private String replyStatus;
}
