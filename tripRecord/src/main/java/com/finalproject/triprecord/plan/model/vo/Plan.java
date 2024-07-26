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
	private int scNo;
	private String spot;
	private String writer;
	private String startDate;
	private String endDate;
	private char status;
	private String together;
	private String hashtag;
}
