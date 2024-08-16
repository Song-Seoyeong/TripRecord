package com.finalproject.triprecord.member.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.board.model.service.BoardService;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.Cancel;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.exception.MemberException;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.place.model.exception.PlaceException;
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
	        model.addAttribute("rename", "defaultImageName"); 
	    }
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
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    Image image = mService.existFileId(memberNo); 
	  
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
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
	public String moveToMyPoint(HttpSession session, Model model, HttpServletRequest req,
								@RequestParam(value="page", defaultValue="1") int currentPage ) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    Image image = mService.existFileId(memberNo); 
	  
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
	    
	    int pmListCount = mService.pmListCount(memberNo);
	    PageInfo pi = Pagination.getPageInfo(currentPage, pmListCount, 10);
	    
	    ArrayList<Payment> pmList = mService.getPaymentList(pi, memberNo);
	    Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
	    model.addAttribute("pi", pi);
	    model.addAttribute("pmList",pmList);
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
	        model.addAttribute("rename", "defaultImageName"); 
	    }
		
		ArrayList<Point> pList = mService.selectPointList();  
		
		Member loginUser = mService.getMember(memberNo);
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("pList", pList);
		return "myPayPoint";
	}

	@GetMapping("myPlan.mp")
	public String moveToMyPlan(@RequestParam(value="page", defaultValue="1") int page,
								HttpServletRequest request,
								HttpSession session, Model model) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		// 신청 리스트 불러오기
		int listCount = mService.getReqListCount(memberNo);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
		
		ArrayList<ReqSchedule> list = mService.getReqList(pi, memberNo);
		
		ArrayList<Planner> pList = new ArrayList<Planner>();
		
		for(ReqSchedule r : list) {
			// 신청별 플래너 정보
			Planner p = new Planner();
			p = mService.getReqPlanner(r.getReqPlaNo());
			pList.add(p);
			
			// 신청별 schedule
			Schedule sch = mService.getSchedule(r.getScheNo());
			
			String nightDay = getNightDay(sch.getScEndDate(), sch.getScStartDate());
			r.setNightDay(nightDay);
			
			SimpleDateFormat sp = new SimpleDateFormat("yy.MM.dd");
			r.setStartDay(sp.format(sch.getScStartDate()));
			r.setEndDay(sp.format(sch.getScEndDate()));
			
			// 포인트
			r.setPoint(getDayPoint(sch.getScEndDate(), sch.getScStartDate()));
			
			// 날짜 체크
			LocalDate localDate = sch.getScStartDate().toLocalDate();

	        // 오늘 날짜 가져오기
	        LocalDate today = LocalDate.now();

	        // 날짜 비교
	        if (localDate.isEqual(today)) {
	        	
	        }
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
    	model.addAttribute("pList", pList);
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
        
        return (int)(day*8000);
	}
	
	// 여행 기간 구하는 함수
	public String getNightDay(Date endDate, Date startDate) {
		// 두 날짜 사이의 시간 차이를 밀리초 단위로 계산
        long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());

        // 밀리초를 일(day) 단위로 변환
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

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
		//System.out.println(sch);
		
		// 여행 기간
		String nightDay = getNightDay(sch.getScEndDate(), sch.getScStartDate());
		rs.setNightDay(nightDay);
		
		// 출발/도착 일자
		SimpleDateFormat sp = new SimpleDateFormat("yy.MM.dd");
		rs.setStartDay(sp.format(sch.getScStartDate()));
		rs.setEndDay(sp.format(sch.getScEndDate()));
		
		// 포인트
		rs.setPoint(getDayPoint(sch.getScEndDate(), sch.getScStartDate()));
		
		// 취소 사유
		if(rs.getReqStatus() == 4) {
			Cancel cancel = mService.checkCancel(rs.getReqNo());
			model.addAttribute("cancel", cancel);
		}
		
		// 상세 일정 가져오기
		ArrayList<Plan> planList = mService.getPlanList(rs.getScheNo());
		
		
		// 사용자 프로필 사진
		Image image = mService.existFileId(memberNo); 
	  
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
	    
	    model.addAttribute("planList", planList);
	    model.addAttribute("rs", rs);
	    model.addAttribute("planner", planner);
	    model.addAttribute("sch", sch);
	    model.addAttribute("page", page);
	    return "detailReqPlan";
	}

	@GetMapping("feedback.mp")
	public String moveToFeedback(HttpSession session, Model model) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    Image image = mService.existFileId(memberNo); 
	  
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
		return "feedback";
	}
	
	@GetMapping("cancelReqSch.mp")
	public String updateReqSch(@ModelAttribute ReqSchedule req,
								@RequestParam("cancelContent") String cancelContent,
								@RequestParam("page") int page,
								HttpSession session, RedirectAttributes ra) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		// 신청진행상태 변경
		int result = mService.updateReqState(req);
		
		// 신청 취소 사유 저장
		Cancel c = new Cancel();
		c.setCancelRefNo(req.getReqNo());
		c.setCancelRefType("REQSCHEDULE");
		c.setCancelComent(cancelContent);
		c.setCancelMemNo(memberNo);
		int canResult = mService.insertCancel(c);
		
		if(result > 0 && canResult > 0) {
		// 프로필 이미지 조회
	    Image image = mService.existFileId(memberNo); 
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

	@GetMapping("myInquiry.mp")
	public String moveToMyInquiry(HttpSession session, Model model, HttpServletRequest req,
								  @RequestParam(value="page", defaultValue="1") int currentPage,
								  @RequestParam(value="generalType", defaultValue="ALL") String generalType,
								  @RequestParam(value="boardType", defaultValue="GENERAL") String boardType,
								  @RequestParam(value="localName", defaultValue="ALL") String localName) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Member loginUser = mService.getMember(memberNo);
	    Image image = mService.existFileId(memberNo); 
		
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
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
		
		if(!qList.isEmpty()) {
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

	@GetMapping("updateInquiry.mp")
	public String moveToUpdateInquiry() {
		return "updateInquiry";
	}

	@GetMapping("detailMyInquiry.mp")
	public String moveToDetailMyInquiry() {
		return "detailMyInquiry";
	}

	@GetMapping("myBoard.mp")
	public String moveToMyBoard(HttpSession session, Model model, HttpServletRequest req,
								@RequestParam(value="page", defaultValue="1") int currentPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		Member loginUser = mService.getMember(memberNo);
	    Image image = mService.existFileId(memberNo); 
	  
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
	    
	    CategorySelect cs = new CategorySelect();
		cs.setBoardType("GENERAL");
	    
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put("cs", cs);
		map.put("memberNo", memberNo);
		int listCount = mService.getListCount(map);
		System.out.println("제너럴보드 수 : " + listCount);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10); 
	    ArrayList<Board> aList = mService.getBoardList(map, pi);// GENERAL -> 동행 WITH, 양도 GIVE, 후기 REVIEW
	    System.out.println("제너럴 보드 데이터 : " + aList);
	  		
	    if(aList != null) {
	    	model.addAttribute("aList", aList); // 보드
	    }else {
	    	model.addAttribute("nothing", null);
	    }
	    
	    model.addAttribute("pi", pi);
		model.addAttribute("loc", req.getRequestURI());
	    model.addAttribute("loginUser", loginUser);
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
	public String moveToPlannerPage(HttpSession session, Model model,
									HttpServletRequest req,
									@RequestParam(value="page", defaultValue="1") int currentReviewPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("refType", "MEMBER");
		map.put("refNo", memberNo);
		//플래너 정보
		Planner planner = mService.getPalnner(memberNo);
		Local local = mService.getLocalName(memberNo);
		int likes = mService.countLikes(memberNo);
		Image image = mService.getImgRename(map);
		
		 if (image != null && image.getImageRename() != null) {
		        String existFileId = image.getImageRename(); 
		        model.addAttribute("rename", existFileId);
		    } else {
		        // 이미지가 없거나 리네임이 없는 경우 처리
		        model.addAttribute("rename", "defaultImageName"); 
		    }
		
		model.addAttribute("planner", planner);
		model.addAttribute("local", local);
		model.addAttribute("likes", likes);
		//플래너 소개
		
		
		//플래너 후기 + 별점
		Double avgStar = mService.averageStar(memberNo);
		int reviewlistCount = mService.getReviewListCount(memberNo);
		PageInfo pi = Pagination.getPageInfo(currentReviewPage, reviewlistCount, 5);
		ArrayList<Review> list = mService.getReviewList(pi, memberNo);
		
		model.addAttribute("pi",pi);
		model.addAttribute("avgStar", avgStar);
		model.addAttribute("rList",list);
		return "plannerPage";
	}
	
	@PostMapping("canclePlanner.mp")
	@ResponseBody
	public String canclePlanner(HttpSession session,
								@RequestParam("grade") String grade) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		RequestGrade rg = mService.checkRequest(memberNo);
		int result = 0;
		if(rg != null) {
			return rg.getGrade();
		}else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("memberNo", memberNo);
			map.put("grade", grade);
			result = mService.canclePlanner(map);
		}
		
		return result != 0 ? "success" : "fail";
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
