package com.finalproject.triprecord.member.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {
	private int memberNo;
	private String memberId;
	private String memberPwd;
	private String memberName;
	private String nickname;
	private String phone;
	private String email;
	private Date createDate;
	private String grade;
	private String status;
	private int memberPoint;
	private String memberDrop;
	private String localName;
}
