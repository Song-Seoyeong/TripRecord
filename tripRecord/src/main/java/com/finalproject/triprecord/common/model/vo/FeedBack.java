package com.finalproject.triprecord.common.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FeedBack {
	private int feedNo;
	private int reqNo;
	private String feedContent;
	private String feedStatus;
	
//	FEED_NO	NUMBER
//	REQ_NO	NUMBER
//	FEED_CONTENT	VARCHAR2(1000 BYTE)
//	FEED_STATUS	CHAR(1 BYTE)
}
