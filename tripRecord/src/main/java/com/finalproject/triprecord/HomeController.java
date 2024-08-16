package com.finalproject.triprecord;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.matching.model.service.MatchingService;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.controller.MyPageController;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;
import com.finalproject.triprecord.plan.model.vo.Schedule;

@Controller
public class HomeController {
	
	@Autowired
	private MatchingService matService;
	
	@Autowired
	private PlaceService pService;
	
	@Autowired
	private MemberService mService;
	
	@GetMapping("home")
	public String home(Model model) {
		
		// 여행 지역 조회수 top 5
		ArrayList<Local> lList = pService.getTopList();
		
		// 일정 이미지
		Image img = pService.planImg();
		
		// 플래너 top 5 (좋아요순)
		ArrayList<Planner> plannerList = matService.topPlannerList();
		
		// 관광지 top 5
		ArrayList<Place> pList = pService.topPlaceList();
		
		model.addAttribute("lList", lList);
		model.addAttribute("img", img);
		model.addAttribute("plList", plannerList);
		model.addAttribute("pList", pList);
		return "home";
	}
	
	@GetMapping("updateReqStatus.mp")
	@ResponseBody
	public String updateReqStatus() {
		ArrayList<ReqSchedule> rList = mService.getReqTotalList();
		
		// 일정 생성 이전 리스트
		ArrayList<ReqSchedule> beforeSchList = new ArrayList<ReqSchedule>();
		
		// 일정 생성 이후 리스트
		ArrayList<ReqSchedule> afterSchList = new ArrayList<ReqSchedule>();
		
		for(ReqSchedule r : rList) {
			// 신청별 플래너 정보
			Planner p = new Planner();
			p = mService.getReqPlanner(r.getReqPlaNo());
			
			// 신청별 schedule
			Schedule sch = mService.getSchedule(r.getScheNo());
			
			// 날짜 체크
			LocalDate localDate = sch.getScStartDate().toLocalDate();

	        // 오늘 날짜 가져오기
	        LocalDate today = LocalDate.now();

	        // 날짜 비교
	        if (localDate.isBefore(today)) {
	        	if(r.getReqStatus() == 1) beforeSchList.add(r);
	        	else afterSchList.add(r);
	        }
		}
		int result = 0;
		if(!beforeSchList.isEmpty()) {
			for(ReqSchedule r : beforeSchList) {
				r.setReqStatus(4);
				result += mService.updateReqState(r);
				
				
				
			}
		}
		
		if(!afterSchList.isEmpty()) {
			for(ReqSchedule r : afterSchList) {
				
			}
		}
		return null;
	}
}
