package com.finalproject.triprecord.common.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Review {
	private int reviewNo;
	private int memberNo;
	private String nickname;
	private String imageRename; // 리뷰어의 프로필 사진 조회
	private String reviewTitle;
	private String reviewContent;
	private int reviewStar;
	private String revRefType;
	private int revRefNo;
	private String reviewStatus;
	private int reqRefNo;
}


//REVIEW_NO	NUMBER	PRIMARY KEY,
//MEMBER_NO	NUMBER		NOT NULL,
//REVIEW_TITLE	VARCHAR2(60)	NOT NULL,
//REVIEW_CONTENT	VARCHAR2(4000)	NOT NULL,
//REVIEW_STAR	NUMBER	NOT NULL,
//REV_REF_TYPE	VARCHAR2(10)	NOT NULL CHECK (REV_REF_TYPE IN ('PLANNER', 'PLACE')),
//REV_REF_NO