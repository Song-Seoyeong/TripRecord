package com.finalproject.triprecord.matching.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Planner {
	private int memberNo;
	private int localNo;
	private String introContent;
	private String plaReqContent;
	private String sIntroContent;
	
	private String Nickname; // 많이 쓰일 변수같은데.. vo?
}
