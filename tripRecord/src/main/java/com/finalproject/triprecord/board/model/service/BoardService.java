package com.finalproject.triprecord.board.model.service;

import java.util.ArrayList;

import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.board.model.vo.Reply;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;

public interface BoardService {

	ArrayList<Board> getBoardList(CategorySelect cs, PageInfo pi);

	int getListCount(CategorySelect cs);

	int getCategorySelectListCount(CategorySelect cs);

	ArrayList<Board> getCategorySelectBoardList(CategorySelect cs, PageInfo pi);

	Board selectBoard(Integer boardNo, int id);

	ArrayList<Reply> selectReply(Integer boardNo);

	int insertBoard(Board b);

	int insertGeneralBoard(Board b);

	int selectBoardNo();

	int insertImage(ArrayList<Image> list);

	void deleteBoard(int bNo);

	ArrayList<Image> selectImage(Integer boardNo);

	ArrayList<Question> getQuestionList(int i);



}
