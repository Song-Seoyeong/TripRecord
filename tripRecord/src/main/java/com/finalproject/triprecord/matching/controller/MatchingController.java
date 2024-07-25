package com.finalproject.triprecord.matching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MatchingController {

	@GetMapping("matchingMain.ma")
	public String matchingMain() {
		
		return "matchingMain";
	}
	
	@GetMapping("matchingDetail.ma")
	public String matchingDetail() {
		
		return "matchingDetail";
	}
	
	@GetMapping("matchingRequest.ma")
	public String matchingRequest() {
		
		return "matchingRequest";
	}
	
	@GetMapping("matchingReview.ma")
	public String matchingReview() {
		
		return "matchingReview";
	}
}
