package com.finalproject.triprecord.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class DeleteNotification implements HandlerInterceptor{
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		HttpSession session = request.getSession(false);
		
		if (session != null) { // Check if session exists
            if (session.getAttribute("notification") != null) { // Check if "notification" attribute exists
                session.removeAttribute("notification"); // Remove the attribute
            }
        }
		
	}
}
