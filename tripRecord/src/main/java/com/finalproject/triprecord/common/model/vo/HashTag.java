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
public class HashTag {
	private int tagNo;
	private String tagName;
	private String tagType;
	private int count;
	private int rownum;
}
