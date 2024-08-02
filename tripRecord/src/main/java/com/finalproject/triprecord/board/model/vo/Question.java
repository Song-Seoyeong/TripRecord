package com.finalproject.triprecord.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Question {
	private int questionNo;
	private int questionPwd;
	private String questionAnswer;
	private String questionSuccess;
}
