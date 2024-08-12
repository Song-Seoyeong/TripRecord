package com.finalproject.triprecord.common.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReqSchedule {
	private int reqNo;
	private int reqMemNo;
	private int reqPlaNo;
	private int scheNo;
	private String reqRef;
	private String reqStatus;
	private String memberName;
}
//REQ_NO	NUMBER
//REQ_MEM_NO	NUMBER
//REQ_PLA_NO	NUMBER
//SCHE_NO	NUMBER
//REQ_REF	VARCHAR2(200 BYTE)
//REQ_STATUS	CHAR(1 BYTE)