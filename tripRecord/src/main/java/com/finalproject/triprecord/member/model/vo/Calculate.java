package com.finalproject.triprecord.member.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Calculate {
	private int calculateNo;
	private int plannerNo;
	private int reqNo;
	private String calComent;
	private Date confirmDate;
	private Date calDate;
	private int expectedPoint;
	private int expectedMoney;
	private String memberName;
	
//	CREATE TABLE CALCULATE(
//		    CALCULATE_NO NUMBER PRIMARY KEY,
//		    PLANNER_NO NUMBER NOT NULL,
//		    REQ_NO NUMBER NOT NULL,
//		    CAL_COMENT VARCHAR2(2000),
//		    CONFIRM_DATE DATE DEFAULT SYSDATE,
//		    CAL_DATE DATE,
//		    EXPECTED_POINT NUMBER NOT NULL,
//		    EXPECTED_MONEY NUMBER NOT NULL
//		);
}
