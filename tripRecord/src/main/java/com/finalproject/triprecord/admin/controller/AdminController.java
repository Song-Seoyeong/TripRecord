package com.finalproject.triprecord.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.finalproject.triprecord.admin.model.exception.AdminException;
import com.finalproject.triprecord.admin.model.service.AdminService;
import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.Content;
import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
import com.finalproject.triprecord.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private AdminService aService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private GoogleDriveService gdService;
	
	/** 대시보드 */
	// 대시보드 페이지 이동
	@GetMapping("dashBoard.ad")
	public String dashBoardView(Model model) {
		int totalCount = aService.selectMemberTotalCount();
		int generalCount = aService.selectMemberGradeCount("GENERAL");
		int plannerCount = aService.selectMemberGradeCount("PLANNER");
		int adminCount = aService.selectMemberGradeCount("ADMIN");
		int noAnswerCount = aService.selectQuestionAnswerCount("N");
		int gradeCount = aService.selectGradeCount();
		ArrayList<Content> placeCount = aService.selectPlacesCount();
		
		Point total = aService.selectTotalPoint();
		
		TreeMap<String, Integer> monthStats = new TreeMap<String, Integer>();
		TreeMap<String, Integer> dayStats = new TreeMap<String, Integer>();
		
		ArrayList<Payment> mpList = aService.selectMonthStats();
		String date;
		for(Payment p: mpList) {
			date = String.valueOf(p.getPayDate()).split("-")[1];
			
			if(monthStats.containsKey(date)){
				monthStats.put(date, monthStats.get(date) + p.getPoPrice());
			} else {
				monthStats.put(date, p.getPoPrice());
			}
		}
		
		ArrayList<Payment> dpList = aService.selectMonthStats();
		String day;
		for(Payment p: dpList) {
			day = String.valueOf(p.getPayDate()).split("-")[2];
			if(dayStats.containsKey(day)){
				dayStats.put(day, dayStats.get(day) + p.getPoPrice());
			} else {
				dayStats.put(day, p.getPoPrice());
			}
		}
		
		String loges = "";
		try {
			loges = gdService.selectLogFile("login.txt");
		} catch (IOException e) {
			
		}
		
		TreeMap<String, Integer> dateCount = new TreeMap<String, Integer>();
		
		
		for(String log : loges.split("\n")) {
			if(log.contains("[INFO]")) {
				String str = log.split(" ")[0];
				
				if(dateCount.containsKey(str)) {
					dateCount.put(str, dateCount.get(str) + 1);
				} else {
					dateCount.put(str, 1);
				}
			}
		}
		model.addAttribute("noAnswerCount", noAnswerCount);
		model.addAttribute("gradeCount", gradeCount);
		model.addAttribute("datedata", dateCount);
		model.addAttribute("monthStats", monthStats);
		model.addAttribute("dayStats", dayStats);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("generalCount", generalCount);
		model.addAttribute("plannerCount", plannerCount);
		model.addAttribute("adminCount", adminCount);
		model.addAttribute("placeCount", placeCount);
		model.addAttribute("total", total);
		return "dashBoard";
	}
	
	/** 회원 */
	// 회원 관리 페이지 이동
	@GetMapping("memberManage.ad")
	public String userManageView(Model model) {
		int totalCount = aService.selectMemberTotalCount();
		int generalCount = aService.selectMemberGradeCount("GENERAL");
		int plannerCount = aService.selectMemberGradeCount("PLANNER");
		int adminCount = aService.selectMemberGradeCount("ADMIN");
		
		ArrayList<Member> mList = aService.selectMemberList();
		
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("generalCount", generalCount);
		model.addAttribute("plannerCount", plannerCount);
		model.addAttribute("adminCount", adminCount);
		model.addAttribute("mList", mList);
		return "memberManage";
	}
	
	// 회원 상태 변경 + 강제 탈퇴
	@GetMapping("changeMemberStatus.ad")
	@ResponseBody
	public String changeMemberStatus(@ModelAttribute Member m) {
		if(m.getStatus().equals("Y")) {
			m.setMemberDrop("N");
		} else {
			m.setMemberDrop("Y");
		}
		
		int result = aService.changeMemberStatus(m);
		
		if(result > 0) {
			return "success";
		} else {
			throw new AdminException("회원 상태 변경을 실패하였습니다.");
		}
	}
	
	
	/** 문의사항 */
	// 문의사항 관리 페이지 이동
	@GetMapping("questManage.ad")
	public String questManageView(Model model) {
		int totalCount = aService.selectQuestionTotalCount();
		int answerCount = aService.selectQuestionAnswerCount("Y");
		int noAnswerCount = aService.selectQuestionAnswerCount("N");
		ArrayList<Question> qList = aService.selectQuestionList();
		
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("answerCount", answerCount);
		model.addAttribute("noAnswerCount", noAnswerCount);
		model.addAttribute("qList", qList);
		return "questManage";
	}
	
	// 문의사항 선택
	@GetMapping("selectQuest.ad")
	@ResponseBody
	public Question selectQuest(@RequestParam("qNo") int qNo) {
		Question question = aService.selectQuestion(qNo);
		
		return question;
	}
	
	// 문의사항 작성
	@PostMapping("insertAnswer.ad")
	public String insertAnswer(@ModelAttribute Question q) {
		int result = aService.insertAnswer(q);
		if(result > 0) {
			return "redirect:questManage.ad";
		} else {
			throw new AdminException("문의사항 답변 작성을 실패하였습니다.");
		}
	}
	
	
	/** 등급 */
	// 등급 관리 페이지 이동
	@GetMapping("gradeManage.ad")
	public String gradeManageView(Model model) {
		ArrayList<Member> pList = aService.selectPlannerList();
		ArrayList<Member> aList = aService.selectAdminList();
		ArrayList<RequestGrade> rList = aService.selectRequestGradeList();

		model.addAttribute("pList", pList);
		model.addAttribute("aList", aList);
		model.addAttribute("rList", rList);
		return "gradeManage";
	}
	
	
	// 일반 등급으로 전환
	@GetMapping("updateGrade.ad")
	public String updateGrade(@RequestParam("mNo") int mNo) {
		int result = aService.updateGrade(mNo);
		
		if(result > 0) {
			aService.deleteGrade(mNo);
			return "redirect:gradeManage.ad";
		} else {
			throw new AdminException("등급 전환에 실패하였습니다.");
		}
	}
	
	// 등급 취소 요청 시
	@GetMapping("gradeDownSuccess.ad")
	@ResponseBody
	public String gradeDownSuccess(@RequestParam("mNo") int mNo) {
		int result = aService.gradeDownSuccess(mNo);
		
		
		if(result > 0) {
			aService.deleteRequestGrade(mNo);
			return "success";
		} else {
			throw new AdminException("등급 취소를 실패하였습니다.");
		}
	}
	
	// 등급 요청 시
	@GetMapping("gradeSuccess.ad")
	@ResponseBody
	public String gradeSuccess(@RequestParam("mNo") int mNo) {
		RequestGrade rg = aService.selectRequestGrade(mNo);
		
		if(rg != null) {
			String grade = rg.getGrade();
			
			Member request = new Member();
			request.setMemberNo(mNo);
			request.setGrade(grade);
			
			int result = aService.gradeSuccess(request);
			
			if(result > 0) {
				if(grade.equals("PLANNER")) {
					aService.insertPlanner(rg);
				}
				aService.deleteRequestGrade(mNo);
				return "success";
			} else {
				throw new AdminException("등급 승인을 실패하였습니다.");
			}
			
		} else {
			throw new AdminException("회원 정보가 없습니다.");
		}
		
	}
	
	// 등급 요청 취소
	@GetMapping("gradeFail.ad")
	public String gradeFail(@RequestParam("mNo") int mNo) {
		aService.deleteRequestGrade(mNo);
		
		return "redirect:gradeManage.ad";
	}
	
	
	/** 공지사항 */
	// 공지사항 관리 페이지 이동, 검색 페이지 이동
	@GetMapping("noticeManage.ad")
	public String noticeManageView(@RequestParam(value="search", required=false) String search,
								   Model model) {
		ArrayList<Board> nList = aService.selectNoticeList(search);
		
		model.addAttribute("nList", nList);
		return "noticeManage";
	}
	
	// 공지사항 작성
	@PostMapping("insertNotice.ad")
	public String insertNotice(@ModelAttribute Board b,
							   HttpSession session) {
		int memberNo = ((Member)session.getAttribute("loginUser")).getMemberNo();
		
		b.setBoardWriterNo(memberNo);
		int result = aService.insertNotice(b);
		
		if(result > 0) {
			return "redirect:noticeManage.ad";
		} else {
			throw new AdminException("공지사항 작성을 실패하였습니다.");
		}
	}
	
	// 공지사항 선택
	@GetMapping("selectNotice.ad")
	@ResponseBody
	public Board selectNotice(@RequestParam("boardNo") int boardNo) {
		Board notice = aService.selectNotice(boardNo);
		
		return notice;
	}
	
	
	// 공지사항 수정
	@PostMapping("updateNotice.ad")
	public String updateNotice(@ModelAttribute Board b) {
		int result = aService.updateNotice(b);
		
		if(result > 0) {
			return "redirect:noticeManage.ad";
		} else {
			throw new AdminException("공지사항 수정을 실패하였습니다.");
		}
	}
	
	// 공지사항 삭제
	@GetMapping("deleteNotice.ad")
	public String deleteNotice(@RequestParam("boardNo") int boardNo) {
		int result = aService.deleteNotice(boardNo);
		
		if(result > 0) {
			return "redirect:noticeManage.ad";
		} else {
			throw new AdminException("공지사항 삭제를 실패하였습니다.");
		}
	}
	
	
	/** 포인트 */
	// 포인트 관리 페이지 이동
	@GetMapping("pointManage.ad")
	public String pointManageView(Model model) {
		ArrayList<Point> pList = aService.selectPointList();
		Point total = aService.selectTotalPoint();
		ArrayList<Payment> topTen = aService.selectTopTen();
		ArrayList<Payment> rList = aService.selectPaymentList();
		
		model.addAttribute("pList", pList);
		model.addAttribute("total", total);
		model.addAttribute("topTen", topTen);
		model.addAttribute("rList", rList);
		return "pointManage";
	}
	
	// 포인트 수정
	 @GetMapping("updatePoint.ad") 
	 @ResponseBody
	 public String updatePoint(@ModelAttribute Point p) {
		 int result = aService.updatePoint(p);
		 
		 if(result > 0) {
			 return "success";
		 } else {
			 return "fail";
		 }
		 
	 }
	 
	 // 포인트 상태 변경
	 @GetMapping("changePointStatus.ad")
	 @ResponseBody
	 public String changePointStatus(@ModelAttribute Point p) {
		 int result = aService.changePointStatus(p);
		 
		 if(result > 0) {
			 return "success";
		 } else {
			 return "fail";
		 }
	 }
	 
	 // 포인트 추가
	 @PostMapping("insertPoint.ad")
	 public String insertPoint(@ModelAttribute Point p) {
		 int result = aService.insertPoint(p);
		 
		 if(result > 0) {
			 return "redirect:pointManage.ad";
		 } else {
			 throw new AdminException("포인트 등록을 실패하였습니다.");
		 }
	 }
	 
	 
	
	
	/** 게시글 */
	// 게시글 관리 페이지 이동, 검색
	@GetMapping("boardManage.ad")
	public String boardManageView(@RequestParam(value="search", required=false) String search, 
								  Model model) {
		int totalCount = aService.getTotalCount();
		Integer reviewCount = aService.getTypeCount("REVIEW");
		Integer giveCount = aService.getTypeCount("GIVE");
		Integer withCount = aService.getTypeCount("WITH");
		
		ArrayList<Board> list = aService.selectBoardList(search);
	
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("reviewCount", reviewCount);
		model.addAttribute("giveCount", giveCount);
		model.addAttribute("withCount", withCount);
		model.addAttribute("list", list);
		
		return "boardManage";
	}
	
	// 게시글 삭제
	@GetMapping("deleteBoard.ad")
	@ResponseBody
	public String deleteBoard(@RequestParam("boardNo") int boardNo){
		int result = aService.deleteBoard(boardNo);
		
		if(result > 0) {
			return "success";
		} else {
			return "fail";
		}
	}
	
	
	/** 해시태그 */
	// 해시태그 관리 페이지 이동
	@GetMapping("hashTagManage.ad")
	public String hashTagManageView(Model model) {
		ArrayList<HashTag> tagList = aService.selectHashTagList();
		
		ArrayList<HashTag> generalTagList = aService.selectTopHashTagList("GENERAL");
		ArrayList<HashTag> personTagList = aService.selectTopHashTagList("PERSON");
		
		model.addAttribute("tList", tagList);
		model.addAttribute("gList", generalTagList);
		model.addAttribute("pList", personTagList);
		return "hashTagManage";
	}
	
	// 해시태그 추가
	@PostMapping("insertHashTag.ad")
	public String insertHashTag(@ModelAttribute HashTag hashTag) {
		int result = aService.insertHashTag(hashTag);
		
		if(result > 0) {
			return "redirect:hashTagManage.ad";
		} else {
			throw new AdminException("해시태그 추가를 실패하였습니다.");
		}
	}
	
	
	// 해시태그 삭제
	@PostMapping("deleteHashTag.ad")
	public String deleteHashTag(@RequestParam("tagNo") int tagNo) {
		int result = aService.deleteHashTag(tagNo);
		
		if(result > 0) {
			return "redirect:hashTagManage.ad";
		} else {
			throw new AdminException("해시태그 삭제를 실패하였습니다.");
		}
	}
	
	/** 지역 사진 관리 */
	// 지역 사진 관리 페이지 이동
	@GetMapping("contentImgManage.ad")
	public String contentImgManageView(Model model) {
		
		ArrayList<Image> list = aService.selectLocalImage();
		
		model.addAttribute("list", list);
		return "contentImgManage";
	}
	
	@PostMapping("insertLocalImg.ad")
	public String insertLocalImg(@RequestParam("localNo") int localNo, @RequestParam("formFile") ArrayList<MultipartFile> file) {
		ArrayList<Image> list = aService.selectLocalImage();
		System.out.println(list);
		try {
			for(Image i : list) {
				if(i.getImageRefNo() == localNo) {
					gdService.deleteFile(i.getImageRename());
				}
			}
		} catch (IOException e) {
			
		}
		
		System.out.println("파일 insert 전");
		MultipartFile upload = file.get(0);
		int result = 0;
		if(upload != null && !upload.isEmpty()) {
			String fileId;
			
			try {
				fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
				Image a = new Image();
				a.setImageOriginName(upload.getOriginalFilename());
				a.setImageRename(fileId);
				a.setImagePath("drive://files/" + fileId);
				a.setImageThum(2);
				a.setImageRefType("LOCAL");
				a.setImageRefNo(localNo);
				
				result = aService.insertLocalImg(a);
			} catch (IOException e) {
				
			}
		}
		
		System.out.println("파일 insert 후");
		if(result > 0) {
			return "redirect:contentImgManage.ad";
		} else {
			throw new AdminException("지역 사진 저장에 실패하였습니다.");
		}
	}
	
	// 삭제 과정 중 비밀번호 확인
	@GetMapping("matchPwd.ad")
	@ResponseBody
	public String matchPwd(@RequestParam("pwd") String pwd, HttpSession session) {
		Member m = (Member)session.getAttribute("loginUser");
		
		String result = "";
		if(bcrypt.matches(pwd, m.getMemberPwd())) {
			result = "success";
		} else {
			result = "fail";
		}
		
		return result;
	}
}
