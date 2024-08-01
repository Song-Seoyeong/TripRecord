package com.finalproject.triprecord.matching.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.matching.model.service.MatchingService;
import com.finalproject.triprecord.matching.model.vo.Planner;
import com.finalproject.triprecord.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class MatchingController {

	@Autowired
	private MatchingService matService;
	
	@GetMapping("matchingMain.ma")
	public String matchingMain(@RequestParam(value="page", defaultValue="1") int currentPage, @RequestParam(value="localNo", defaultValue="1")int localNo, Model model, HttpServletRequest request, HttpSession session) {
		
		int listCount = matService.getPlannerListCount(localNo);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<Planner> list = matService.getPlannerList(pi, localNo);
		
		//좋아요 + 지역
		Map<Integer, String> selectLocalMap = new HashMap();
		Map<Integer, Integer> plannerLikesMap = new HashMap();
		for(Planner planner : list) {
			int pNo = planner.getMemberNo();
			int likes = matService.countLikes(pNo);
			String localNames = matService.selectLocalName(pNo);
			
			plannerLikesMap.put(pNo, likes);
			selectLocalMap.put(pNo, localNames);
		}
		
		model.addAttribute("list",list);
		model.addAttribute("pi", pi);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("likes", plannerLikesMap);
		model.addAttribute("localName", selectLocalMap);
		
		return "matchingMain";
	}
	
	@GetMapping("selectPlanner.ma")
	public String selectPlanner(@RequestParam("pNo") int pNo, @RequestParam("page") int page, Model model,HttpSession session) {
		
		Planner planner = matService.selectPlanner(pNo);
		
		//좋아요 + 지역
		int likes = matService.countLikes(pNo);
		String localNames = matService.selectLocalName(pNo);
		
		model.addAttribute("planner", planner);
		model.addAttribute("page", page);
		model.addAttribute("likes", likes);
		model.addAttribute("localName", localNames);
		
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
	
	@PostMapping(value="insertLikes.ma", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String insertBLikes(@RequestParam("pNo") int pNo, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUsersNo = 0;
		if(loginUser != null) {
			loginUsersNo = loginUser.getMemberNo();
		}
		
		JSONObject json = new JSONObject();
		if(loginUsersNo == 0) {
			json.put("result", -1);
			return json.toString();
		}
		
	    HashMap<String, Integer> map = new HashMap<>();
	    map.put("refPlannerNo", pNo);
	    map.put("loginUsersNo", loginUsersNo);
	    
	    int result = matService.insertBLikes(map);
	    json.put("result", result);
	    return json.toString();
	}
	
	@PostMapping(value="deleteBLikes.ma", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String deleteLikes(@RequestParam("pNo") int pNo, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUserNo = 0;
		if(loginUser != null) {
			loginUserNo = loginUser.getMemberNo();
		}
		HashMap<String, Integer> map = new HashMap();
		map.put("refPlannerNo", pNo);
		map.put("loginUserNo", loginUserNo);
		
		int result = matService.deleteBLikes(map);
			JSONObject json = new JSONObject();
			json.put("result", result);
		return json.toString();
	}
	
}
