package com.finalproject.triprecord.common.model.vo;

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
public class Point {
	private int poNo;
	private Integer poPoint;
	private Integer poPrice;
	private String poActive;
	private int oldPoint;
}
