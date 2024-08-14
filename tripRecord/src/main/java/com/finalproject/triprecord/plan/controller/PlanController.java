package com.finalproject.triprecord.plan.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;
import com.finalproject.triprecord.plan.model.exception.PlanException;
import com.finalproject.triprecord.plan.model.service.PlanService;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PlanController {
	
	@Autowired
	PlanService plService;
	
	@Autowired
	private PlaceService pService;

	@Autowired
	private MemberService mService;
	
	// 일정 메인
	@GetMapping("planMain.pl")
	public String planMainView(Model model) {
		ArrayList<HashTag> list = plService.hashTagList();
		model.addAttribute("list", list);
		return "planMain";
	}
	
	// 일정 생성
	@PostMapping("planCreate.pl")
	public String planCreateView(@ModelAttribute Schedule s,
								 @RequestParam("spot") String spot,
								 @RequestParam(value = "togetherTagNo", required = false) String togetherNo,
								 @RequestParam(value = "hashtagTagNos", required = false) String hashtagNo,
								 @RequestParam("localNo") int localNo,
								 Model model) {
//		HashMap<Integer, LocalDate> dates = dateFunction(s.getStartDate(), s.getEndDate());
		HashMap<Integer, String> dates = dateFunction(s.getStartDate(), s.getEndDate());
		
		ArrayList<Place> pList = pService.selectPlaceList(localNo);
		
		model.addAttribute("pList", pList);
		model.addAttribute("s", s);
		model.addAttribute("spot", spot);
		model.addAttribute("togetherNo", togetherNo);
		model.addAttribute("hashtagNo", hashtagNo);
		model.addAttribute("dates", dates);
		
		return "planDetail";
	}
	
	// 여행 n일 계산
	public HashMap<Integer, String> dateFunction(String startDate, String endDate) {

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		LocalDate start = LocalDate.parse(startDate, format);
		LocalDate end = LocalDate.parse(endDate, format);

		HashMap<Integer, String> dates = new HashMap<>();
		
		int day = 1;
		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			dates.put(day, date+"");
			day++;
		}

		return dates;
	}
	
	// planDetil 에서 시작일, 종료일, 지역 수정
	@PostMapping("updateValue.pl")
	public ResponseEntity<Map<String, Object>> updateValue(@RequestParam("startDate") String startDate, 
														   @RequestParam("endDate") String endDate,
														   	@RequestParam("spot") String spot) {
		Schedule s = new Schedule();
		s.setStartDate(startDate);
		s.setEndDate(endDate);
		s.setSpot(spot);
		
		HashMap<Integer, String> dates = dateFunction(startDate, endDate);
		
		if(!dates.isEmpty()) {
			Map<String, Object> map = new HashMap<>();
			map.put("s", s);
			map.put("dates", dates);

			return ResponseEntity.ok(map);
		} else {
			return null ;
		}
		
	}
	
	// 일정 저장
	@PostMapping("saveplan.pl")
	public String savePlanInsert(@ModelAttribute Schedule s, @ModelAttribute Plan p, 
								HttpSession session, @RequestParam("count") String count) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		if(loginUser != null) {
			s.setWriterNo(loginUser.getMemberNo());
		}
		
		String place[] = p.getPlace().split(",");
		String time[] = p.getTime().split(",");
		String memo[] = p.getMemo().split(",");
		String reserve[] = p.getReserve().split("/");
		String day[] = p.getDay().split(",");
		
		String cStr[] = count.split(";"); // count 안에 한 날짜에 몇 개 담겼는지 += 구분자(;) 로 들어옴
		Integer coNum[] = new Integer[cStr.length];
		for(int i = 0; i < cStr.length; i++) {
			coNum[i] = Integer.parseInt(cStr[i]);
		}
		
		s.setScLocalNo(Integer.parseInt(s.getSpot()));
		
		ArrayList<Plan> plList = new ArrayList<Plan>();
		for(int i = 0, coCount = 0, dNum = 0; i < place.length; i++, coCount++) {
			
			Plan pl = new Plan();
			pl.setPlNo(i);
			pl.setPlace(place[i]);
			pl.setTime(time[i]);
			
			if(memo[i].equals("-")) {
				pl.setMemo(null);
			} else {
				pl.setMemo(memo[i]);
			}
			
			pl.setReserve(reserve[i]);
			
			if(coCount == coNum[dNum]) {
				coCount = 0;
				dNum++;
			}
			pl.setDay(day[dNum]);
			
			plList.add(pl);
		}
		
		ArrayList<HashTag> tagList = new ArrayList<HashTag>();
		HashTag h = null;
		String hashtag[] = s.getHashtag().split(",");
		if(!hashtag[0].equals("0") || !hashtag[1].equals("0")) {
			for(int i = 0; i < hashtag.length; i++) {
				if(!hashtag[i].equals("0")) {
					h = new HashTag();
					h.setTagNo(Integer.parseInt(hashtag[i]));
					h.setTagRefType("SCHEDULE");
					tagList.add(h);
				}
			}
		} else {
			tagList.add(h); //serviceImpl 에서 contains(null) 비교 후 반환함
		}
		
		int result = plService.savePlanInsert(s, plList, tagList);
		
		if(result > 0) {
			return "redirect:/";
		} else {
			throw new PlanException("일정을 저장하던 중 오류가 발생했습니다.");
		}
	}
	
	// 마이페이지 내 일정 보기 -> 내 여행 노트
	@GetMapping("myTripNote.pl")
	public String myTripNoteList(HttpSession session, HttpServletRequest request, 
								Model model, @RequestParam(value="page", defaultValue="1") int currentPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    Image image = mService.existFileId(memberNo); 
	    
	    int listCount = plService.getListCount(memberNo);
	    PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
	    
	    ArrayList<Schedule> sList = plService.myTripNoteList(memberNo, pi);
	    
	    if(sList.isEmpty()) {
	    	Schedule s = new Schedule();
	    	s.setScNo(0); // 일정 작성 안 했어도 해시태그 가지러 갔다올 거라
	    	sList.add(s);
	    } else {
	    	Date today = new Date();
	    	
	    	for(int i = 0; i < sList.size(); i++) {
	    		if(today.compareTo(sList.get(i).getScEndDate()) > 0) {
	    			sList.get(i).setStatus("완료");
	    			
	    		} else {
	    			sList.get(i).setStatus("기대하는 중");
	    		}
	    	}
	    }
	    
	    ArrayList<HashTag> hList = plService.myTripTagList(sList);
	    String hashtag = "";
	    
	    if(!hList.isEmpty()) {
	    	for(int s = 0; s < sList.size(); s++) {
	    		int scNo = sList.get(s).getScNo();
	    		for(int i = 0; i < hList.size(); i++) {
    				if(scNo == hList.get(i).getTagRefNo()) {
    					hashtag += hList.get(i).getTagName() + " ";
	    			}
    			}
	    		if(!hashtag.equals("")) {
	    			sList.get(s).setHashtag(hashtag);
	    		}
	    		hashtag = "";
	    	}
	    }
	    
	    model.addAttribute("page", currentPage);
	    model.addAttribute("sList", sList);
	    model.addAttribute("pi", pi);
	    model.addAttribute("listCount", listCount);
	    model.addAttribute("loc", request.getRequestURI());
	    
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
		return "myTripNote";
	}
	
