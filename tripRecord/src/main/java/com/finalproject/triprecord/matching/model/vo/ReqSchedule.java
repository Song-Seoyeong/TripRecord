package com.finalproject.triprecord.matching.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReqSchedule {
	private int reqNo;
	private int reqMemNo;
	private int reqPlaNo;
	private int scheNo;
	private String reqRef;
	private int reqStatus;
	private int payPoint;
	private Date confirmDate;
	private String nickname;
	private String memo;
	
	private String nightDay;
	private String startDay;
	private String endDay;
	private int feedBackCount;
	private String localName;
	private String memberName;
	private String plannerName;	
}
