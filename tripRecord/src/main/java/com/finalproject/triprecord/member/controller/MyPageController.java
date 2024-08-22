package com.finalproject.triprecord.member.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.board.model.service.BoardService;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.Cancel;
import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.service.MatchingService;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.exception.MemberException;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Calculate;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.place.model.exception.PlaceException;
import com.finalproject.triprecord.plan.controller.PlanController;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {
	
	@Autowired
	MemberService mService;
	
	@Autowired
	BoardService bService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private GoogleDriveService gdService;
	
	@Autowired
	private MatchingService matService;
	
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
			model.addAttribute("noProfile", "noProfile");
		}
	
		Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
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
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Image image = mService.existFileId(memberNo);

		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}

		Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
		return "updateMyPwd";
	}

	// 내 비밀번호 수정
	@PostMapping("updatPwdOfMe.mp")
	@ResponseBody
	public String updatPwd(HttpSession session, Model model, @RequestParam("currentPwd") String pwd,
			@RequestParam("newPwd") String newPwd) {
		String id = ((Member) session.getAttribute("loginUser")).getMemberId();
		Member m = new Member();
		m.setMemberId(id);
		m.setMemberPwd(pwd);
		Member loginUser = mService.login(m);

		// 기존 비번과 새 비번 동일여부 확인 후 수정
		Member m2 = new Member();
		m2.setMemberPwd(newPwd);
		if (bcrypt.matches(m2.getMemberPwd(), loginUser.getMemberPwd())) {
			return "true";
		} else {
			if (bcrypt.matches(m.getMemberPwd(), loginUser.getMemberPwd())) {
				// 비번 수정
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("newPwd", bcrypt.encode(newPwd));

				int result = mService.updatPwdOfMe(map);
				if (result > 0) {

					return "false";
				} else {
					throw new MemberException("비밀번호가 수정에 실패하였습니다");
				}
			} else {
				throw new MemberException("비밀번호가 일치하지 않습니다");
			}
		}
	}

	@GetMapping("promoteToPlanner.mp")
	public String moveToPromoteToPlanner(HttpSession session, Model model) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Image image = mService.existFileId(memberNo);

		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}

		Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
		return "promoteToPlanner";
	}

	// 판매자 신청
	@PostMapping("promotion.mp")
	@ResponseBody
	public String promotion(HttpSession session, @RequestParam("region") int lNo,
			@RequestParam(value = "introProfile", required = false) String intro,
			@RequestParam(value = "submitContent", required = false) String content,
			@RequestParam("bank") String bank, @RequestParam("account") String account) {

		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();

		RequestGrade rg = mService.checkRequest(memberNo);
		if (rg != null) {
			return rg.getGrade();
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("lNo", lNo);
			map.put("intro", intro);
			map.put("content", content);
			map.put("memberNo", memberNo);
			map.put("grade", "PLANNER");
			map.put("bank", bank);
			map.put("account", account);
			int result = mService.reqPromote(map);
			if (result > 0) {
				return "success";
			} else {
				throw new MemberException("플래너 신청에 실패하였습니다");
			}
		}
	}

	// 관리자 요청
	@PostMapping("submitAdmin.mp")
	@ResponseBody
	public String promoteAdmin(HttpSession session, @RequestParam("grade") String grade) {

		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		RequestGrade rg = mService.checkRequest(memberNo);
		if (rg != null) {
			return rg.getGrade();
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("lNo", null);
			map.put("intro", null);
			map.put("content", null);
			map.put("memberNo", memberNo);
			map.put("grade", grade);
			int result = mService.reqPromote(map);
			if (result > 0) {
				return "success";
			} else {
				throw new MemberException("플래너 신청에 실패하였습니다");
			}
		}
	}

	// 탈퇴
	@PostMapping("deleteMember.mp")
	@ResponseBody
	public String deleteMember(HttpSession session) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Member m = new Member();
		m.setMemberNo(memberNo);
		// 게시글 n
		// 댓글 n
		// 여행일정 n
		// 리뷰 n
		// req_schedule 삭제
		// request_grade 삭제
		int mR = mService.deleteMember(m);
		int bR = mService.deleteBoard(m);
		int rR = mService.deleteReply(m);
		int sR = mService.deleteSchedule(m);
		int vR = mService.deleteReview(m);
		int rsR = mService.deleteReqSchedule(m);
		int rgR = mService.deleteReqGrade(m);

		if (mR > 0 && bR > 0 && rR > 0 && sR > 0 && vR > 0 && rsR > 0 && rgR > 0) {
			return "success";
		} else {
			return "fail";
		}
	}

	// 프로필 사진 수정
	@PostMapping("uploadProfile.mp")
	@ResponseBody
	public String uploadProfile(HttpSession session, Model model, @ModelAttribute Image i,
			@RequestParam("file") MultipartFile file) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		String fileId = null;
		// 사진 업로드
		if (file != null && !file.isEmpty()) {
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
		// 프로필 유무 확인
		int checkProfile = mService.checkProfile(memberNo);
		String rename = i.getImageRename();
		int iResult = 0;
		if (checkProfile == 0) {
			iResult = mService.insertProfile(i);
			if (iResult > 0) {
				model.addAttribute("rename", rename);
				return "success0";
			} else {
				return "fail0";
			}
		} else {
			Image exist = mService.existFileId(memberNo);
			String existFileId = exist.getImageRename();
			try {
				gdService.deleteFile(existFileId);
				mService.deleteProfile(memberNo);
				iResult = mService.insertProfile(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (iResult > 0) {
				model.addAttribute("rename", rename);
				return "success1";
			} else {
				return "fail1";
			}
		}

	}

	@GetMapping("myPoint.mp")
	public String moveToMyPoint(HttpSession session, Model model, HttpServletRequest req,
			@RequestParam(value = "page", defaultValue = "1") int currentPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Image image = mService.existFileId(memberNo);

		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}

		int pmListCount = mService.pmListCount(memberNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, pmListCount, 10);

		ArrayList<Payment> pmList = mService.getPaymentList(pi, memberNo);
		Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("pi", pi);
		model.addAttribute("pmList", pmList);
		model.addAttribute("loc", req.getRequestURI());

		return "myPoint";
	}

	@GetMapping("myPayPoint.mp")
	public String moveToMyPayPoint(HttpSession session, Model model) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Image image = mService.existFileId(memberNo);

		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}

		ArrayList<Point> pList = mService.selectPointList();

		Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("pList", pList);
		return "myPayPoint";
	}


	@GetMapping("myPlan.mp")
	public String moveToMyPlan(@RequestParam(value="page", defaultValue="1") int page,
								@RequestParam(value="statusSearch", required=false) Integer statusSearch,
								HttpServletRequest request,
								HttpSession session, Model model) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		// 신청 리스트 불러오기
		ReqSchedule rs = new ReqSchedule();
		rs.setReqMemNo(memberNo);
		if(statusSearch != null) {
			rs.setReqStatus(statusSearch);
		}else {
			statusSearch = 0;
		}
		//System.out.println(rs);
		int listCount = mService.getReqListCount(rs);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
		
		//System.out.println(pi);
		
		ArrayList<ReqSchedule> list = mService.getReqList(pi, rs);
		
		
		for(ReqSchedule r : list) {
			// 신청별 플래너 정보
			Planner p = new Planner();
			p = mService.getReqPlanner(r.getReqPlaNo());
			
			r.setNickname(p.getNickname());
			r.setLocalName(p.getLocalName());
			
			// 신청별 schedule
			Schedule sch = mService.getSchedule(r.getScheNo());
			
			String nightDay = getNightDay(sch.getScEndDate(), sch.getScStartDate());
			r.setNightDay(nightDay);
			
			SimpleDateFormat sp = new SimpleDateFormat("yy.MM.dd");
			r.setStartDay(sp.format(sch.getScStartDate()));
			r.setEndDay(sp.format(sch.getScEndDate()));
			r.setPayPoint((int)r.getPayPoint());
		}
		//System.out.println(list);
	    //프로필 사진
		
	    Image image = mService.existFileId(memberNo); 
	    
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
    	model.addAttribute("list", list);
    	model.addAttribute("statusSearch", statusSearch);
    	model.addAttribute("pi", pi);
    	model.addAttribute("loc", request.getRequestURI());
		return "myPlan";
	}
	
	// 기간별 포인트 구하는 함수
	public int getDayPoint(Date endDate, Date startDate) {
		// 두 날짜 사이의 시간 차이를 밀리초 단위로 계산
        long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());

        // 밀리초를 일(day) 단위로 변환
        long day = diffInMillies / (24 * 60 * 60 * 1000);
        day = day + 1;
        return (int)(day*8000);
	}
	
	// 여행 기간 구하는 함수
	public String getNightDay(Date endDate, Date startDate) {
		// 두 날짜 사이의 시간 차이를 밀리초 단위로 계산
        long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());

        // 밀리초를 일(day) 단위로 변환
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
        diffInDays = diffInDays + 1;
        // 박(숙박 횟수) 계산: 1박은 1일을 제외한 일수로 계산
        long nights = diffInDays - 1;
        long days = diffInDays;
        
        return nights + "박" + days + "일";
	}

	@GetMapping("detailReqPlan.mp")
	public String moveToDetailReqPlan(@RequestParam("reqNo")int reqNo,
									  @RequestParam("page") int page,
									   HttpSession session, Model model) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    
		// 상세 요청 가져오기
		ReqSchedule rs = mService.getReqSchedule(reqNo);
		Planner planner = mService.getReqPlanner(rs.getReqPlaNo());
		Image i = mService.existFileId(planner.getMemberNo());
		
		if(i != null) 
			planner.setImageRename(i.getImageRename());
		
		Schedule sch = mService.getSchedule(rs.getScheNo());
		//System.out.println(rs);
		
		// 플랜 데이트 가져오기
		PlanController pc = new PlanController();
		SimpleDateFormat sp = new SimpleDateFormat("yyyy/MM/dd");
		HashMap<Integer, String> dates = pc.dateFunction(sp.format(sch.getScStartDate()), sp.format(sch.getScEndDate()));
		
		// 여행 기간
		String nightDay = getNightDay(sch.getScEndDate(), sch.getScStartDate());
		rs.setNightDay(nightDay);
		
		// 출발/도착 일자
		SimpleDateFormat sp2 = new SimpleDateFormat("yy.MM.dd");
		rs.setStartDay(sp2.format(sch.getScStartDate()));
		rs.setEndDay(sp2.format(sch.getScEndDate()));
		
		// 취소 사유
		if(rs.getReqStatus() == 4) {
			Cancel cancel = mService.checkCancel(rs);
			model.addAttribute("cancel", cancel);
			//System.out.println(cancel);
		}
		
		// 상세 일정 가져오기
		ArrayList<Plan> planList = mService.getPlanList(rs.getScheNo());
		
		for(Plan plan:planList) {
			plan.setDay(plan.getDay().split(" ")[0]);
		}
		//System.out.println(planList);
		
		
		// 리뷰 여부 가져오기
		int reviewCount = 0;
		if(rs.getReqStatus() == 4 || rs.getReqStatus() == 3) {
			reviewCount = mService.checkReviewCount(rs);
		}
		
		// 사용자 프로필 사진
		Image image = mService.existFileId(memberNo); 
	  
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
	    
	    model.addAttribute("rCount", reviewCount);
	    model.addAttribute("pList", planList);
	    model.addAttribute("rs", rs);
	    model.addAttribute("planner", planner);
	    model.addAttribute("sch", sch);
	    model.addAttribute("dates", dates);
	    model.addAttribute("page", page);
	    return "detailReqPlan";
	}
	
	@GetMapping("cancelReqSch.mp")
	public String updateReqSch(@ModelAttribute ReqSchedule req,
								@RequestParam("cancelContent") String cancelContent,
								@RequestParam("page") int page,
								HttpSession session, RedirectAttributes ra) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		// 신청진행상태 변경
		int result = mService.updateReqState(req);
		
		// 신청 취소 사유 저장
		Cancel c = new Cancel();
		c.setCancelRefNo(req.getReqNo());
		c.setCancelRefType("REQSCHEDULE");
		c.setCancelComent(cancelContent);
		c.setCancelMemNo(loginUser.getMemberNo());
		int canResult = mService.insertCancel(c);
		
		req.setReqMemNo(loginUser.getMemberNo());
		// 포인트 환불
		mService.refundPoint(req);
		
		loginUser.setMemberPoint(loginUser.getMemberPoint() + req.getPayPoint());
		session.setAttribute("loginUser", loginUser);
		
		if(result > 0 && canResult > 0) {
			// 프로필 이미지 조회
		    Image image = mService.existFileId(loginUser.getMemberNo()); 
			if (image != null && image.getImageRename() != null) {
		        String existFileId = image.getImageRename(); 
		        ra.addAttribute("rename", existFileId);
		    } else {
		        // 이미지가 없거나 리네임이 없는 경우 처리
		        ra.addAttribute("rename", "defaultImageName"); 
		    }
			ra.addAttribute("page", page);
			return "redirect:myPlan.mp";
		
		}else {
			throw new PlaceException("신청 내역 취소 중 에러 발생");
		}
	}
	
	@GetMapping("reqConfirm.mp")
	public String reqConfirm(@ModelAttribute ReqSchedule r,
							@RequestParam("page") int page,
							HttpSession session, RedirectAttributes ra) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		r = mService.getReqSchedule(r.getReqNo());
		
		r.setReqMemNo(loginUser.getMemberNo());
		r.setReqStatus(3);
		
		// 신청 상태 변경
		mService.updateReqState(r);
		mService.updateReqConfirmDate(r);
		
		// 스케줄 작성자 변경
		mService.updateScheduleWriter(r);
		
		// 플래너 정산에 추가
		mService.insertCalcultate(r);
	
    	ra.addAttribute("page", page);
    	ra.addAttribute("reqNo", r.getReqNo());
    	return "redirect:detailReqPlan.mp";
	}

	@GetMapping("myInquiry.mp")
	public String moveToMyInquiry(HttpSession session, Model model, HttpServletRequest req,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "generalType", defaultValue = "ALL") String generalType,
			@RequestParam(value = "boardType", defaultValue = "GENERAL") String boardType,
			@RequestParam(value = "localName", defaultValue = "ALL") String localName) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Member loginUser = mService.getMember(memberNo);
		Image image = mService.existFileId(memberNo);

		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}

		CategorySelect cs = new CategorySelect();
		cs.setBoardType("QUESTION");
		// GENERAL, QUESTION, NOTICE -> 일반, 문의, 공지

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cs", cs);
		map.put("memberNo", memberNo);
		map.put("i", 0);
		int listCount = mService.getListCount(map);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Question> qList = mService.getQuestionList(map); // 0 보내면 Question 전체리스트, 숫자 보내면 해당 번호 가져오기

		if (!qList.isEmpty()) {
			model.addAttribute("qList", qList); // 문의(글번호, 비번, 답변, 답변YN)
//			model.addAttribute("listCount", listCount);
//			model.addAttribute("generalType","ALL");
		} else {
			model.addAttribute("nothing", null);
		}
		model.addAttribute("pi", pi);
		model.addAttribute("loc", req.getRequestURI());
		model.addAttribute("loginUser", loginUser);
		return "myInquiry";
	}

	
	@GetMapping("myBoard.mp")
	public String moveToMyBoard(HttpSession session, Model model, HttpServletRequest req,
								@RequestParam(value = "page", defaultValue = "1") int currentPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Member loginUser = mService.getMember(memberNo);
		Image image = mService.existFileId(memberNo);

		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}

		CategorySelect cs = new CategorySelect();
		cs.setBoardType("GENERAL");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cs", cs);
		map.put("memberNo", memberNo);
		int listCount = mService.getListCount(map);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Board> aList = mService.getBoardList(map, pi);// GENERAL -> 동행 WITH, 양도 GIVE, 후기 REVIEW

		if (aList != null) {
			model.addAttribute("aList", aList); // 보드
		} else {
			model.addAttribute("nothing", null);
		}
		model.addAttribute("pi", pi);
		model.addAttribute("loc", req.getRequestURI());
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("wholeReview", "none");
		return "myBoard";
	}
	
	@GetMapping("myReview.mp")
	public String moveToMyReview(HttpSession session, Model model, HttpServletRequest req,
			@RequestParam(value = "page", defaultValue = "1") int currentPage) {
		
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Member loginUser = mService.getMember(memberNo);
		Image image = mService.existFileId(memberNo);

		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}
		
		int listCount = mService.getWholeReviewListCount(memberNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Review> rList = mService.getWholeReviewList(memberNo, pi);
		
		if (rList != null) {
			model.addAttribute("rList", rList); // review
			model.addAttribute("wholeReview", "wholeReview");
		} else {
			model.addAttribute("nothing", null);
		}
		model.addAttribute("pi", pi);
		model.addAttribute("loc", req.getRequestURI());
		model.addAttribute("loginUser", loginUser);
		return "myBoard";
	}

	//////////플래너 페이지 ////////// 
	@GetMapping("plannerPage.mp")
	public String moveToPlannerPage(HttpSession session, Model model, HttpServletRequest req,
									@RequestParam(value = "page", defaultValue = "1") int currentReviewPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("refType", "MEMBER");
		map.put("refNo", memberNo);
		// 플래너 정보
		Planner planner = mService.getPlanner(memberNo);
		Local local = mService.getLocalName(memberNo);
		int likes = mService.countLikes(memberNo);
		Image image = mService.getImgRename(map);
	
		if (image != null && image.getImageRename() != null) {
			String existFileId = image.getImageRename();
			model.addAttribute("rename", existFileId);
		} else {
			// 이미지가 없거나 리네임이 없는 경우 처리
			model.addAttribute("noProfile", "noProfile");
		}
	
		model.addAttribute("planner", planner);
		model.addAttribute("local", local);
		model.addAttribute("likes", likes);
		// 플래너 소개
	
		// 플래너 후기 + 별점
		Double avgStar = mService.averageStar(memberNo);
		int reviewlistCount = mService.getReviewListCount(memberNo);
		PageInfo pi = Pagination.getPageInfo(currentReviewPage, reviewlistCount, 5);
		ArrayList<Review> list = mService.getReviewList(pi, memberNo);
		model.addAttribute("pi", pi);
		model.addAttribute("avgStar", avgStar);
		model.addAttribute("rList", list);
		// 플래너 해쉬태그 불러오기
		ArrayList<HashTag> tagList = mService.getHashTag(memberNo);
		model.addAttribute("tagList", tagList);
	
		// 플래너 소개 이미지 불러오기
		Image exist = mService.existPlannerFileId(memberNo);
		if (exist == null) {
			model.addAttribute("plannerIntroImage", "noImg");
		} else {
			model.addAttribute("plannerIntroImage", exist.getImageRename());
		}
		
		ArrayList<Image> rImgList = matService.selectrImgList();
		model.addAttribute("rImgList", rImgList);
		
		Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
		return "plannerPage";
	
	}
	
	@PostMapping("cancelPlanner.mp")
	@ResponseBody
	public String cancelPlanner(HttpSession session, @RequestParam("grade") String grade) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Planner planner = mService.getPlanner(memberNo);
		RequestGrade rg = mService.checkRequest(memberNo);
		int result = 0;
		if (rg != null) {
			return rg.getGrade();
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("memberNo", memberNo);
			map.put("grade", grade);
			map.put("lNo", planner.getLocalNo());
			map.put("account", planner.getAccount());
			map.put("bank", planner.getBank());
			result = mService.cancelPlanner(map);
		}

		return result > 0 ? "success" : "fail";
	}
	
	@GetMapping("request.mp")
	public String moveToRequest(@RequestParam(value="page", defaultValue="1") int currentPage, Model model, HttpSession session){
		int pNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		int listCount = mService.getRequestListCount(pNo);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<ReqSchedule> reqList = mService.selectRequestList(pNo, pi);
		
		for(ReqSchedule r : reqList) {
			String StartDay = (r.getStartDay()).split(" ")[0];
			r.setStartDay(StartDay);
			String EndDay = (r.getEndDay()).split(" ")[0];
			r.setEndDay(EndDay);
		}
		
		model.addAttribute("pi", pi);
		model.addAttribute("reqList", reqList);
		return "request";
	}

