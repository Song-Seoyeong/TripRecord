package com.finalproject.triprecord.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.finalproject.triprecord.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null || !loginUser.getGrade().equals("ADMIN")) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("<script>alert('접근 권한이 없습니다.'); location.href='/';</script>");
			
			return false;
		}
		
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
