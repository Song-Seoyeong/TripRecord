package com.finalproject.triprecord;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;

@Controller
public class HomeController {
	
	@Autowired
	private PlaceService pService;
	
	
	@GetMapping("home")
	public String home(Model model) {
		
		
		
		// 일정 이미지
		Image img = pService.planImg();
		
		// 관광지 top 5
		ArrayList<Place> pList = pService.topPlaceList();
		
		
		model.addAttribute("img", img);
		model.addAttribute("pList", pList);
		return "home";
	}
}
