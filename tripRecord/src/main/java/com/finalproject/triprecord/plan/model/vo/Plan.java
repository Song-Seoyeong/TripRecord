package com.finalproject.triprecord.plan.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Plan {
	private int plNo;
	private int scNo;
	private String place;
	private String time;
	private String memo;
	private String reserve;
	private String day;
	
	private String plannerMemo;
}
