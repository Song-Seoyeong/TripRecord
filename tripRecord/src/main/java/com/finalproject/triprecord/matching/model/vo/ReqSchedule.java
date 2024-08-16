package com.finalproject.triprecord.matching.model.vo;

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
	
	private String nightDay;
	private String startDay;
	private String endDay;
	private int point;
	private int feedBackCount;
}
