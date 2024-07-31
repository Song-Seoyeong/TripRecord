package com.finalproject.triprecord.board.model.vo;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
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
