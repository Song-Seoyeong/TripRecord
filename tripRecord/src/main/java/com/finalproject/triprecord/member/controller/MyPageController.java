package com.finalproject.triprecord.member.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import com.finalproject.triprecord.member.model.exception.MemberException;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {
	
	@Autowired
	MemberService mService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	//////////마이페이지 ////////// 
	@GetMapping("myPage.mp")
	public String moveToMyPage(HttpSession session, Model model) {
		return "myPage";
	}
	//내 정보 수정
	@PostMapping("updateMember.mp")
	public String updateMember(HttpSession session, SessionStatus status, Model model,
							   @RequestParam("nickName")String nickName, 
							   @RequestParam("phoneNo")String phoneNo, 
							   @RequestParam("memEmail")String memEmail, @RequestParam("domain")String domain) {
		String id = ((Member)session.getAttribute("loginUser")).getMemberId();
		String email = memEmail + '@' + domain;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("nickName", nickName);
		map.put("phoneNo", phoneNo);
		map.put("email", email);
		
		int result = mService.updateMember(map);
		if(result > 0) {
			model.addAttribute("pageIndex", "myPage");
			return "alert";
		}else {
			throw new MemberException("정보 수정 실패");
		}
	}
	
	@GetMapping("updateMyPwd.mp")
	public String moveToUpdateMyPwd() {
		return "updateMyPwd";
	}
	
	//내 비밀번호 수정
	@PostMapping("updatPwdOfMe.mp")
	public String updatPwd(HttpSession session, Model model,
						   @RequestParam("currentPwd") String pwd,
						   @RequestParam("newPwd") String newPwd) {
		String id = ((Member)session.getAttribute("loginUser")).getMemberId();
		Member m = new Member();
		m.setMemberId(id);
		m.setMemberPwd(pwd);
		Member loginUser = mService.login(m);
		if(bcrypt.matches(m.getMemberPwd(), loginUser.getMemberPwd())) {
			//비번 수정
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", id);
			map.put("newPwd", bcrypt.encode(newPwd));
			
			int result = mService.updatPwdOfMe(map);
			if(result > 0) {
				model.addAttribute("pageIndex", "updateMyPwd");
				return "alert";
			}else {
				throw new MemberException("비밀번호가 수정에 실패하였습니다");
			}
		}else {
			throw new MemberException("비밀번호가 일치하지 않습니다");
		}
	}

	@GetMapping("promoteToPlanner.mp")
	public String moveToPromoteToPlanner() {
		return "promoteToPlanner";
	}
	
	//관리자 신청
	@PostMapping("promotion.mp")
	public String promotion(HttpSession session,
							@RequestParam("region") int lNo,
						 	@RequestParam("introProfile") String intro,
						 	@RequestParam("submitContent") String content) {
		
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		System.out.println(lNo);
		System.out.println(intro);
		System.out.println(content);
		System.out.println(memberNo);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("lNo", lNo);
		map.put("intro", intro);
		map.put("content", content);
		map.put("memberNo", memberNo);
		map.put("grade", "PLANNER");
		int result = mService.reqPromote(map);
		if(result > 0) {
			return "redirect:promoteToPlanner.mp";
		}else {
			throw new MemberException("플래너 신청에 실패하였습니다");
		}
	}

	@GetMapping("myPoint.mp")
	public String moveToMyPoint() {
		return "myPoint";
	}

	@GetMapping("myPayPoint.mp")
	public String moveToMyPayPoint() {
		return "myPayPoint";
	}

	@GetMapping("myPlan.mp")
	public String moveToMyPlan() {
		return "myPlan";
	}

	@GetMapping("detailReqPlan.mp")
	public String moveToDetailReqPlan() {
		return "detailReqPlan";
	}

	@GetMapping("feedback.mp")
	public String moveToFeedback() {
		return "feedback";
	}

	@GetMapping("myTripNote.mp")
	public String moveToMyTripNote() {
		return "myTripNote";
	}

	@GetMapping("detailMyTripNote.mp")
	public String moveToDetailMyTripNote() {
		return "detailMyTripNote";
	}

	@GetMapping("myInquiry.mp")
	public String moveToMyInquiry() {
		return "myInquiry";
	}

	@GetMapping("updateInquiry.mp")
	public String moveToUpdateInquiry() {
		return "updateInquiry";
	}

	@GetMapping("detailMyInquiry.mp")
	public String moveToDetailMyInquiry() {
		return "detailMyInquiry";
	}

	@GetMapping("myBoard.mp")
	public String moveToMyBoard() {
		return "myBoard";
	}

	@GetMapping("detailMyBoard.mp")
	public String moveToDetailMyBoard() {
		return "detailMyBoard";
	}

	@GetMapping("updateMyBoard.mp")
	public String moveToUpdateMyBoard() {
		return "updateMyBoard";
	}

////////// 플래너 페이지 ////////// 
	@GetMapping("plannerPage.mp")
	public String moveToPlannerPage() {
		return "plannerPage";
	}

	@GetMapping("request.mp")
	public String moveToRequest() {
		return "request";
	}

	@GetMapping("detailRequest.mp")
	public String moveToDetailRequest() {
		return "detailRequest";
	}

	@GetMapping("sales.mp")
	public String moveToSales() {
		return "sales";
	}

	@GetMapping("updatePlanner.mp")
	public String moveToUpdatePalnner() {
		return "updatePlanner";
	}

	@GetMapping("processRequest.mp")
	public String moveToProcessRequest() {
		return "processRequest";
	}

	@GetMapping("requestEnd.mp")
	public String moveToRequestEnd() {
		return "requestEnd";
	}
}
