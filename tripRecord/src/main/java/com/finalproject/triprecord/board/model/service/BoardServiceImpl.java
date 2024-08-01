package com.finalproject.triprecord.board.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.board.model.dao.BoardMapper;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
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


}