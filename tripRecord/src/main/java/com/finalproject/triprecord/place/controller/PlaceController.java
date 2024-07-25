package com.finalproject.triprecord.place.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.place.model.exception.PlaceException;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;

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
	public String placeDetail(@RequestParam("contentid") int contentid,
							  @RequestParam("contenttypeid") int contenttypeid,
							  @RequestParam("page") int page,
							  @RequestParam("areaCode") int areaCode,
							  Model model) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("contentid", contentid);
		map.put("areaCode", areaCode);
		
		// 장소 db에 저장되어있는지 확인
		int checkPlace = pService.checkPlace(map);
		
		// db 저장 여부에 따라 다름
		Place p = new Place();
		if(checkPlace > 0){
			p = pService.selectPlace(map);
		}else {
			int result = pService.insertPlace(map);
			if(result > 0) {
				p.setLocalNo(areaCode);
				p.setPlaceNo(contentid);
				p.setPlaceCount(1);
				p.setPlaceStar(0);
			}else {
				throw new PlaceException("장소 조회 중 에러 발생");
			}
		}
		
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
