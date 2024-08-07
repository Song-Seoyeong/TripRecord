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
		
		//System.out.println(cs);
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
		// 위 2개의 카테고리를 반복 설정? 클릭 시 어느 순간 에러가 남
		
		//System.out.println(cs);
		
		int listCount = bService.getCategorySelectListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Board> cList = bService.getCategorySelectBoardList(cs, pi);
		
		//System.out.println(cList);
		
		if(!cList.isEmpty()) {
			model.addAttribute("cList", cList);
			model.addAttribute("pi",pi);
			model.addAttribute("loc", req.getRequestURI());
			model.addAttribute("listCount", listCount);
			model.addAttribute("generalType", cs.getGeneralType());
			model.addAttribute("localName", cs.getLocalName());
			//model.addAttribute("boardType", "GENERAL");    // 현재 커뮤니티 게시판 == 무조건 GENERAL
		} else {
			model.addAttribute("nothing", "nothing");
		}
		return "commuList";
	}
	
	@GetMapping("commuWrite.bo")
	public String commuWrite() {
		return "communityWrite";
	}
	
	@GetMapping("commuEdit.bo")
	public String commuEdit(@RequestParam("boardNo") int bNo, Model model) {
		Board b = bService.selectBoard(bNo, 0);
		ArrayList<Image> iList = bService.selectImage(bNo);
		
		if(b != null) {
			model.addAttribute("b", b);
			model.addAttribute("iList", iList);
		}
		return "communityEdit";
	}
	
	@GetMapping("commuDelete.bo")
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
		ArrayList<Reply> rList = bService.selectReply(boardNo);
		//System.out.println(rList);
		/* 댓글 작성자들 썸네일 사진도 가져와야 함 */
		if(board != null) {
			model.addAttribute("b", board);
			model.addAttribute("page", page);
			model.addAttribute("rList", rList);
			model.addAttribute("generalType", generalType);
			model.addAttribute("loc", req.getRequestURI());
			model.addAttribute("iList", iList);
			//System.out.println(rList);
			return "communitySelect";
		} else {
			return "NOT";
		}
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
			return "NOT";
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
			json.put("replyId", reply.getReplyNo());
			json.put("replyContent", reply.getReplyContent());
			json.put("replyBoardId", reply.getBoardNo());
			json.put("replyWriterNo", reply.getReplyWriterNo());
			json.put("nameNick", reply.getNickname());
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
	public String ask(@RequestParam(value="page", defaultValue="1") int currentPage, Model model, HttpServletRequest req) {// 문의는 사이드 + 카테고리

		CategorySelect cs = new CategorySelect();
		cs.setBoardType("QUESTION");
		// GENERAL, QUESTION, NOTICE -> 일반, 문의, 공지
		
		
		int listCount = bService.getListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10); // 10개씩 보여주겠다
		ArrayList<Board> aList = bService.getBoardList(cs, pi); // 메소드 안에 해당 타입 넣으면 그거 가져옴
		// GENERAL -> 동행 WITH, 양도 GIVE, 후기 REVIEW
		ArrayList<Question> qList = bService.getQuestionList(0);
		
		//System.out.println(aList);
		//System.out.println(qList);
		
		if(!aList.isEmpty()) {
			model.addAttribute("aList", aList); // 보드
			model.addAttribute("pi",pi);
			model.addAttribute("loc", req.getRequestURI());
			model.addAttribute("qList", qList); // 문의(글번호, 비번, 답변, 답변YN)
			model.addAttribute("generalType","ALL");
			//model.addAttribute("boardType","GENERAL");
		}
		return "askList";
	}
	
	@GetMapping("askSelect.no")
	public String askSelect(@RequestParam("boardNo") Integer boardNo, @RequestParam(value="page", defaultValue="1") int page, Model model) { // 선택한 글번호 넘겨받기
		
		Board board = bService.selectBoard(boardNo, 0);
		ArrayList<Image> iList = bService.selectImage(boardNo);
		
		if(board != null) {
			model.addAttribute("n", board);
			model.addAttribute("page", page);
			model.addAttribute("iList", iList);
			return "askSelect";
		}else {
			return "NOT";
		}
		
	}
	
	@GetMapping("askWrite.no")
	public String askWrite() {
		return "askWrite";
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
			model.addAttribute("loc", req.getRequestURI());
		}
		return "noticeList";
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
			return "NOT";
		}
	}
	
	@GetMapping("noticeWrite.no")
	public String noticeWrite() {
		return "noticeWrite";
	}
}
