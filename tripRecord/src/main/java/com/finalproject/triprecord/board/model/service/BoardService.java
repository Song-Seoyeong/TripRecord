package com.finalproject.triprecord.board.model.service;

import java.util.ArrayList;

import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.board.model.vo.GeneralBoard;
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

	int deleteReply(int rNo);

	int updateReply(Reply r);

	void insertReply(Reply r);

	int insertGeneralAsk(Board b);

	int insertQuestion(Board t);

	int getaskCategoryListCount(CategorySelect cs);

	ArrayList<Board> getCategorySelectQuestionList(CategorySelect cs, PageInfo pi);

	int getNoticeListCount(String searchWord);

	ArrayList<Board> getNoticeSelect(String searchWord, PageInfo pi);

	Question selectQuestion(Integer boardNo);

	int updateBoard(Board b);

	int delImg(String del);

	void updateQuestion(Integer pwd);

	int deleteImg(int boardNo);

	Image selectProfileImage(int i);

	void updateQuestion(Board t);

	String selectAskBoard(int boardNo);

	int countBoardList(int memberNo);

	void firstBoardPoint(int memberNo);

	
	
	//추가
	ArrayList<GeneralBoard> getGeneralAsk();



}
