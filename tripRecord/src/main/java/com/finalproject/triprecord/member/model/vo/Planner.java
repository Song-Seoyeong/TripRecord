package com.finalproject.triprecord.member.model.vo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Planner {
	private int memberNo;
	private int localNo;
	private String introContent;
	private String plaReqContent;
	private String sIntroContent;
	
	private String Nickname;
	private String imageRename;
}
