<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장소 상세 페이지&#x1F978;</title>
<link type="text/css" href='../../css/placeDetail.css' rel="stylesheet">
</head>
<body>
	<%@ include file='../common/logoBar.jsp'%>
	<%@ include file='../common/mainCategoryBar.jsp'%>
	
	<!-- 타이틀 영역 -->
	<div class="div" style='background-image: url("${contextPath}/resources/IMG_1152.JPG");'>
		<div class="text-content-title">
			<b class="title">엠파이어 스테이트 빌딩</b>
			<div class="subtitle">뉴욕에서 가장 대표되는 랜드마크이자 미국 마천루 역사의 상징인 건물</div>
		</div>
	</div>
	<!-- 타이틀 영역 -->
	
	<!-- 내용 출력 -->
	<div id='content'>내용 출력 칸</div>
	<!-- 내용 출력 -->
	
	<div class="line-div"></div>
	
	<%@ include file='../common/footer.jsp'%>
</body>
</html>