package com.finalproject.triprecord.board.controller;


import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
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

import com.finalproject.triprecord.board.model.exception.BoardException;
import com.finalproject.triprecord.board.model.service.BoardService;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.board.model.vo.Reply;
import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.place.model.exception.PlaceException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

	@Autowired
	private BoardService bService;
	
	@Autowired
	private GoogleDriveService gdService;
	
	@GetMapping("community.bo") // 커뮤니티 게시글 가져오기
	public String community(@RequestParam(value="page", defaultValue="1") int currentPage,@RequestParam(value="generalType", defaultValue="ALL") String generalType,
			@RequestParam(value="boardType", defaultValue="GENERAL") String boardType,
			@RequestParam(value="localName", defaultValue="ALL") String localName,
							Model model, HttpServletRequest req) {
		
		CategorySelect cs = new CategorySelect();
		cs.setBoardType("GENERAL");
		
		
		int listCount = bService.getListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10); // 10개씩 보여주겠다
		ArrayList<Board> cList = bService.getBoardList(cs, pi); // 메소드 안에 해당 타입 넣으면 그거 가져옴
		// GENERAL, QUESTION, NOTICE -> 일반, 문의, 공지
		// GENERAL -> 동행 WITH, 양도 GIVE, 후기 REVIEW
		
		
		
		if(!cList.isEmpty()) {
			model.addAttribute("cList", cList);
			model.addAttribute("pi", pi);
			//model.addAttribute("loc", req.getRequestURI());
			model.addAttribute("listCount", listCount);
			model.addAttribute("generalType","ALL");
			model.addAttribute("boardType","GENERAL");
		} else {
			model.addAttribute("nothing", null);
		}
		return "commuList";
	}
	
	@GetMapping("categorySelect.bo")
	public String categorySelect(@ModelAttribute CategorySelect cs, @RequestParam(value="page", defaultValue="1") int currentPage,
								 Model model, HttpServletRequest req) {
		
		if(cs.getGeneralType().equals("동행") || cs.getGeneralType().equals("WITH") ) {
			cs.setGeneralType("WITH");
		} else if(cs.getGeneralType().equals("양도") || cs.getGeneralType().equals("GIVE")) {
			cs.setGeneralType("GIVE");
		} else if(cs.getGeneralType().equals("후기") || cs.getGeneralType().equals("REVIEW")) {
			cs.setGeneralType("REVIEW");
		} else if(cs.getGeneralType() == "") {
			cs.setGeneralType("ALL");
		} else {
			cs.setGeneralType("ALL");
		}
		
		if(cs.getLocalName() == "") {
			cs.setLocalName("ALL");
		} else if(cs.getLocalName().equals("전국")) {
			cs.setLocalName("ALL");
		}
		
		
		
		int listCount = bService.getCategorySelectListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Board> cList = bService.getCategorySelectBoardList(cs, pi);
		
		
		model.addAttribute("cList", cList);
		model.addAttribute("pi",pi);
//			model.addAttribute("loc", req.getRequestURI());
		model.addAttribute("listCount", listCount);
		model.addAttribute("searchWord", cs.getSearchWord());
		model.addAttribute("generalType", cs.getGeneralType());
		model.addAttribute("localName", cs.getLocalName());
		//model.addAttribute("boardType", "GENERAL");    // 현재 커뮤니티 게시판 == 무조건 GENERAL
		return "commuList";
	}
	
	@GetMapping("commuWrite.bo")
	public String commuWrite() {
		return "communityWrite";
	}
	
	@PostMapping("commuEdit.bo")
	public String commuEdit(@RequestParam("boardNo") int bNo, Model model) {
		Board b = bService.selectBoard(bNo, 0);
		ArrayList<Image> iList = bService.selectImage(bNo);
		
		if(b != null) {
			model.addAttribute("b", b);
			model.addAttribute("iList", iList);
		}
		return "communityEdit";
	}
	
	@PostMapping("commuDelete.bo")
	public String commuDelete(@RequestParam("boardNo") Integer bNo) {
		bService.deleteBoard(bNo);
		return "redirect:community.bo";
	}
	
	@GetMapping("commuSelect.bo")
	public String commuSelect(@RequestParam("generalType") String generalType,@RequestParam("boardNo") Integer boardNo, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpSession session, HttpServletRequest req) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int id = 0;
		if(loginUser != null) {
			id = loginUser.getMemberNo();
		}
		
		Board board = bService.selectBoard(boardNo, id);
		ArrayList<Image> iList = bService.selectImage(boardNo); // 무조건 BOARD 니까 boardNo 만 보내면 가능
		Image writerProfile = bService.selectProfileImage(board.getBoardWriterNo()); // 회원번호를 보내서 그 ref_type = 'MEMBER' && ref_no = 회원번호
		Image replyProfile = bService.selectProfileImage(id); // 로그인 x -> null, 로그인 ㅇ -> 사진 있을때 image, 없을때 null
		ArrayList<Reply> rList = bService.selectReply(boardNo);
		
		
		model.addAttribute("b", board);
		model.addAttribute("page", page);
		model.addAttribute("rList", rList);
		model.addAttribute("generalType", generalType);
		model.addAttribute("writerProfile", writerProfile);
		model.addAttribute("replyProfile", replyProfile);
		model.addAttribute("loc", req.getRequestURI());
		model.addAttribute("iList", iList);
		return "communitySelect";
	}
	
	
	/*
	 * BOARD.BOARD_NO = GENERAL_BO.GENERAL_NO
	 */
	
	public int returnBoardNo() {
		return bService.selectBoardNo(); // 단순히 seq_board.currval 을 가져오려고 만듬
	}
	
	
	@PostMapping("insertBoard.bo")
	public String insertBoard(@ModelAttribute Board b, HttpSession session, @RequestParam(value="files", required=false) ArrayList<MultipartFile> files) {
		// 부트 board 에는 (일반,공지,문의) + (양도,동행,후기) + (지역이름) 있음
		Member loginUser = (Member)session.getAttribute("loginUser");
		b.setBoardWriterNo(loginUser.getMemberNo());
		int result = bService.insertBoard(b); // 성공 시 1
		int result2 = bService.insertGeneralBoard(b); // 성공 시 1
		int bNo = returnBoardNo(); // seq_board.currval
		int memWriteCount = bService.countBoardList(loginUser.getMemberNo());
		
		if(memWriteCount == 0) {
			bService.firstBoardPoint(loginUser.getMemberNo());
		}
		
		ArrayList<Image> list = new ArrayList<Image>();
		for(int i = 0; i < files.size(); i++) {
			MultipartFile upload = files.get(i);
			
			//if(!upload.getOriginalFilename().equals("")) {
			if(upload != null && !upload.isEmpty()) {
	            String fileId;
	            
				try {
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image a = new Image();
					a.setImageOriginName(upload.getOriginalFilename());
					a.setImageRename(fileId);
					a.setImagePath("drive://files/" + fileId);
					a.setImageThum(2);
					a.setImageRefType("BOARD");
					a.setImageRefNo(bNo);
					
					list.add(a);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		int iResult = 0;
		if(!list.isEmpty()) {
			iResult = bService.insertImage(list);
		}
		if(result + result2 + iResult == 2 + list.size()) {
			return "redirect:community.bo";
		}else {
			bService.deleteBoard(bNo); // 이미지 넣기 실패해서 글도 삭제 ?
			for(Image a : list) {
				try {
					gdService.deleteFile(a.getImageRename());
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			throw new BoardException("게시글 작성에 실패하였습니다.");
		}
	}
	
	@PostMapping("updateBoard.bo")
	public String updateBoard(@ModelAttribute Board b, @RequestParam(value="files", required=false) ArrayList<MultipartFile> files,
							  @RequestParam(value="page",defaultValue="1") int page, @RequestParam(value="delImg", required=false) ArrayList<String> delImgs,
							  HttpSession session, RedirectAttributes ra) {
		
		
		int result = bService.updateBoard(b);
		
		ArrayList<Image> list = new ArrayList<Image>();
		ArrayList<String> deleteImg = new ArrayList<String>();
		
		int delResult = 0;
		int insertResult;
		
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
			}
			delResult = bService.delImg(deleteImg);
			
			// 이미지 추가
			for(int i = 0; i < files.size(); i++) {
				MultipartFile upload = files.get(i);
				
				//if(!upload.getOriginalFilename().equals("")) {
				if(upload != null && !upload.isEmpty()) {
		            String fileId;
		            
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image a = new Image();
					a.setImageOriginName(upload.getOriginalFilename());
					a.setImageRename(fileId);
					a.setImagePath("drive://files/" + fileId);
					a.setImageThum(2);
					a.setImageRefType("BOARD");
					a.setImageRefNo(b.getBoardNo());
					
					list.add(a);
				}
			}
			
			
			if(!list.isEmpty()) {
				insertResult = bService.insertImage(list);
				
				if(insertResult < 0) {
					for(Image i : list) {
						gdService.deleteFile(i.getImageRename());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(result + delResult == 1 + deleteImg.size()) {
			ra.addAttribute("boardNo", b.getBoardNo());
			ra.addAttribute("generalType", b.getGeneralType());
			ra.addAttribute("page", page);
			return "redirect:commuSelect.bo";
		}else {
			throw new PlaceException("게시글 수정 중 에러발생");
		}
	}
	
	
	@GetMapping(value="insertCommuReply.bo", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String insertCommuReply(@ModelAttribute Reply r) {
		bService.insertReply(r);
		ArrayList<Reply> list = bService.selectReply(r.getBoardNo());
//		out.print(list); // PrintWriter 사용 시 String 타입으로 출력됨
		
		JSONArray array = new JSONArray();
		for(Reply reply : list) {
			JSONObject json = new JSONObject(); // {"key":value}
			json.put("replyNo", reply.getReplyNo());
			json.put("replyContent", reply.getReplyContent());
			json.put("replyBoardId", reply.getBoardNo());
			json.put("replyWriterNo", reply.getReplyWriterNo());
			json.put("nickname", reply.getNickname());
			json.put("replyCreateDate", reply.getReplyCreateDate());
			json.put("replyModifyDate", reply.getReplyModifyDate());
			json.put("replyStatus", reply.getReplyStatus());
		
			array.put(json); // 하나를 담은 json을 jsonArray에 담기(add 없음)
		}
		return array.toString(); // 보내야 하는 형식은 toString 밖에 없음
	}
	
	@GetMapping("deleteReply.bo")
	@ResponseBody
	public String deleteReply(@RequestParam("rNo") int rNo) {
		int result = bService.deleteReply(rNo);
		return result == 1? "success" : "no";
	}
	
	@GetMapping("updateReply.bo")
	@ResponseBody
	public String updateReply(@ModelAttribute Reply r) {
		int result = bService.updateReply(r);
		return result == 1? "success" : "fail";
	}
	
	
	
	@GetMapping("ask.no")
	public String ask(@RequestParam(value="page", defaultValue="1") int currentPage,@RequestParam(value="generalType", defaultValue="ALL") String generalType,
			@RequestParam(value="boardType", defaultValue="GENERAL") String boardType,
			@RequestParam(value="localName", defaultValue="ALL") String localName,
							Model model, HttpServletRequest req) {// 문의는 사이드 + 카테고리

		CategorySelect cs = new CategorySelect();
		cs.setBoardType("QUESTION");
		// GENERAL, QUESTION, NOTICE -> 일반, 문의, 공지
		
		
		int listCount = bService.getListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10); // 10개씩 보여주겠다
		ArrayList<Board> aList = bService.getBoardList(cs, pi); // 메소드 안에 해당 타입 넣으면 그거 가져옴
		// GENERAL -> 동행 WITH, 양도 GIVE, 후기 REVIEW
		ArrayList<Question> qList = bService.getQuestionList(0); // 0 보내면 Question 전체리스트, 숫자 보내면 해당 번호 가져오기
		
		
		if(!aList.isEmpty()) {
			model.addAttribute("aList", aList); // 보드
			model.addAttribute("pi",pi);
			model.addAttribute("loc", req.getRequestURI());
			model.addAttribute("listCount", listCount);
			model.addAttribute("qList", qList); // 문의(글번호, 비번, 답변, 답변YN)
			model.addAttribute("generalType","ALL");
			//model.addAttribute("boardType","GENERAL");
		} else {
			model.addAttribute("nothing", null);
		}
		return "askList";
	}
	
	@GetMapping("askSelect.no")
	public String askSelect(@RequestParam(value="generalType", defaultValue="ALL") String generalType, @RequestParam("boardNo") Integer boardNo,
							@RequestParam(value="page", defaultValue="1") int page, Model model, HttpSession session) { // 선택한 글번호 넘겨받기
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		int id = 0;
		if(loginUser != null) {
			id = loginUser.getMemberNo();
		}
		
		Board board = bService.selectBoard(boardNo, id);
		Question q = bService.selectQuestion(boardNo);
		ArrayList<Image> iList = bService.selectImage(boardNo);
		Image writerProfile = bService.selectProfileImage(board.getBoardWriterNo()); 
		
		
		model.addAttribute("n", board);
		model.addAttribute("page", page);
		model.addAttribute("q", q);
		model.addAttribute("writerProfile", writerProfile);
		model.addAttribute("generalType", generalType);
		// model.addattribute("myPage", 1);
		model.addAttribute("iList", iList);
		return "askSelect";
		
	}
	
	@GetMapping("askCategorySelect.no")
	public String askCategory(@ModelAttribute CategorySelect cs, @RequestParam(value="page", defaultValue="1") int currentPage,
			 Model model, HttpServletRequest req) {

		if(cs.getGeneralType().equals("결제") || cs.getGeneralType().equals("PAYMENT") ) {
			cs.setGeneralType("PAYMENT");
		} else if(cs.getGeneralType().equals("플래너") || cs.getGeneralType().equals("PLANNER")) {
			cs.setGeneralType("PLANNER");
		} else if(cs.getGeneralType().equals("기타") || cs.getGeneralType().equals("ELSE")) {
			cs.setGeneralType("ELSE");
		} else if(cs.getGeneralType() == "") {
			cs.setGeneralType("ALL");
		} else {
			cs.setGeneralType("ALL");
		}
		
		cs.setLocalName("ALL");
		if(cs.getSearchWord() != null) {
			cs.setSearchWord(cs.getSearchWord().trim());
		}
		
		
		int listCount = bService.getaskCategoryListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Board> cList = bService.getCategorySelectQuestionList(cs, pi);
		ArrayList<Question> qList = bService.getQuestionList(0);
		
		model.addAttribute("aList", cList);
		model.addAttribute("pi",pi);
		model.addAttribute("listCount", listCount);
		model.addAttribute("qList", qList);
		model.addAttribute("searchWord", cs.getSearchWord());
		model.addAttribute("generalType", cs.getGeneralType());
		return "askList";
	}
	
	@GetMapping("askWrite.no")
	public String askWrite() {
		return "askWrite";
	}
	
	@PostMapping("insertQuestion.no")
	public String insertQuestion(@ModelAttribute Board b, @RequestParam("pwd") Integer pwd, HttpSession session, @RequestParam(value="files", required=false) ArrayList<MultipartFile> files) {
		// b 에 든거 : 제목, 내용, 결제/플래너/기타
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		b.setBoardWriterNo(loginUser.getMemberNo());
		int result = bService.insertBoard(b); // 성공 시 1
		int result2 = bService.insertGeneralAsk(b); // 성공 시 1
		int bNo = returnBoardNo(); // seq_board.currval
		Board t = new Board();
		t.setBoardNo(bNo);
		t.setBoardCount(pwd);
		int result3 = bService.insertQuestion(t);
		
		ArrayList<Image> list = new ArrayList<Image>();
		for(int i = 0; i < files.size(); i++) {
			MultipartFile upload = files.get(i);
			
			//if(!upload.getOriginalFilename().equals("")) {
			if(upload != null && !upload.isEmpty()) {
	            String fileId;
	            
				try {
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image a = new Image();
					a.setImageOriginName(upload.getOriginalFilename());
					a.setImageRename(fileId);
					a.setImagePath("drive://files/" + fileId);
					a.setImageThum(2);
					a.setImageRefType("BOARD");
					a.setImageRefNo(bNo);
					
					list.add(a);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		int iResult = 0;
		if(!list.isEmpty()) {
			iResult = bService.insertImage(list);
		}
		if(result + result2 + iResult == 2 + list.size()) {
			return "redirect:ask.no";
		}else {
			bService.deleteBoard(bNo); // 이미지 넣기 실패해서 글도 삭제 ?
			for(Image a : list) {
				try {
					gdService.deleteFile(a.getImageRename());
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			throw new BoardException("작성에 실패하였습니다.");
		}
	
	}
	
	@PostMapping("editQuestion.no")
	public String editQuestion(@RequestParam("boardNo") int boardNo, Model model) {

		Board b = bService.selectBoard(boardNo, 0);
		ArrayList<Image> iList = bService.selectImage(boardNo);
		String askType = bService.selectAskBoard(boardNo);
		b.setGeneralType(askType);
		
		model.addAttribute("b", b);
		model.addAttribute("iList", iList);
		//System.out.println(b);
		return "askEdit";
	}
	
	@PostMapping("updateQuestion.no")
	public String updateQuestion(@ModelAttribute Board b, @RequestParam("pwd") Integer pwd, @RequestParam(value="delImg", required=false) ArrayList<String> delImgs,
								@RequestParam(value="files", required=false) ArrayList<MultipartFile> files, RedirectAttributes ra) {
		
		int result = bService.updateBoard(b);
		
		//System.out.println(b);
		
		ArrayList<Image> list = new ArrayList<Image>();
		ArrayList<String> deleteImg = new ArrayList<String>();
		
		Board t = new Board();
		t.setBoardNo(b.getBoardNo());
		t.setBoardCount(pwd);
		bService.updateQuestion(t);
		
		int delResult = 0;
		int insertResult;
		
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
			}
			delResult = bService.delImg(deleteImg);
			
			// 이미지 추가
			for(int i = 0; i < files.size(); i++) {
				MultipartFile upload = files.get(i);
				
				//if(!upload.getOriginalFilename().equals("")) {
				if(upload != null && !upload.isEmpty()) {
		            String fileId;
		            
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image a = new Image();
					a.setImageOriginName(upload.getOriginalFilename());
					a.setImageRename(fileId);
					a.setImagePath("drive://files/" + fileId);
					a.setImageThum(2);
					a.setImageRefType("BOARD");
					a.setImageRefNo(b.getBoardNo());
					
					list.add(a);
				}
			}
			
			
			if(!list.isEmpty()) {
				insertResult = bService.insertImage(list);
				
				if(insertResult < 0) {
					for(Image i : list) {
						gdService.deleteFile(i.getImageRename());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(result + delResult == 1 + deleteImg.size()) {
			ra.addAttribute("boardNo", b.getBoardNo());
			ra.addAttribute("generalType", b.getGeneralType());
			ra.addAttribute("page", 1);
			return "redirect:askSelect.no";
		}else {
			throw new PlaceException("문의사항 수정 중 에러발생");
		}
	}
	
	@PostMapping("deleteQuestion.no")
	public String deleteQuestion(@RequestParam("boardNo") int boardNo) {
		// 문의사항 : 보드 스테이터스 바꾸기 + 이미지 삭제하기
		bService.deleteBoard(boardNo);
		int result = bService.deleteImg(boardNo);
		return "redirect:askList.no";
	}
	
	
	
	@GetMapping("notice.no")
	public String notice(@RequestParam(value="page", defaultValue="1") int currentPage, Model model, HttpServletRequest req) {// 공지는 사이드메뉴만 있음
		
		CategorySelect cs = new CategorySelect();
		cs.setBoardType("NOTICE");
		// GENERAL, QUESTION, NOTICE -> 일반, 문의, 공지
		
		
		int listCount = bService.getListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10); // 10개씩 보여주겠다
		ArrayList<Board> nList = bService.getBoardList(cs, pi); // 메소드 안에 해당 타입 넣으면 그거 가져옴
		// GENERAL -> 동행 WITH, 양도 GIVE, 후기 REVIEW
		
		
		
		if(!nList.isEmpty()) {
			model.addAttribute("nList", nList);
			model.addAttribute("pi",pi);
			model.addAttribute("listCount", listCount);
			model.addAttribute("loc", req.getRequestURI());
		}
		return "noticeList";
	}
	
	@GetMapping("noticeSearch.no")
	public String noticeSearch(@RequestParam(value="page", defaultValue="1") int currentPage,@RequestParam("searchWord") String searchWord, Model model) {
		if(searchWord.trim() == "") {
			return "redirect:notice.no";
		}else {
			int listCount = bService.getNoticeListCount(searchWord);
			PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
			ArrayList<Board> nList = bService.getNoticeSelect(searchWord,pi);
			
			if(!nList.isEmpty()) {
				model.addAttribute("nList", nList);
				model.addAttribute("pi", pi);
				model.addAttribute("listCount", listCount);
				model.addAttribute("searchWord", searchWord);
				
				return "noticeList";
			}else {
				throw new BoardException("검색에 실패하였습니다.");
			}
		}
	}
	
	@GetMapping("noticeSelect.no")
	public String noticeSelect(@RequestParam("boardNo") Integer boardNo, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpSession session) { // 선택한 공지 글번호 가져오기
		Member loginUser = (Member)session.getAttribute("loginUser");
		int no = 0;
		if(loginUser != null) {
			no = loginUser.getMemberNo();
		}
		
		
		Board board = bService.selectBoard(boardNo, no);
		
		ArrayList<Image> iList = bService.selectImage(boardNo);
		
		
		if(board != null) {
			model.addAttribute("n", board);
			model.addAttribute("page", page);
			model.addAttribute("iList", iList);
			
			return "noticeSelect";
		}else {
			throw new BoardException("해당 공지를 불러오는데 실패하였습니다.");
		}
	}
	
	@GetMapping("noticeWrite.no")
	public String noticeWrite() {
		return "noticeWrite";
	}
}
