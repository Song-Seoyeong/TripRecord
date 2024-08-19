package com.finalproject.triprecord.common.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cancel {
	private int cancelNo;
	private int cancelMemNo;
	private String cancelComent;
	private Date cancelDate;
	private String cancelRefType;
	private int cancelRefNo;
	private String memberName;
	

//CREATE TABLE CANCEL(
//    CANCEL_NO number primary key,
//    CANCEL_MEM_NO NUMBER NOT NULL,
//    CANCEL_COMENT VARCHAR2(3000) NOT NULL,
//    CANCEL_DATE DATE DEFAULT SYSDATE,
//    CANCEL_REF_TYPE VARCHAR2(30) NOT NULL,
//    CANCEL_REF_NO NUMBER NOT NULL
//)
}
