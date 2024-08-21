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
								 /*@RequestParam("scLocalNo") int sclocalNo,*/
								 Model model) {
		HashMap<Integer, String> dates = dateFunction(s.getStartDate(), s.getEndDate()); 
		// planDetail 에 하루마다의 일정 생성하기 위한 날짜 계산 함수
		// Integer : 1일차, 2일차... String: 2024-08-16, 2024-08-17...
		// 2024-08-16 등의 날짜는 planDetail 의 날짜 밑 input type="hidden" name="day" 의 value 가 됨
		
		ArrayList<Place> pList = pService.selectPlaceList(s.getScLocalNo()); // 추천 장소. select option 의 value 값 들고 감
		
		model.addAttribute("pList", pList);
		model.addAttribute("s", s);
		model.addAttribute("spot", spot); // 선택한 지역명. select 밑에 input type="hidden" name="spot" 으로 있고 선택한 지역은 javaScript 통해서 value 로 담김
		model.addAttribute("togetherNo", togetherNo); // 같이 갈 사람 해시태그 번호 (schedule 에는 text 담겨있음)
		model.addAttribute("hashtagNo", hashtagNo);	  // 어떤 컨셉 여행인지 해시태그 번호
		model.addAttribute("dates", dates);			  // 날짜 계산 함수
		
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
	
	// planDetil 에서 시작일, 종료일, 지역 수정 Ajax
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
	// planDetail 에서 일정 저장하기 버튼 눌렀을 때
	@PostMapping("saveplan.pl")
	public String savePlanInsert(@ModelAttribute Schedule s, @ModelAttribute Plan p, 
								HttpSession session, @RequestParam("count") String count) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		if(loginUser != null) {
			s.setWriterNo(loginUser.getMemberNo());
		}
		
		// 밑에 있는 것들은 전부 , 로 이어져서 들어오기 때문에 split 사용하였음, 밑에서 배열 돌려야 함
		String place[] = p.getPlace().split(",");
		String time[] = p.getTime().split(",");
		String memo[] = p.getMemo().split(",");
		String reserve[] = p.getReserve().split("/"); // 예약했는지 안 했는지 javaScript 로 Y/N/Y/N 이런 형태로 들어오게 만들었음
		String day[] = p.getDay().split(","); // 계획 1개에 해당하는 day
		// 16일 계획 2개, 17일 계획 1개라면 2024-08-16, 2024-08-16, 2024-08-17... (중복되는 거 맞음)
		
		String cStr[] = count.split(";"); // count 안에 한 날짜에 몇 개 담겼는지 += 구분자(;) 로 들어옴. 16일 계획 2개, 17일 계획 1개라면 2, 1
		
		Integer coNum[] = new Integer[cStr.length]; // 날짜만큼 plan 만들기 위해서 Integer 숫자 하나 만듬
		for(int i = 0; i < cStr.length; i++) {
			coNum[i] = Integer.parseInt(cStr[i]); // cStr 이랑 같은 값 들어있는데 타입만 다름
		}
		
		s.setScLocalNo(Integer.parseInt(s.getSpot())); // 지역 번호 설정 select option 에서 선택된 value 값
		
		ArrayList<Plan> plList = new ArrayList<Plan>(); // 계획 1개씩 담을 ArrayList
		for(int i = 0, coCount = 0, dNum = 0; i < place.length; i++, coCount++) { // 장소는 무조건 있어야 하는 값 중 하나라 place.length 만큼 돌림
			
			Plan pl = new Plan();
			pl.setPlNo(i); // 이건 그냥 넣었어요
			pl.setPlace(place[i]); // 장소 소매넣기
			pl.setTime(time[i]); // 시간 소매넣기
			
			if(memo[i].equals("-")) { // 메모 안 써있으면 - 로 값 들어옴
				pl.setMemo(null); // null 허용해 둠
			} else {
				pl.setMemo(memo[i]);
			}
			
			pl.setReserve(reserve[i]); // Y, N 들어감
			
			if(coCount == coNum[dNum]) { 
				// 위에서 계산한 coNum의 하루에 계획 몇 개인지에 대한 값과 coCount 가 일치하면 그 다음 날 날짜 넣음 
				// dNum은 coNum 과 day 의 인덱스를 위함
				// coCount 는 날짜 하루마다 초기화됨
				coCount = 0;
				dNum++;
			}
			pl.setDay(day[dNum]);
			
			plList.add(pl);
		}
		
		ArrayList<HashTag> tagList = new ArrayList<HashTag>();
		HashTag h = null; // serviceImpl 에서 해시태그가 contains(null) 인지 아닌지 판단해서 insert 할지 말지 결정함(해시태그는 선택사항임)
		String hashtag[] = s.getHashtag().split(","); // 해시태그 선택 안 했더라도 javaScript를 통해서 생성되는 input type="hidden" 에 0 으로 들어가있음
		if(!hashtag[0].equals("0") || !hashtag[1].equals("0")) { // 동반이든 일반이든 0이 아니라면
			for(int i = 0; i < hashtag.length; i++) {
				if(!hashtag[i].equals("0")) {
					h = new HashTag();
					h.setTagNo(Integer.parseInt(hashtag[i]));
					h.setTagRefType("SCHEDULE");
					tagList.add(h);
				}
			}
		} else {
			tagList.add(h);
		}
		
		// 처음 일정 작성하는 거면 기특하다구 포인트 줘야 됨
		int num = plService.scheduleCount(loginUser.getMemberNo());
		if(num == 0) {
			plService.updatePoint(loginUser.getMemberNo()); // db 로그인 업데이트
			loginUser.setMemberPoint(loginUser.getMemberPoint() + 1000);
			
			Member m = new Member();
			m.setMemberId(loginUser.getMemberId());
			m.setMemberPwd(loginUser.getMemberPwd());
			mService.login(m);
		}
		
		int result = plService.savePlanInsert(s, plList, tagList); // serviceImpl 주의
		
		if(result > 0) {
			return "redirect:/";
		} else {
			throw new PlanException("일정을 저장하던 중 오류가 발생했습니다.");
		}
	}
	
	// 마이페이지 내 일정 보기 -> 내 여행 노트 리스트
	@GetMapping("myTripNote.mp")
	public String myTripNoteList(HttpSession session, HttpServletRequest request, 
								Model model, @RequestParam(value="page", defaultValue="1") int currentPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    Image image = mService.existFileId(memberNo); 
	    
	    int listCount = plService.getListCount(memberNo); // 페이지 계산
	    PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
	    
	    ArrayList<Schedule> sList = plService.myTripNoteList(memberNo, pi); // 로그인 유저가 작성한 일정 리스트 가져오기
	    
	    if(sList.isEmpty()) {
	    	Schedule s = new Schedule();
	    	s.setScNo(0); // 일정 작성 안 했어도 해시태그 가지러 갔다올 거라
	    	sList.add(s);
	    } else {
	    	Date today = new Date(); // 오늘 날짜 생성
	    	
	    	for(int i = 0; i < sList.size(); i++) {
	    		if(today.compareTo(sList.get(i).getScEndDate()) > 0) { // endDate 가 지났다면
	    			sList.get(i).setStatus("완료");	// 여행 상태 : 완료
	    			
	    		} else {
	    			sList.get(i).setStatus("기대하는 중"); // 여행 상태 : 기대하는 중
	    		}
	    	}
	    }
	    
	    ArrayList<HashTag> hList = plService.myTripTagList(sList); // scNo 에 따른 해시태그가 각기 다를 거라 Schedule list 들고 감
	    String hashtag = "";
	    
	    if(!hList.isEmpty()) { // 하나라도 해시태그 선택을 한 일정이 있다면
	    	for(int s = 0; s < sList.size(); s++) { // sList 랑 hList 는 개수가 틀려서 중복 for 문 돌려야 함
	    		int scNo = sList.get(s).getScNo(); 
	    		for(int i = 0; i < hList.size(); i++) { // select_hashtag의 hList
    				if(scNo == hList.get(i).getTagRefNo()) { // scNo 랑 hList의 참고하고 있는 참조 번호(scNo)가 같다면
    					hashtag += hList.get(i).getTagName() + " "; // 위에서 만든 String hashtag 에 추가(배열 아님)
	    			}
    			}
	    		if(!hashtag.equals("")) { // hashtag에 값 들어갔다면 그때 sList 에 넣을 것 이때는 together 구분 안 함
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
	        model.addAttribute("noProfile", "noProfile"); 
	    }
		return "myTripNote";
	}
	
//	마이페이지 내 일정 보기 -> 내 여행 노트 -> 상세 조회
	@GetMapping("detailMyTripNote.mp")
	public String detailMyTripNote(@RequestParam("scNo") int scNo, @RequestParam(value="page", defaultValue="1") int page, 
									HttpSession session, Model model, RedirectAttributes ra, HttpServletRequest req) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		Image image = mService.existFileId(memberNo); 
	    if (image != null && image.getImageRename() != null) {
	    	String existFileId = image.getImageRename(); 
	    	model.addAttribute("rename", existFileId);
	    } else {
	    	// 이미지가 없거나 리네임이 없는 경우 처리
	    	model.addAttribute("noProfile", "noProfile"); 
	    }
	    
	    Schedule s = plService.detailMySchedule(scNo);
	    System.out.println(s);
	    
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
	@PostMapping("deleteTripNote.mp")
	public String deleteTripNote(@RequestParam("scNo") int scNo) {
		int result = plService.deleteTripNote(scNo);
		if(result > 0) {
			return "redirect:myTripNote.mp";
		} else {
			throw new PlanException("일정 삭제에 실패하였습니다.");
		}
	}
	
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 장소, 시간, 메모 수정 ajax 
	@GetMapping("detailTripUpdate.mp")
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
	@GetMapping("updateReserve.mp")
	@ResponseBody
	public String updateReserve(@RequestParam("plNo") String plNo, @RequestParam("status") String status) {
		Properties prop = new Properties();
		prop.setProperty("plNo", plNo);
		prop.setProperty("status", status);
		int result = plService.updateReserve(prop);
		return result > 0 ? "1" : "0";
	}
	
	@GetMapping("detailDeletePlan.mp")
	@ResponseBody
	public String detailDeletePlan(@RequestParam("plNo") String plNo) {
		int result = plService.detailDeletePlan(plNo);
		return result > 0 ? "1" : "0";
	}
	
	
	
	
	
	
	
	
}
