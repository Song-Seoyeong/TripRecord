package com.finalproject.triprecord.place.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaceController {
	
	@GetMapping("recoPlace.pla")
	public String recoPlacelist() {
		return "recommendPlace";
	}
	
	
	
	@GetMapping("placeDetail.pla")
	public String placeDetail() {
		return "placeDetail";
	}
	
	@GetMapping("placeReviewWrite.pla")
	public String placeReviewWrite() {
		return "placeReviewWrite";
	}
	
	@GetMapping("placeReviewDetail.pla")
	public String placeReviewDetail() {
		return "placeReviewDetail";
	}
}
