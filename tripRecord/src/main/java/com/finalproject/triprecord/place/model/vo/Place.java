package com.finalproject.triprecord.place.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Place {
	private int placeNo;
	private int localNo;
	private int contentTypeId;
	private int placeStar;
	private int placeCount;
	private String placeName;
	private String imagePath;
}
