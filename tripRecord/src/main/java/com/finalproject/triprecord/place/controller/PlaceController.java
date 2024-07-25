package com.finalproject.triprecord.place.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.place.model.service.PlaceService;

@Controller
public class PlaceController {
	
	@Autowired
	private PlaceService pService;
	
	@GetMapping("recoPlace.pla")
	public String recoPlacelist(@RequestParam(value="page", defaultValue="1") int page, Model model) {
		ArrayList<Local> list = pService.getLocalList();
		if(list != null) {
			model.addAttribute("list", list);
			model.addAttribute("page", page);
			return "recommendPlace";
		}else {
			return "fail";
		}
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
