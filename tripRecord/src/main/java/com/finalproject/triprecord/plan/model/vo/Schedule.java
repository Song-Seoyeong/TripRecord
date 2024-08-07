package com.finalproject.triprecord.plan.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Schedule {
	private int scNo;
	private int scLocalNo;
	private String spot;
	private int writerNo;
	private String writer;
	private String startDate;
	private String endDate;
	private String delStatus;
	
	private String together;
	private String hashtag;
}
