package com.finalproject.triprecord.common.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Image {
	private int imageNo;
	private String imagePath;
	private String imageOriginName;
	private String imageRename;
	private int imageThum;
	private String imageRefType;
	private int imageRefNo;
	private String localName;	
	
}



//COMMENT ON COLUMN IMAGE.IMAGE_NO IS '사진 번호';
//COMMENT ON COLUMN IMAGE.IMAGE_PATH IS '저장경로';
//COMMENT ON COLUMN IMAGE.IMAGE_ORIGIN_NAME IS '사진 이름';
//COMMENT ON COLUMN IMAGE.IMAGE_RENAME IS '사진rename';
//COMMENT ON COLUMN IMAGE.IMAGE_THUM IS '썸네일 여부';
//COMMENT ON COLUMN IMAGE.IMAGE_REF_TYPE IS '사진 참조 타입';
//COMMENT ON COLUMN IMAGE.IMAGE_REF_NO IS '참조 번호 : 회원/게시글/추천장소 PK';