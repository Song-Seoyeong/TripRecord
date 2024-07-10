<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
#mainCategoryBar {
	height: 70px;
	width: 100%;
	border-bottom: 3px solid #96CAF0;
	margin: 0px;
}
.col{
	position: relative;
	height: 100%;
	display: flex;
	justify-content: center;
	align-items: center;
}
.mainCateName:hover {
	font-weight: bold;
	color: white;
}

.mainCateName {
	font-size: 25px;
	color: black;
	text-decoration: none;
}

.hoverDiv{
	height: 35px;
	border-radius: 20px;
	display: flex;
	justify-content: center;
	align-items: center;
}

</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="container-fluid text-center" style='margin: 0px; padding: 0px;'>
		<div class="row container-fluid" id='mainCategoryBar'>
			<div class='col'></div>
		    <div class="col">
		    	<div class='hoverDiv' style='width: 190px;'>
		    		<a href='#' class='mainCateName' id='plannerMatching'>플래너 1:1 매칭</a>
		    	</div>
		    </div>
		    <div class="col">
		    	<div style='width: 80px;' class='hoverDiv'>
		    		<a href='#' class='mainCateName' id='schedule'>일정</a>
		    	</div>
		    </div>
		    <div class="col">
		    	<div class='hoverDiv' style='width: 125px;'>
					<a href='${ contextPath }/views/place/recommendPlace.jsp' class='mainCateName' id='recommendedPlace'>추천 장소</a>
				</div>
			</div>
		    <div class="col">
		    	<div class='hoverDiv' style='width: 120px;'>
					<a href='#' class='mainCateName' id='community'>커뮤니티</a>
				</div>
			</div>
			<div class='col'></div>
		</div>
	</div>
	
	<script>
		const mainCateNames = document.getElementsByClassName('mainCateName');
		
		for(const mainCateName of mainCateNames){
			const menuDiv = mainCateName.parentElement;
			
			mainCateName.addEventListener('mouseenter', function(){
				menuDiv.style.background = '#96CAF0'
			})
			mainCateName.addEventListener('mouseout', () =>{
				menuDiv.style.background = 'none'
			})
		}
	</script>
</body>
</html>
