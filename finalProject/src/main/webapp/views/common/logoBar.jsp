<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>


<!-- jQuerylib -->
<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>

<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.memberLink{color: black; text-decoration: none;}
	body{font-family: 'NanumSquareRound'; margin:0px;}
	#headerLogo{width: 134px; height: 57px;}
	#loginUserName{font-weight: bold; font-size: 1.3em;}
</style>

<!-- **필수** 부트스트랩 : 상단 태그 **필수** -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

</head>
<body>
	<!-- contextPath -->
	<c:set var='contextPath' value='${ pageContext.servletContext.contextPath }' scope='application'/>
	
	<div class="row container-fluid" style='margin: 20px 0px;'>
		  <div class="col-4 text-start" style='padding-left:30px;'>
		  	<a href="#" class="memberLink">고객센터</a>
		  </div>
	      <div class="col-4 text-center">
        	<a href="${ contextPath }">
				<img alt="logo" src="${ contextPath }/resources/Logo-skyblue.png" id='headerLogo'>
			</a>
		  </div>
		  
		  <!-- 로그인 유무에 따라 변경 -->
	      <div class="col-4 text-end">
		        <a href="#" class="memberLink">회원가입</a>
		        &nbsp;&nbsp;|&nbsp;&nbsp;
				<a href="#" class="memberLink">로그인</a>&nbsp;&nbsp;&nbsp;&nbsp;
	     </div>
	</div>
</body>
</html>