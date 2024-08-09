package com.finalproject.triprecord.common.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Payment {
	private int payNo;
	private int memberNo;
	private int pointNo;
	private Date payDate;
	private String impUid;
	private String merchantUid;
	
	private String memberName;
	private int poPoint;
	private int poPrice;
	private int rownum;
}
