package com.finalproject.triprecord.admin.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestGrade {
	private int gradeNo;
	private int memberNo;
	private int localNo;
	private String grade;
	private String plaReqContent;
	private String sIntroContent;
	private Date requestDate;
	
	private String memberName;
	private String localName;
}
