package com.finalproject.triprecord.matching.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.exception.MatchingException;
import com.finalproject.triprecord.matching.model.service.MatchingService;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.controller.MyPageController;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.plan.model.vo.Schedule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class MatchingController {
	
	@Autowired
	private PlaceService pService;
	
	@Autowired
	private MatchingService matService;
	
	@Autowired
	private GoogleDriveService gdService;
	
	@GetMapping("homeView.ma")
	@ResponseBody
	public String homeView() {
		return null;
	}
	
	@GetMapping("matchingMain.ma")
	public String matchingMain(@RequestParam(value="page", defaultValue="1") int currentPage, @RequestParam(value="localNo", defaultValue="1")int localNo, Model model, HttpServletRequest request, HttpSession session) {
		
		int listCount = matService.getPlannerListCount(localNo);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<Planner> list = matService.getPlannerList(pi, localNo);
		ArrayList<Local> localList = matService.selectLocalList();
		
		//별점 + 좋아요 + 플래너지역
		Map<Integer, String> selectLocalMap = new HashMap<>();
		Map<Integer, Integer> plannerLikesMap = new HashMap<>();
		Map<Integer, Double> starMap = new HashMap<>();
		for(Planner planner : list) {
			int pNo = planner.getMemberNo();
			int likes = matService.countLikes(pNo);
			String localNames = matService.selectLocalName(pNo);
			Double AvgStar = matService.AverageStar(pNo);
			
			starMap.put(pNo, AvgStar);
			plannerLikesMap.put(pNo, likes);
			selectLocalMap.put(pNo, localNames);
			
		}
		
		model.addAttribute("localList",localList);
		model.addAttribute("localName", selectLocalMap);
		model.addAttribute("list",list);
		model.addAttribute("pi", pi);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("likes", plannerLikesMap);
		model.addAttribute("star", starMap);
		
		return "matchingMain";
	}
	
	@GetMapping("localImgClick.ma")
	@ResponseBody
	public String localImgClick(@RequestParam(value="page", defaultValue="1") int currentPage, @RequestParam(value="localNo", defaultValue="1")int localNo, HttpServletRequest request, HttpSession session) {
		
		int listCount = matService.getPlannerListCount(localNo);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<Planner> list = matService.getPlannerList(pi, localNo);
		
		//별점 + 좋아요 + 플래너지역
		Map<Integer, String> selectLocalMap = new HashMap<>();
		Map<Integer, Integer> plannerLikesMap = new HashMap<>();
		Map<Integer, Double> starMap = new HashMap<>();
		for(Planner planner : list) {
			int pNo = planner.getMemberNo();
			int likes = matService.countLikes(pNo);
			String localNames = matService.selectLocalName(pNo);
			Double AvgStar = matService.AverageStar(pNo);
			
			starMap.put(pNo, AvgStar);
			plannerLikesMap.put(pNo, likes);
			selectLocalMap.put(pNo, localNames);
		}
		
		Map<String, Object> json = new HashMap<>();
		json.put("list", list);
		json.put("pi", pi);
		json.put("loc", request.getRequestURI());
		json.put("likes", plannerLikesMap);
		json.put("localName", selectLocalMap);
		json.put("star", starMap);
		
		Gson gson = new GsonBuilder().create();
		return gson.toJson(json);
	}
	
	@GetMapping("selectPlanner.ma")
	public String selectPlanner(@RequestParam(value="page", defaultValue="1") int currentReviewPage, @RequestParam("pNo") int pNo, @RequestParam("page") int page, Model model, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUserNo = 0;
		if(loginUser != null) {
			loginUserNo = loginUser.getMemberNo();
		}
		
		//플래너 정보
		Planner planner = matService.selectPlanner(pNo);
		
		//해시태그
		ArrayList<HashTag> tagList = matService.selectTagList(pNo);
		
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
		ArrayList<Review> rList = matService.getReviewList(pi, pNo);
		
		ArrayList<Image> rImgList = matService.selectrImgList();
		ArrayList<Image> iImgList = matService.selectiImgList(pNo);
		
		model.addAttribute("tagList", tagList);
		model.addAttribute("AvgStar", AvgStar);
		model.addAttribute("pi", pi);
		model.addAttribute("rList", rList);
		model.addAttribute("rImgList", rImgList);
		model.addAttribute("iImgList", iImgList);
		model.addAttribute("planner", planner);
		model.addAttribute("page", page);
		model.addAttribute("checkLikes", checkLikes);
		model.addAttribute("likes", likes);
		model.addAttribute("localName", localNames);
		
		return "matchingDetail";
	}
	
	@GetMapping("matchingRequest.ma")
	public String matchingRequest(@RequestParam("pNo") int pNo, @RequestParam("page") int page, @RequestParam(value="payPoint", defaultValue="0") int payPoint, Model model, HttpSession session) {
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
		
		model.addAttribute("payPoint", payPoint);
		model.addAttribute("planner", planner);
		model.addAttribute("page", page);
		model.addAttribute("checkLikes", checkLikes);
		model.addAttribute("likes", likes);
		model.addAttribute("localName", localNames);
		
		return "matchingRequest";
	}
	@PostMapping("insertRequest.ma")
	public String insertRequest(@ModelAttribute Schedule schedule,
								@ModelAttribute ReqSchedule reqSchedule,
								@RequestParam("pNo") int pNo,
								@RequestParam("lNo") int lNo,
								Model model,
								RedirectAttributes ra,
								HttpSession session){
		Member loginUser = (Member)session.getAttribute("loginUser");
		int loginUserNo = 0;
		int loginUserPoint = 0;
		if(loginUser != null) {
			loginUserNo = loginUser.getMemberNo();
			loginUserPoint = loginUser.getMemberPoint();
		}
		
		SimpleDateFormat sdp = new SimpleDateFormat("yyyy/MM/dd");
		
		Date scEndDate = null;
		Date scStartDate = null;
		try {
			scEndDate = sdp.parse(schedule.getEndDate());
			scStartDate = sdp.parse(schedule.getStartDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		MyPageController mc = new MyPageController();
		int payPoint = mc.getDayPoint(scEndDate, scStartDate);
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("loginUserNo", loginUserNo);
		map.put("payPoint", payPoint);
		
		int pointResult = matService.checkPoint(map);
		if (pointResult > 1) {
			schedule.setWriterNo(pNo);
			schedule.setScLocalNo(lNo);
			
			int result1 = matService.insertSchedule(schedule);
			
			reqSchedule.setReqPlaNo(pNo);
			reqSchedule.setReqMemNo(loginUserNo);
			reqSchedule.setScheNo(schedule.getScNo());
			reqSchedule.setPayPoint(payPoint);
			
			int result2 = matService.insertReqSchedule(reqSchedule);
			
			if(result1 + result2 > 2) {
				ra.addAttribute("pNo", pNo);
				ra.addAttribute("page", 1);
				return "redirect:selectPlanner.ma";
			} else {
				throw new MatchingException("일정 요청을 실패했습니다.");
			}
		} else {
			ra.addAttribute("payPoint", payPoint);
			ra.addAttribute("pNo", pNo);
			ra.addAttribute("page", 1);
			return "redirect:matchingRequest.ma";
		}
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
		
		//해시태그
		ArrayList<HashTag> tagList = matService.selectTagList(pNo);
		
		//좋아요 + 지역
		HashMap<String, Integer> likemap = new HashMap<>();
		likemap.put("pNo", pNo);
		likemap.put("loginUserNo", loginUserNo);
		int likes = matService.countLikes(pNo);
		int checkLikes = matService.checkLikes(likemap);
		
		String localNames = matService.selectLocalName(pNo);
		
		model.addAttribute("tagList", tagList);
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
		HashMap<String, Integer> map = new HashMap<>();
		map.put("pNo", pNo);
		map.put("loginUserNo", loginUserNo);
		
		int result = matService.deleteLikes(map);
			JSONObject json = new JSONObject();
			json.put("result", result);
		return json.toString();
	}
	
	@PostMapping("insertReview.ma")
	public String insertReview(@ModelAttribute Review r, @RequestParam("plannerNo") int pNo, @RequestParam(value="files", required=false) ArrayList<MultipartFile> files, HttpServletRequest request, RedirectAttributes ra) {
		
		int memNo = ((Member)request.getSession().getAttribute("loginUser")).getMemberNo();
		r.setMemberNo(memNo);
		r.setRevRefNo(pNo);
		r.setRevRefType("PLANNER");
		
		int result = matService.insertReview(r);
		
		ArrayList<Image> list = new ArrayList<Image>();
		for(int i = 0; i < files.size(); i++) {
			MultipartFile upload = files.get(i);
			
			if(upload != null && !upload.isEmpty()) {
	            String fileId;
	            
				try {
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image a = new Image();
					a.setImageOriginName(upload.getOriginalFilename());
					a.setImageRename(fileId);
					a.setImagePath("drive://files/" + fileId);
					a.setImageThum(2);
					a.setImageRefType("REVIEW");
					a.setImageRefNo(r.getReviewNo());
					
					list.add(a);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		int iResult = 0;
		if(!list.isEmpty()) {
			iResult = matService.insertReviewImage(list);
		}
		if(result + iResult == 1 + list.size()) {
			ra.addAttribute("page", 1);
			ra.addAttribute("pNo", pNo);
			return "redirect:selectPlanner.ma";
		} else {
			for(Image a : list) {
				try {
					gdService.deleteFile(a.getImageRename());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			throw new MatchingException("리뷰 작성을 실패했습니다.");
		}
	}
	
	@PostMapping("updateReviewView.ma")
	public String updateReviewView(@RequestParam("pNo") int pNo, @RequestParam("rNo") int rNo, Model model, HttpSession session) {
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
		//리뷰 이미지
		ArrayList<Image> rImgList = matService.selectrImgList(rNo);
		
		Review review = matService.selectReview(rNo);
		
		model.addAttribute("rImgList", rImgList);
		model.addAttribute("r", review);
		model.addAttribute("planner", planner);
		model.addAttribute("checkLikes", checkLikes);
		model.addAttribute("likes", likes);
		model.addAttribute("localName", localNames);
		
		return "matchingReviewUpdate";
	}
	
	@PostMapping("updateReview.ma")
	public String updateReview(@ModelAttribute Review review,
							   @RequestParam("plannerNo")int pNo,
							   @RequestParam(value="delImg", required=false) ArrayList<String> delImgs,
							   @RequestParam(value="files", required=false) ArrayList<MultipartFile> files,
							   RedirectAttributes ra) {
		
		int result = pService.updateReview(review);
		
		ArrayList<String> deleteImg = new ArrayList<String>();
		int delResult = 0;
		int insertResult = 0;
		
		try {
			// 이미지 삭제
			if(delImgs != null && !delImgs.isEmpty()) {
				for(String del : delImgs) {
					if(!del.equals("none")) {
						deleteImg.add(del);
						// 구글 드라이브에서 삭제
						gdService.deleteFile(del);
					}
				}
				if(!deleteImg.isEmpty()) {
					delResult = matService.deleteReviewImage(deleteImg);
				}
			}
			
			
			// 이미지 추가
			ArrayList<Image> list = new ArrayList<Image>();
			for(int i = 0; i < files.size(); i++) {
				MultipartFile upload = files.get(i);
				
				//if(!upload.getOriginalFilename().equals("")) {
				if(upload != null && !upload.isEmpty()) {
		            String fileId;
		            
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image Img = new Image();
					Img.setImageOriginName(upload.getOriginalFilename());
					Img.setImageRename(fileId);
					Img.setImagePath("drive://files/" + fileId);
					Img.setImageThum(2);
					Img.setImageRefType("REVIEW");
					Img.setImageRefNo(review.getReviewNo());
					
					list.add(Img);
				}
			}
			if(!list.isEmpty()) {
				insertResult = matService.insertReviewImage(list);
				
				if(insertResult < 0) {
					for(Image i : list) {
						gdService.deleteFile(i.getImageRename());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(result + insertResult + delResult == 2 + deleteImg.size()) {
			ra.addAttribute("pNo", pNo);
			ra.addAttribute("page", 1);
			return "redirect:selectPlanner.ma";
		} else {
			throw new MatchingException("리뷰 수정을 실패했습니다.");
		}
	}
	
	@PostMapping("deleteReview.ma")
	public String deleteReview(@RequestParam("pNo")int pNo,
							   @RequestParam("rNo")int rNo,
							   RedirectAttributes ra) {
		
		int result = matService.deletePlannerReview(rNo);
		
		if(result > 0) {
			ra.addAttribute("pNo", pNo);
			ra.addAttribute("page",1);
			return "redirect:selectPlanner.ma";
		} else {
			throw new MatchingException("리뷰 삭제를 실패했습니다.");
		}
	}
}
