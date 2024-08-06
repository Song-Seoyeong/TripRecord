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
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.service.MatchingService;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		ArrayList<Local> localList = matService.selectLocalList();
		
		//좋아요 + 플래너지역
		Map<Integer, String> selectLocalMap = new HashMap();
		Map<Integer, Integer> plannerLikesMap = new HashMap();
		for(Planner planner : list) {
			int pNo = planner.getMemberNo();
			int likes = matService.countLikes(pNo);
			String localNames = matService.selectLocalName(pNo);
			
			plannerLikesMap.put(pNo, likes);
			selectLocalMap.put(pNo, localNames);
		}
		
		
		model.addAttribute("localList",localList);
		model.addAttribute("list",list);
		model.addAttribute("pi", pi);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("likes", plannerLikesMap);
		model.addAttribute("localName", selectLocalMap);
		
		return "matchingMain";
	}
	
	@GetMapping("localImgClick.ma")
	@ResponseBody
	public String localImgClick(@RequestParam(value="page", defaultValue="1") int currentPage, @RequestParam(value="localNo", defaultValue="1")int localNo, HttpServletRequest request, HttpSession session) {
		
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
		
		Map<String, Object> json = new HashMap<>();
		json.put("list", list);
		json.put("pi", pi);
		json.put("loc", request.getRequestURI());
		json.put("likes", plannerLikesMap);
		json.put("localName", selectLocalMap);
		
		Gson gson = new GsonBuilder().create();
		return gson.toJson(json);
	}
	
	
	
	
	@GetMapping("selectPlanner.ma")
	public String selectPlanner(@RequestParam(value="page", defaultValue="1") int currentReviewPage, @RequestParam("pNo") int pNo, @RequestParam("page") int page, Model model,HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUserNo = 0;
		if(loginUser != null) {
			loginUserNo = loginUser.getMemberNo();
		}
		
		//사진들
		//ArrayList<Image> iList = matService.selectImage(pNo);
		//if iList가 프사인 경우, 소개글 사진인 경우로 나눠서 가져옴
		
		
		//플래너 정보
		Planner planner = matService.selectPlanner(pNo);
		
		//좋아요 + 지역
		HashMap<String, Integer> likemap = new HashMap<>();
		likemap.put("pNo", pNo);
		likemap.put("loginUserNo", loginUserNo);
		int likes = matService.countLikes(pNo);
		int checkLikes = matService.checkLikes(likemap);
		
		String localNames = matService.selectLocalName(pNo);
		
		//후기 + 별점
		Double AvgStar = matService.AverageStar(pNo);
		int ReviewlistCount = matService.getReviewListCount(pNo);
		PageInfo pi = Pagination.getPageInfo(currentReviewPage, ReviewlistCount, 5);
		ArrayList<Review> list = matService.getReviewList(pi, pNo);
		
		model.addAttribute("AvgStar", AvgStar);
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);
		
		model.addAttribute("planner", planner);
		model.addAttribute("page", page);
		model.addAttribute("checkLikes", checkLikes);
		model.addAttribute("likes", likes);
		model.addAttribute("localName", localNames);
		
		return "matchingDetail";
	}
	
	@GetMapping("matchingRequest.ma")
	public String matchingRequest() {
		
		return "matchingRequest";
	}
	
	@GetMapping("matchingReview.ma")
	public String matchingReview(@RequestParam("pNo") int pNo, Model model, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUserNo = 0;
		if(loginUser != null) {
			loginUserNo = loginUser.getMemberNo();
		}
		
		//플래너 정보
		Planner planner = matService.selectPlanner(pNo);
		
		//좋아요 + 지역
		HashMap<String, Integer> likemap = new HashMap<>();
		likemap.put("pNo", pNo);
		likemap.put("loginUserNo", loginUserNo);
		int likes = matService.countLikes(pNo);
		int checkLikes = matService.checkLikes(likemap);
		
		String localNames = matService.selectLocalName(pNo);
		
		model.addAttribute("planner", planner);
		model.addAttribute("checkLikes", checkLikes);
		model.addAttribute("likes", likes);
		model.addAttribute("localName", localNames);
		
		return "matchingReview";
	}
	
	@PostMapping(value="insertLikes.ma", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String insertLikes(@RequestParam("pNo") int pNo, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUserNo = 0;
		if(loginUser != null) {
			loginUserNo = loginUser.getMemberNo();
		}
		
		JSONObject json = new JSONObject();
		if(loginUserNo == 0) {
			json.put("result", -1);
			return json.toString();
		}
		
	    HashMap<String, Integer> map = new HashMap<>();
	    map.put("pNo", pNo);
	    map.put("loginUserNo", loginUserNo);
	    
	    int result = matService.insertLikes(map);
	    
	    json.put("result", result);
	    return json.toString();
	}
	
	@PostMapping(value="deleteLikes.ma", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String deleteLikes(@RequestParam("pNo") int pNo, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUserNo = 0;
		if(loginUser != null) {
			loginUserNo = loginUser.getMemberNo();
		}
		HashMap<String, Integer> map = new HashMap();
		map.put("pNo", pNo);
		map.put("loginUserNo", loginUserNo);
		
		int result = matService.deleteLikes(map);
			JSONObject json = new JSONObject();
			json.put("result", result);
		return json.toString();
	}
}
