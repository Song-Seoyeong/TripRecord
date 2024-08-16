package com.finalproject.triprecord.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.finalproject.triprecord.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			String url = request.getRequestURI();
			
			String msg = null;
			if(url.contains("planMain.pl") || url.contains("planDetail.pl") || url.contains("placeReviewWrite.pla") || url.contains("commuWrite.bo")
			 		|| url.contains("insertQuestion.no") || url.contains("commuEdit.bo") || url.contains("insertBoard.bo") || url.contains("askWrite.no")
			 		|| url.contains("editQuestion.no")|| url.contains("matchingRequest.ma") || url.contains("matchingReview.ma") || url.contains("updateReviewView.ma")) {
				msg = "로그인 후 이용하세요.";
			} else {
				msg = "로그인 세션이 만료되어 로그인 화면으로 넘어갑니다.";
			}
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().append("<script> alert('" + msg + "'); location.href='loginView.me'; </script>");
			
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
