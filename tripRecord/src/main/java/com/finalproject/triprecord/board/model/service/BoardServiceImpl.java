package com.finalproject.triprecord.board.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.board.model.dao.BoardMapper;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.board.model.vo.GeneralBoard;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.board.model.vo.Reply;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper bMapper;
	
	@Override
	public ArrayList<Board> getBoardList(CategorySelect cs, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return bMapper.getBoardList(rb, cs);
	}

	@Override
	public int getListCount(CategorySelect cs) {
		return bMapper.getListCount(cs);
	}

	@Override
	public int getCategorySelectListCount(CategorySelect cs) {
		return bMapper.getCategorySelectListCount(cs);
	}

	@Override
	public ArrayList<Board> getCategorySelectBoardList(CategorySelect cs, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return bMapper.getCategorySelectBoardList(rb, cs);
	}

	@Override
	public Board selectBoard(Integer boardNo, int id) {
		Board b = bMapper.selectBoard(boardNo);
		if(id != 0 && id != b.getBoardWriterNo()) {
			bMapper.updateCount(boardNo);
			b.setBoardCount(b.getBoardCount() + 1);
		}
		return b;
		
	}

	@Override
	public ArrayList<Reply> selectReply(Integer boardNo) {
		return bMapper.selectReply(boardNo);
	}

	@Override
	public int insertBoard(Board b) {
		return bMapper.insertBoard(b);
	}

	@Override
	public int insertGeneralBoard(Board b) {
		return bMapper.insertGeneralBoard(b);
	}

	@Override
	public int selectBoardNo() {
		return bMapper.selectBoardNo();
	}

	@Override
	public int insertImage(ArrayList<Image> list) {
		return bMapper.insertImage(list);
	}

	@Override
	public void deleteBoard(int bNo) {
		bMapper.deleteBoard(bNo);
	}

	@Override
	public ArrayList<Image> selectImage(Integer boardNo) {
		return bMapper.selectImage(boardNo);
	}

	@Override
	public ArrayList<Question> getQuestionList(int i) {
		return bMapper.getQuestionList(i);
	}

	@Override
	public void insertReply(Reply r) {
		bMapper.insertReply(r);
	}

	@Override
	public int deleteReply(int rNo) {
		return bMapper.deleteReply(rNo);
	}

	@Override
	public int updateReply(Reply r) {
		return bMapper.updateReply(r);
	}

	@Override
	public int insertGeneralAsk(Board b) {
		return bMapper.insertGeneralAsk(b);
	}

	@Override
	public int insertQuestion(Board t) {
		return bMapper.insertQuestion(t);
	}

	@Override
	public int getaskCategoryListCount(CategorySelect cs) {
		return bMapper.getaskCategoryListCount(cs);
	}

	@Override
	public ArrayList<Board> getCategorySelectQuestionList(CategorySelect cs, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return bMapper.getCategorySelectQuestionList(rb,cs);
	}

	@Override
	public int getNoticeListCount(String searchWord) {
		return bMapper.getNoticeListCount(searchWord);
	}

	@Override
	public ArrayList<Board> getNoticeSelect(String searchWord, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return bMapper.getNoticeSelect(rb,searchWord);
	}

	@Override
	public Question selectQuestion(Integer boardNo) {
		return bMapper.selectQuestion(boardNo);
	}

	@Override
	public int updateBoard(Board b) {
		return bMapper.updateBoard(b);
	}

	@Override
	public int delImg(ArrayList<String> deleteImg) {
		return bMapper.deleteImg(deleteImg);
	}

	@Override
	public void updateQuestion(Integer pwd) {
		bMapper.updateQuestion(pwd);
	}

	@Override
	public int deleteImg(int boardNo) {
		return bMapper.deleteImages(boardNo);
	}

	@Override
	public Image selectProfileImage(int i) {
		return bMapper.selectProfileImage(i);
	}

	@Override
	public void updateQuestion(Board t) {
		bMapper.updateQuestion(t);
	}

	@Override
	public String selectAskBoard(int boardNo) {
		return bMapper.selectAskBoard(boardNo);
	}

	@Override
	public int countBoardList(int memberNo) {
		return bMapper.countBoardList(memberNo);
	}

	@Override
	public void firstBoardPoint(int memberNo) {
		bMapper.firstBoardPoint(memberNo);
	}

	
	// plus
	@Override
	public ArrayList<GeneralBoard> getGeneralAsk() {
		return bMapper.getGeneralAsk();
	}


}
