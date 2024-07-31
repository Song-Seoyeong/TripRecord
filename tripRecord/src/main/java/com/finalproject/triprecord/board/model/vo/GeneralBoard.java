package com.finalproject.triprecord.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GeneralBoard {
	private int generalNo;
	private int localNo;
	private String generalType;
}
