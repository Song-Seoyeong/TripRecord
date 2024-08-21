package com.finalproject.triprecord.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CheckPlannerSevenDaysSchedule implements HandlerInterceptor{
	
	private MemberService mService;
	
	@Autowired
	public CheckPlannerSevenDaysSchedule(MemberService mService) {
		this.mService = mService;
	}
	
	
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
		
		
		
		HttpSession session = request.getSession();
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser != null && !loginUser.getGrade().equals("ADMIN")) { // 플래너이거나 일반회원이면
			
			int result = mService.checkSevenSchedule(loginUser); // 로그인 유저가 작성한 글의 갯수
			
			if(result>0) { // 알림창을 띄울 것임
				
				String msg = result+"";
				
				session.setAttribute("notification", msg);
			}
		}
	}
}
