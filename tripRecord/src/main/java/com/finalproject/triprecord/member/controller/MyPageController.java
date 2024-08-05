package com.finalproject.triprecord.member.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.Image;
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
	
	@Autowired
	private GoogleDriveService gdService;
	
	//////////마이페이지 ////////// 
	@GetMapping("myPage.mp")
	public String moveToMyPage(HttpSession session, Model model) {

		    int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		    Image image = mService.existFileId(memberNo); 
		  
		    if (image != null && image.getImageRename() != null) {
		        String existFileId = image.getImageRename(); 
		        model.addAttribute("rename", existFileId);
		    } else {
		        // 이미지가 없거나 리네임이 없는 경우 처리
		        model.addAttribute("rename", "defaultImageName"); 
		    }

		    return "myPage";
	}
	//내 정보 수정
	@PostMapping("updateMember.mp")
	@ResponseBody
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
			return "success";
		}else {
			throw new MemberException("정보 수정 실패");
		}
	}
	
	@GetMapping("updateMyPwd.mp")
	public String moveToUpdateMyPwd(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "updateMyPwd";
	}
	
	//내 비밀번호 수정
	@PostMapping("updatPwdOfMe.mp")
	@ResponseBody
	public String updatPwd(HttpSession session, Model model,
						   @RequestParam("currentPwd") String pwd,
						   @RequestParam("newPwd") String newPwd) {
		String id = ((Member)session.getAttribute("loginUser")).getMemberId();
		Member m = new Member();
		m.setMemberId(id);
		m.setMemberPwd(pwd);
		Member loginUser = mService.login(m);
		
		//기존 비번과 새 비번 동일여부 확인 후 수정
		Member m2 = new Member();
		m2.setMemberPwd(newPwd);
		if(bcrypt.matches(m2.getMemberPwd(), loginUser.getMemberPwd())) {
			return "true" ;
		}else {
			if(bcrypt.matches(m.getMemberPwd(), loginUser.getMemberPwd())) {
				//비번 수정
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("newPwd", bcrypt.encode(newPwd));
				
				int result = mService.updatPwdOfMe(map);
				if(result > 0) {
					
					return "false";
				}else {
					throw new MemberException("비밀번호가 수정에 실패하였습니다");
				}
			}else {
				throw new MemberException("비밀번호가 일치하지 않습니다");
			}
		}
	}

	@GetMapping("promoteToPlanner.mp")
	public String moveToPromoteToPlanner(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "promoteToPlanner";
	}
	
	//판매자 신청
	@PostMapping("promotion.mp")
	@ResponseBody
	public String promotion(HttpSession session,
							@RequestParam("region") int lNo,
						 	@RequestParam(value="introProfile", required=false) String intro,
						 	@RequestParam(value="submitContent", required=false) String content) {
		
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		
		RequestGrade rg = mService.checkRequest(memberNo);
		if(rg != null) {
			return rg.getGrade();
		}else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("lNo", lNo);
			map.put("intro", intro);
			map.put("content", content);
			map.put("memberNo", memberNo);
			map.put("grade", "PLANNER");
			int result = mService.reqPromote(map);
			if(result > 0) {
				return "success";
			}else {
				throw new MemberException("플래너 신청에 실패하였습니다");
			}
		}
	}
	
	//관리자 요청
	@PostMapping("submitAdmin.mp")
	@ResponseBody
	public String promoteAdmin(HttpSession session,
							@RequestParam("grade") String grade) {
		
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		RequestGrade rg = mService.checkRequest(memberNo);
		if(rg != null) {
			return rg.getGrade();
		}else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("lNo", null);
			map.put("intro", null);
			map.put("content", null);
			map.put("memberNo", memberNo);
			map.put("grade", grade);
			int result = mService.reqPromote(map);
			if(result > 0) {
				return "success";
			}else {
				throw new MemberException("플래너 신청에 실패하였습니다");
			}
		}
	}
	
	//탈퇴
	@PostMapping("deleteMember.mp")
	@ResponseBody
	public String deleteMember(HttpSession session) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		Member m = new Member();
		m.setMemberNo(memberNo);
		//게시글 n
		//댓글 n
		//여행일정 n
		//리뷰 n
		//req_schedule 삭제
		//request_grade 삭제
		int mR = mService.deleteMember(m);
		int bR = mService.deleteBoard(m);
		int rR = mService.deleteReply(m);
		int sR = mService.deleteSchedule(m);
		int vR = mService.deleteReview(m);
		int rsR = mService.deleteReqSchedule(m);
		int rgR = mService.deleteReqGrade(m);
		
		if(mR > 0 && bR > 0 && rR > 0 && sR > 0 && vR > 0 && rsR >0 && rgR >0) {
			return "success";
		}else {
			return "fail";
		}
	}
	
	//프로필 사진 수정
	@PostMapping("uploadProfile.mp")
	@ResponseBody
	public String uploadProfile(HttpSession session, Model model,
								@ModelAttribute Image i,
			                    @RequestParam("file") MultipartFile file) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String fileId = null;
		//사진 업로드
		if(file != null && !file.isEmpty()) {
			try {
				fileId = gdService.uploadFile(file.getInputStream(), file.getOriginalFilename());
				i.setImageOriginName(file.getOriginalFilename());
				i.setImageRename(fileId);
				i.setImagePath("drive://files/" + fileId);
				i.setImageThum(2);
				i.setImageRefType("MEMBER");
				i.setImageRefNo(memberNo);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//프로필 유무 확인
		int checkProfile = mService.checkProfile(memberNo);
		String rename =i.getImageRename();
		int iResult = 0;
		if(checkProfile == 0) {
			iResult = mService.insertProfile(i);
			if(iResult > 0) {
				model.addAttribute("rename", rename);
				return "success0";
			}else {
				return "fail0";
			}
		}else {
			Image exist = mService.existFileId(memberNo) ;
			String existFileId = exist.getImageRename();
			try {
				gdService.deleteFile(existFileId);
				mService.deleteProfile(memberNo);
				iResult = mService.insertProfile(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(iResult > 0) {
				model.addAttribute("rename", rename);
				return "success1";
			}else {
				return "fail1";
			}
		}
		
	}
	
	@GetMapping("myPoint.mp")
	public String moveToMyPoint(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "myPoint";
	}

	@GetMapping("myPayPoint.mp")
	public String moveToMyPayPoint(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "myPayPoint";
	}

	@GetMapping("myPlan.mp")
	public String moveToMyPlan(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);	
		return "myPlan";
	}

	@GetMapping("detailReqPlan.mp")
	public String moveToDetailReqPlan(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "detailReqPlan";
	}

	@GetMapping("feedback.mp")
	public String moveToFeedback(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "feedback";
	}

	@GetMapping("myTripNote.mp")
	public String moveToMyTripNote(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "myTripNote";
	}

	@GetMapping("detailMyTripNote.mp")
	public String moveToDetailMyTripNote(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
		return "detailMyTripNote";
	}

	@GetMapping("myInquiry.mp")
	public String moveToMyInquiry(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
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
	public String moveToMyBoard(HttpSession session, Model model) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		String existFileId =  mService.existFileId(memberNo).getImageRename();
		model.addAttribute("rename", existFileId);
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
