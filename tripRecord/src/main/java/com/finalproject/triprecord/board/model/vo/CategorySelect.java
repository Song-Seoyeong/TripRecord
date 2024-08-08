package com.finalproject.triprecord.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategorySelect {
	private String boardType; // GENERAL, QUESTION, NOTICE
	private String generalType; // WITH, GIVE, REVIEW        /       PAYMENT, PLANNER, ELSE
	private String localName;		// 서울, 부산, 대구 등등
	private String searchWord; // 검색어
}