//	마이페이지 내 일정 보기 -> 내 여행 노트 -> 상세 조회
	@GetMapping("detailMyTripNote.pl")
	public String detailMyTripNote(@RequestParam("scNo") int scNo, @RequestParam(value="page", defaultValue="1") int page, 
									HttpSession session, Model model, RedirectAttributes ra, HttpServletRequest req) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		Image image = mService.existFileId(memberNo); 
	    if (image != null && image.getImageRename() != null) {
	    	String existFileId = image.getImageRename(); 
	    	model.addAttribute("rename", existFileId);
	    } else {
	    	// 이미지가 없거나 리네임이 없는 경우 처리
	    	model.addAttribute("rename", "defaultImageName"); 
	    }
	    
	    Schedule s = plService.detailMySchedule(scNo);
	    
	    HashMap<Integer, String> dates = dateFunction(s.getStartDate(), s.getEndDate());
	    
	    ArrayList<Plan> pList = plService.detailMyTripNote(scNo);
	    if(!pList.isEmpty()) {
	    	for(int i = 0; i < pList.size(); i++) {
	    		pList.get(i).setDay(pList.get(i).getDay().split(" ")[0]);
	    	}
	    	
	    	model.addAttribute("page", page);
	    	model.addAttribute("s", s);
	    	model.addAttribute("dates", dates);
	    	model.addAttribute("pList", pList);
	    	model.addAttribute("loc", req.getRequestURI());
	    	
	    	return "detailMyTripNote";
	    } else {
	    	throw new PlanException("일정 상세 조회에 실패하였습니다.");
	    }
	}
	
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 일정 삭제
	@PostMapping("deleteTripNote.pl")
	public String deleteTripNote(@RequestParam("scNo") int scNo) {
		int result = plService.deleteTripNote(scNo);
		if(result > 0) {
			return "redirect:myTripNote.pl";
		} else {
			throw new PlanException("일정 삭제에 실패하였습니다.");
		}
	}
	
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 장소, 시간, 메모 수정 ajax 
	@GetMapping("detailTripUpdate.pl")
	@ResponseBody
	public String detailTripUpdate(@RequestParam("place") String place, @RequestParam("time") String time, @RequestParam("memo") String memo, @RequestParam("plNo") String plNo) {
		Properties prop = new Properties();
		prop.setProperty("place", place);
		prop.setProperty("time", time);
		prop.setProperty("memo", memo);
		prop.setProperty("plNo", plNo);
		int result = plService.detailTripUpdate(prop);
		return result > 0? "1" : "0";
	}
	
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 예약 여부 수정 ajax 
	@GetMapping("updateReserve.pl")
	@ResponseBody
	public String updateReserve(@RequestParam("plNo") String plNo, @RequestParam("status") String status) {
		Properties prop = new Properties();
		prop.setProperty("plNo", plNo);
		prop.setProperty("status", status);
		int result = plService.updateReserve(prop);
		return result > 0 ? "1" : "0";
	}
	
	
	
	
	
	
	
	
	
	
	
}
