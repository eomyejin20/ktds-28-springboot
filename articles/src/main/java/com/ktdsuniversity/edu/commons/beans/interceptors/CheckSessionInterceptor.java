package com.ktdsuniversity.edu.commons.beans.interceptors;

import java.io.PrintWriter;

import org.springframework.web.servlet.HandlerInterceptor;

import com.google.gson.Gson;
import com.ktdsuniversity.edu.commons.util.ApiResponse;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckSessionInterceptor implements HandlerInterceptor{
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    		throws Exception {
    	// 세션을 검사하고
    	HttpSession session = request.getSession();
    	MembersVO membersVO = (MembersVO) session.getAttribute("__LOGIN_USER__");
    	
    	// 세션이 있으면 컨트롤러를 실행
    	if (membersVO != null) {
    		return true;
    	} else {
    		
    		// Response의 content-Type을 JSON(appliation/json)으로 설정
    		response.setContentType("application/json");
    		
    		//클라이언트가 표현할 인코딩을 UTF-8로 설정
    		response.setCharacterEncoding("UTF-8");
    		
    		// 클라이언트에게 응답메시지를 직접 전달할 수 있는 객체
    		// Servlet Code를 작성할 때에 필수 코드
    		PrintWriter printWriter  = response.getWriter();
    		ApiResponse<String> errorResponse = ApiResponse.FORBIDDEN("로그인이 필요한 기능입니다.");
    		// errorResponse => 객체를 JSON으로 변환! (1.Jackson Databind ==> @ResponseBody, 2.Gson)
    		Gson gson = new Gson();
    		String errorJson = gson.toJson(errorResponse);
    		// printWriter에게 write
    		printWriter.write(errorJson);
    		
//    		printWriter.write("{JSON 메시지 직접 작성}");
    		// printWriter에 작성한 내용들이 클라이언트에게 전달된다.
    		printWriter.flush();
    		
    		// 세션이 없으면 컨트롤러를 실행 X => 클라이언트에게 예외 메시지 전달
    		return false;
    	}
    	
//    	return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