//	상세 일정 보기
	@GetMapping("detailRequest.mp")
	public String moveToDetailRequest(@RequestParam("reqNo") int reqNo, /* , @RequestParam("page") int page, */
			Model model, HttpSession session) {
		// 플래너 정보 받아오기
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Planner planner = plannerInfo(memberNo);

		model.addAttribute("planner", planner);

		HashMap<String, Integer> map = new HashMap<>();
		map.put("reqPlaNo", memberNo);
		map.put("reqNo", reqNo);

		ReqSchedule rs = mService.detailRequest(map);

		if (rs != null) {
			model.addAttribute("rs", rs);

			Schedule s = mService.detailSchedule(rs.getScheNo()); // mapper 에서 날짜는 to_char 로 받아옴
			if (s != null) {
				model.addAttribute("s", s);
				Member reqMem = mService.getMember(rs.getReqMemNo());
				if (reqMem != null) {
					model.addAttribute("reqMem", reqMem);
					return "detailRequest";
				} else {
					throw new MemberException("요청 신청자 불러오기에 실패하였습니다.");
				}
			} else {
				throw new MemberException("요청 일정 불러오기에 실패하였습니다.");
			}
		} else {
			throw new MemberException("요청 상세 내역 불러오기에 실패하였습니다.");
		}

	}

	// 플래너 정보, 지역 가져오기
	public Planner plannerInfo(int memberNo) {
		Planner planner = mService.getPlanner(memberNo);
		Local local = mService.getLocalName(memberNo);
		planner.setLocalName(local.getLocalName());

		return planner;
	}

	// 플래너 요청 보기 -> 상세 보기 -> 진행 버튼
	@GetMapping("processRequest.mp")
	public String moveToProcessRequest(@RequestParam("reqNo") int reqNo, Model model, HttpSession session) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Planner planner = plannerInfo(memberNo);

		HashMap<String, Integer> map = new HashMap<>();
		map.put("reqPlaNo", memberNo);
		map.put("reqNo", reqNo);

		ReqSchedule rs = mService.detailRequest(map); // 요청 보기에서 사용한 reqSchedule 가져오는 메소드

		if (rs != null) {
			Member reqMem = mService.getMember(rs.getReqMemNo());
			Schedule s = mService.detailSchedule(rs.getScheNo()); // 요청 보기에서 사용한 schedule 가져오는 메소드

			if (s != null && reqMem != null) {

				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				LocalDate start = LocalDate.parse(s.getStartDate(), format);
				LocalDate end = LocalDate.parse(s.getEndDate(), format);

				HashMap<Integer, String> dates = new HashMap<>();

				int day = 1;
				for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
					dates.put(day, date + "");
					day++;
				}

				model.addAttribute("rs", rs);
				model.addAttribute("s", s);
				model.addAttribute("dates", dates);
				model.addAttribute("reqMem", reqMem);
				model.addAttribute("planner", planner);
				return "processRequest";
			} else {
				throw new MemberException("요청 일정 정보 불러오기에 실패하였습니다.");
			}
		} else {
			throw new MemberException("요청 진행 정보 불러오기에 실패하였습니다.");
		}
	}
	
	// 제출 버튼 누른 곳
	@PostMapping("requestEnd.mp")
	public String moveToRequestEnd(@RequestParam("scNo") int scNo, @RequestParam("reqNo") int reqNo, @RequestParam("reqMemo") String reqMemo, 
									@ModelAttribute Plan p, HttpSession session, @RequestParam("count") String count) {
//		
//		// 밑에 있는 것들은 전부 , 로 이어져서 들어오기 때문에 split 사용하였음, 밑에서 배열 돌려야 함
		String place[] = p.getPlace().split(",");
		String time[] = p.getTime().split(",");
		String memo[] = p.getMemo().split(",");
		String day[] = p.getDay().split(","); // 계획 1개에 해당하는 day
//		// 16일 계획 2개, 17일 계획 1개라면 2024-08-16, 2024-08-16, 2024-08-17... (중복되는 거 맞음)
//
		String cStr[] = count.split(";"); // count 안에 한 날짜에 몇 개 담겼는지 += 구분자(;) 로 들어옴. 16일 계획 2개, 17일 계획 1개라면 2, 1
		
		Integer coNum[] = new Integer[cStr.length]; // 날짜만큼 plan 만들기 위해서 Integer 숫자 하나 만듬
		for (int i = 0; i < cStr.length; i++) {
			coNum[i] = Integer.parseInt(cStr[i]); // cStr 이랑 같은 값 들어있는데 타입만 다름
		}
		
		ArrayList<Plan> plList = new ArrayList<Plan>(); // 계획 1개씩 담을 ArrayList
		for (int i = 0, coCount = 0, dNum = 0; i < place.length; i++, coCount++) { // 장소는 무조건 있어야 하는 값 중 하나라
																					// place.length 만큼 돌림
			Plan pl = new Plan();
			pl.setPlNo(i); // 이건 그냥 넣었어요
			pl.setPlace(place[i]); // 장소 소매넣기
			pl.setTime(time[i]); // 시간 소매넣기
			pl.setScNo(scNo);

			if (memo[i].equals("-")) { // 메모 안 써있으면 - 로 값 들어옴
				pl.setMemo(null); // null 허용해 둠
			} else {
				pl.setMemo(memo[i]);
			}

			if (coCount == coNum[dNum]) {
				coCount = 0;
				dNum++;
			}
			pl.setDay(day[dNum]);

			plList.add(pl);
		}
		
		int result = mService.reqPlanInsert(plList);
		
		if (result > 0) {
			ReqSchedule rs = new ReqSchedule();
			rs.setReqNo(reqNo);
			rs.setMemo(reqMemo);
			
			result = mService.reqScheUpdate(rs);
			if(result > 0) {
				
				return "requestEnd";
			} else {
				throw new MemberException("일정 요청 정보 업데이트에 실패하였습니다.");
			}
		} else {
			throw new MemberException("일정 저장에 실패하였습니다.");
		}

	}
	
	// 요청 거절
	@GetMapping("cancelRequest.mp")
	public String cancelRequest(@ModelAttribute ReqSchedule req, @RequestParam("cancelContent") String cancelContent,
				/*@RequestParam("page") int page,*/ HttpSession session, RedirectAttributes ra) {
		Member loginUser = (Member) session.getAttribute("loginUser");

		// 신청진행상태 변경
		int result = mService.updateReqState(req);
		if(result > 0) {
			// 신청 취소 사유 저장
			Cancel c = new Cancel();
			c.setCancelRefNo(req.getReqNo());
			c.setCancelRefType("REQSCHEDULE");
			c.setCancelComent(cancelContent);
			c.setCancelMemNo(loginUser.getMemberNo());
			
			int canResult = mService.insertCancel(c);
			
			if(canResult > 0) {
				req.setReqMemNo(loginUser.getMemberNo());
				mService.refundPoint(req); // 포인트 환불
				loginUser.setMemberPoint(loginUser.getMemberPoint() + req.getPayPoint());
				session.setAttribute("loginUser", loginUser);
				
				result = mService.scDelStaUpdate(req.getScheNo());
				
				return "redirect:request.mp"; 
			} else {
				throw new MemberException("취소 과정 진행 중 오류가 발생하였습니다.");
			}
		} else {
			throw new MemberException("취소 과정 진행 중 오류가 발생하였습니다.");
		}
	}

	@GetMapping("sales.mp")
	public String moveToSales(HttpSession session, Model model, HttpServletRequest req,
							  @RequestParam(value = "page", defaultValue = "1") int currentPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		int calcListCount = mService.calcListCount(memberNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, calcListCount, 10);
		ArrayList<Calculate> sList = mService.getCalcList(pi, memberNo);

//		Member loginUser = mService.getMember(memberNo);
//		model.addAttribute("loginUser", loginUser);
		model.addAttribute("pi", pi);
		model.addAttribute("loc", req.getRequestURI());
		model.addAttribute("sList", sList);
		return "sales";
	}

	@GetMapping("updatePlanner.mp")
	public String moveToUpdatePalnner(HttpSession session, Model model) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Planner planner = mService.getPlanner(memberNo);
		ArrayList<HashTag> tagList = mService.getHashTag(memberNo);
		ArrayList<HashTag> list = mService.getTags();
		model.addAttribute("planner", planner);
		model.addAttribute("tagList", tagList);
		model.addAttribute("list", list);
		
		Image exist = mService.existPlannerFileId(memberNo);
		if (exist == null) {
			model.addAttribute("plannerIntroImage", "noImg");
		} else {
			model.addAttribute("plannerIntroImage", exist.getImageRename());
		}

		return "updatePlanner";
	}

	@PostMapping("upatePlannerProfile.mp")
	@ResponseBody
	public String upatePlannerProfile(HttpSession session, Model model, @ModelAttribute Image i,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "lNo", required = false) int lNo,
			@RequestParam(value = "intro", required = false) String intro,
			@RequestParam(value = "introImgValue", required = false) String introImgValue,
			@RequestParam(value="hashTags",required = false) ArrayList<Integer> hashTags,
			@RequestParam(value="bank", required=false) String bank,
			@RequestParam(value="account", required=false) String account,
			@RequestParam(value="introProfile", required=false) String sIntroContent) throws IOException {
		Planner planner = new Planner();
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		HashMap<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("refNo", memberNo);
		String fileId = null;
		Image exist = null;
		String existFileId = null;
		int hashTag = 0;
		//계좌 수정
		if(bank == null || "null".equals(bank) || "none".equals(bank) || account.isEmpty() || account.isBlank() || "null".equals(account) || account.length() <= 9) {
			return "bankEmpty";
		}else if(lNo == 0){
			return "localEmpty";
		}
		else{
			pMap.put("bank", bank);
			pMap.put("account", account);
			mService.updateAccount(pMap);
			
			// 지역, 소개글 수정
			planner.setMemberNo(memberNo);
			planner.setIntroContent(intro);
			planner.setLocalNo(lNo);
			planner.setSIntroContent(sIntroContent);
			mService.updatePlannerIntro(planner);
			//해쉬태그
			if(hashTags != null && !hashTags.isEmpty()) {
				mService.deleteTag(memberNo);
				pMap.put("refType", "PLANNER");
				for(int h = 0; h < hashTags.size(); h++) {
					hashTag = hashTags.get(h);
					pMap.put("tagNo", hashTag);
					mService.updateTag(pMap);
				}
			}else if(hashTags == null || hashTags.isEmpty()){
				mService.deleteTag(memberNo);
			}
			// 사진 업로드
			if (file != null && !file.isEmpty()) {
					fileId = gdService.uploadFile(file.getInputStream(), file.getOriginalFilename());
					i.setImageOriginName(file.getOriginalFilename());
					i.setImageRename(fileId);
					i.setImagePath("drive://files/" + fileId);
					i.setImageThum(2);
					i.setImageRefType("PLANNER");
					i.setImageRefNo(memberNo);
			} else {
				//기존 이미지 선택 시 지워지는거
				if (introImgValue.equals("selected")) {
					exist = mService.existPlannerFileId(memberNo);
					existFileId = exist.getImageRename();
					gdService.deleteFile(existFileId);
					mService.deletePlannerProfile(memberNo);
					
					planner.setMemberNo(memberNo);
					planner.setIntroContent(intro);
					planner.setLocalNo(lNo);
					planner.setSIntroContent(sIntroContent);
					mService.updatePlannerIntro(planner);
				//기존 이미지 안 지우는거
				}else {
					planner.setMemberNo(memberNo);
					planner.setIntroContent(intro);
					planner.setLocalNo(lNo);
					planner.setSIntroContent(sIntroContent);
					mService.updatePlannerIntro(planner);
				}
				return "success3";
			}

			// 소개 사진 유무 확인 후 업데이트
			int result1 = mService.checkIntroImg(memberNo);
			int iResult = 0;
			if (result1 == 0) {
				iResult = mService.insertProfile(i);
				if (iResult > 0) {
					return "success0";
				} else {
					return "fail0";
				}
			} else {
				exist = mService.existPlannerFileId(memberNo);
				existFileId = exist.getImageRename();
				
					gdService.deleteFile(existFileId);
					mService.deletePlannerProfile(memberNo);
					iResult = mService.insertProfile(i);
				
				if (iResult > 0) {
					return "success1";
				} else {
					return "fail1";
				}
			}
		}
	}
	
	// 마이페이지 -> 내 일정 상세 보기 -> 수정
	@GetMapping("updateDetailTripLocal.mp")
	@ResponseBody
	public String updateTripLocal(@RequestParam("scNo") String scNo, @RequestParam("spot") String scLocalNo) {
		Properties prop = new Properties();
		prop.setProperty("scNo", scNo);
		prop.setProperty("scLocalNo", scLocalNo);
		
		int result = mService.updateTripLocal(prop);
		return result > 0 ? "success" : "fail";
	}
}