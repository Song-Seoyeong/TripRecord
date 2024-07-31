package com.finalproject.triprecord.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.board.model.vo.Reply;
import com.finalproject.triprecord.common.model.vo.Image;

@Mapper
public interface BoardMapper {

	ArrayList<Board> getBoardList(RowBounds rb, CategorySelect cs);

	int getListCount(CategorySelect cs);

	int getCategorySelectListCount(CategorySelect cs);

	ArrayList<Board> getCategorySelectBoardList(RowBounds rb, CategorySelect cs);

	Board selectBoard(Integer boardNo);

	void updateCount(Integer boardNo);

	ArrayList<Reply> selectReply(Integer boardNo);

	int insertBoard(Board b);

	int insertGeneralBoard(Board b);

	int selectBoardNo();

	int insertImage(ArrayList<Image> list);

	void deleteBoard(int bNo);

	ArrayList<Image> selectImage(Integer boardNo);


}
