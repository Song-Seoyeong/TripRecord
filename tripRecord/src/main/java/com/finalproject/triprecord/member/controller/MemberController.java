package com.finalproject.triprecord.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@GetMapping("loginView.me")
	public String loginView() {
		return "login";
	}
	
	@GetMapping("enrollView.me")
	public String enrollView() {
		return "enrollMember";
	}
	
	@GetMapping("searchIdView.me")
	public String searchId() {
		return "searchId";
	}
	
	@GetMapping("searchPwdView.me")
	public String searchPwd() {
		return "searchPwd";
	}
	@PostMapping("login.me")
	public String login(@RequestParam("memberId") String id, @RequestParam("memberPwd") String pwd, HttpSession session) {
		Member m = new Member();
		m.setMemberId(id);
		m.setMemberPwd(pwd);
		System.out.println("로그인 서비스 갈거임");
		Member loginUser = mService.login(m);
		System.out.println("로그인가져옴");
		if(bcrypt.matches(m.getMemberPwd(), loginUser.getMemberPwd())) {
			session.setAttribute("loginUser", loginUser);
			return "index";
		}else {
			return "에러페이지";
		}
	}
	@GetMapping("updatePwdView.me")
	public String updatePwd() {
		return "updatePwd";
	}
	
	@GetMapping("enrollPlannerView.me")
	public String enrollPlanner() {
		return "enrollPlanner";
	}
	
	@PostMapping("enrollMember.me")
	public String enrollMember(@ModelAttribute Member m, Model model,
						@RequestParam("memEmail") String frontEmail, @RequestParam("domain") String backEmail) {
		String email = null;
		if(!frontEmail.equals("")) {
			email = frontEmail + "@" + backEmail;
		}
		m.setEmail(email);
		String encPwd = bcrypt.encode(m.getMemberPwd());
		m.setMemberPwd(encPwd);
		
		int result = mService.enrollMember(m);
		if(result>0) {
			return "redirect:loginView.me";
		} else {
			return "에러페이지";
		}
	}
	
	
	
	
	
	
	
	///////// 관리자 ////////////
	@GetMapping("dashBoard.ad")
	public String dashBoardView() {
		return "dashBoard";
	}
	
	@GetMapping("userManage.ad")
	public String userManageView() {
		return "userManage";
	}
	
	@GetMapping("questManage.ad")
	public String questManageView() {
		return "questManage";
	}
	
	@GetMapping("gradeManage.ad")
	public String gradeManageView() {
		return "gradeManage";
	}
	
	@GetMapping("noticeManage.ad")
	public String noticeManageView() {
		return "noticeManage";
	}
	
	@GetMapping("pointManage.ad")
	public String pointManageView() {
		return "pointManage";
	}
	
	@GetMapping("boardManage.ad")
	public String boardManageView() {
		return "boardManage";
	}
	
	@GetMapping("hashTagManage.ad")
	public String hashTagManageView() {
		return "hashTagManage";
	}
	
	
	
	
	////////// 마이페이지 ////////// 
	@GetMapping("myPage.me")
	public String moveToMyPage() {
		return "myPage";
	}
	
	@GetMapping("updateMyPwd.me")
	public String moveToUpdateMyPwd() {
		return "updateMyPwd";
	}
	
	@GetMapping("promoteToPlanner.me")
	public String moveToPromoteToPlanner() {
		return "promoteToPlanner";
	}
	
	@GetMapping("myPoint.me")
	public String moveToMyPoint() {
		return "myPoint";
	}
	
	@GetMapping("myPayPoint.me")
	public String moveToMyPayPoint() {
		return "myPayPoint";
	}
	
	@GetMapping("myPlan.me")
	public String moveToMyPlan() {
		return "myPlan";
	}
	
	@GetMapping("detailReqPlan.me")
	public String moveToDetailReqPlan() {
		return "detailReqPlan";
	}
	
	@GetMapping("feedback.me")
	public String moveToFeedback() {
		return "feedback";
	}
	
	@GetMapping("myTripNote.me")
	public String moveToMyTripNote() {
		return "myTripNote";
	}
	
	@GetMapping("detailMyTripNote.me")
	public String moveToDetailMyTripNote() {
		return "detailMyTripNote";
	}
	
	@GetMapping("myInquiry.me")
	public String moveToMyInquiry() {
		return "myInquiry";
	}
	
	@GetMapping("updateInquiry.me")
	public String moveToUpdateInquiry() {
		return "updateInquiry";
	}
	
	
	@GetMapping("detailMyInquiry.me")
	public String moveToDetailMyInquiry() {
		return "detailMyInquiry";
	}

	@GetMapping("myBoard.me")
	public String moveToMyBoard() {
		return "myBoard";
	}
	
	@GetMapping("detailMyBoard.me")
	public String moveToDetailMyBoard() {
		return "detailMyBoard";
	}
	
	@GetMapping("updateMyBoard.me")
	public String moveToUpdateMyBoard() {
		return "updateMyBoard";
	}
	
	
	
	
	
	
	
	
	
	
	
	////////// 플래너 페이지 ////////// 
	@GetMapping("plannerPage.me")
	public String moveToPlannerPage() {
		return "plannerPage";
	}
	
	@GetMapping("request.me")
	public String moveToRequest() {
		return "request";
	}
	
	@GetMapping("detailRequest.me")
	public String moveToDetailRequest() {
		return "detailRequest";
	}
	
	@GetMapping("sales.me")
	public String moveToSales() {
		return "sales";
	}
	
	@GetMapping("updatePlanner.me")
	public String moveToUpdatePalnner() {
		return "updatePlanner";
	}
	
	@GetMapping("processRequest.me")
	public String moveToProcessRequest() {
		return "processRequest";
	}
	
	@GetMapping("requestEnd.me")
	public String moveToRequestEnd() {
		return "requestEnd";
	}
}
