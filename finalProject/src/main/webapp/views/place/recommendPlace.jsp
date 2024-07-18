<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
	
</style>
<meta charset="UTF-8">
<title>'여기'가 추천하는 여행장소 :)</title>
<link type="text/css" href='../../css/recommendPlace.css' rel="stylesheet">
</head>
<body>
	<%@ include file='../common/logoBar.jsp'%>
	<%@ include file='../common/mainCategoryBar.jsp'%>
	<!-- ------------------------------------------------------------------------------------ -->
	
	<div id='menuTitle'><h1>여기가 추천하는 여행지 &#x1F5FA;</h1></div>
	<div class="div">
		<!-- 필터 메뉴 -->
		<div class="filter-menu">
			<div class="section" id="locations-section">
				<div class="text">
					<div class="text1">선택한 지역</div>
				</div>
				<div class="keyword-list" id="selected-locations">
				</div>
			</div>
			<div class="section" id="keywords-section">
				<div class="text">
					<div class="text1">선택한 키워드</div>
				</div>
				<div class="keyword-list" id="selected-keywords">
					<%-- <div class="tag">
						<div class="tag1">Spring</div>
						<img class="x-icon" alt="" src="${ contextPath }/resources/Icon.png">
					</div> --%>
				</div>
			</div>
			<div class="checkbox-group">
				<div class="text"  >
					<a class="text1 group" data-bs-toggle="collapse" href="#localDiv" aria-expanded="false" aria-controls="localDiv"  style='color: black; text-decoration: none; font-weight: blod;'>지역</a>
				</div>
				<div class="checkbox-group collapse" id='localDiv'>
					<div class="checkbox-field">
						<div class="checkbox-and-label">
							<input type='checkbox' id='seoul' value='seoul' name='local'>
							<label for='seoul'>서울</label>
						</div>
					</div>
					<div class="checkbox-field">
						<div class="checkbox-and-label">
							<input type='checkbox' id='busan' value='busan'  name='local'>
							<label for='busan'>부산</label>
						</div>
					</div>
					<div class="checkbox-field">
						<div class="checkbox-and-label">
							<input type='checkbox' id='gangwondo' value='gangwondo'  name='local'>
							<label for='gangwondo'>강원도</label>
						</div>
					</div>
				</div>
			</div>
			<div class="section2">
				<div class="text">
					<a class="text1 group" data-bs-toggle="collapse" href="#filter" aria-expanded="false" aria-controls="filter"  style='color: black; text-decoration: none; font-weight: blod;'>필터</a>
				</div>
				<div class="checkbox-group collapse" id='filter'>
					<div class="checkbox-field">
						<div class="checkbox-and-label">
							<input type='checkbox' id='ticket' value='ticket'  name='keyword'>
							<label for='ticket'>티켓</label>
						</div>
					</div>
					<div class="checkbox-field">
						<div class="checkbox-and-label">
							<input type='checkbox' id='lodging' value='lodging' name='keyword'>
							<label for='lodging'>숙소</label>
						</div>
					</div>
					<div class="checkbox-field">
						<div class="checkbox-and-label">
							<input type='checkbox' id='place' value='place' name='keyword'>
							<label for='place'>관광지</label>
						</div>
					</div>
					<div class="checkbox-field">
						<div class="checkbox-and-label">
							<input type='checkbox' id='rastaurant' value='rastaurant' name='keyword'>
							<label for='rastaurant'>맛집</label>
						</div>
					</div>
				</div>
			</div>
			<div class="slider-field"></div>
		</div>
		<!-- 필터 메뉴 -->
		
		<!-- <script>
			document.getElementById('location').addEvenetListner('click', ()=>{
				
			})
			
		</script> -->
		
		
		<!-- 컨텐츠 카드 -->
		<div class="section-product-grid">
			<!-- 검색바 -->
			<div class="filter-bar row">
				<div class='col-4'></div>
				<div class="search-filter col-4">
					<div class="search">
						<input type='text' placeholder='search' id='search' name='search' size='25'>
						<img class="x-icon" alt="" src="${ contextPath }/resources/Search.png">
					</div>
				</div>
				<div class='col-2'></div>
				<div class='col-2'>
					<select class="form-select" aria-label="Sort by">
		                <option selected>정렬 기준</option>
		                <option value="1">리뷰 많은 순 ↑</option>
		                <option value="2">별점 높은 순 ↑</option>
		                <option value="3">조회수 높은 순 ↑</option>
	           	 	</select>
           	 	</div>
			</div>
			<!-- 검색바 -->
			<div class="card-grid row">
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
				<div class='col'>
					<div class="product-info-card">
						<img class="image-icon" alt="" src="${ contextPath }/resources/Image.png">
						<div class="body">
							<div class="text6">
								<div class="text7">Text</div>
							</div>
							<div class="text">
								<div class="text-strong1">content</div>
							</div>
							<div class="text-small"></div>
						</div>
					</div>
				</div>
			</div>
			<div class='row' style='margin-top: 50px;'>
				<div class='col-4'></div>
				<nav aria-label="Page navigation" class='col-4 d-flex justify-content-center'>
				  <ul class="pagination">
				    <li class="page-item">
				      <a class="page-link" href="#" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				      </a>
				    </li>
				    <li class="page-item"><a class="page-link" href="#">1</a></li>
				    <li class="page-item"><a class="page-link" href="#">2</a></li>
				    <li class="page-item"><a class="page-link" href="#">3</a></li>
				    <li class="page-item">
				      <a class="page-link" href="#" aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				      </a>
				    </li>
				  </ul>
				</nav>
				<div class='col-4'></div>
			</div>
		</div>
	</div>
	
	<!-- ------------------------------------------------------------------------------------ -->
	<%@ include file='../common/footer.jsp'%>
	
	<script>
		document.addEventListener('DOMContentLoaded', ()=> {
			const localCheckboxes = document.querySelectorAll('input[name="local"]');
			const keywordCheckboxes = document.querySelectorAll('input[name="keyword"]');
		
			// 초기화
		    updateTags("local", 'selected-locations', 'locations-section');
		    updateTags('keyword', 'selected-keywords', 'keywords-section');
		    
			localCheckboxes.forEach(checkbox => {
			    checkbox.addEventListener('change', () => {
			        updateTags('local', 'selected-locations', 'locations-section');
			    });
			});
		
			keywordCheckboxes.forEach(checkbox => {
			    checkbox.addEventListener('change', () => {
			        updateTags('keyword', 'selected-keywords', 'keywords-section');
			    });
			});
		})
		
		function updateTags(name, targetId, sectionId){
		    //console.log(`updateTags 호출됨: name=${name}, targetId=${targetId}, sectionId=${sectionId}`);

		    const targetDiv = document.getElementById(targetId);
		    const sectionDiv = document.getElementById(sectionId);
		    
		   /*  console.log('targetDiv:', targetDiv);
		    console.log('sectionDiv:', sectionDiv); */
		    
		    if (!targetDiv || !sectionDiv) {
		        console.error(`Element not found: targetId=${targetId}, sectionId=${sectionId}`);
		        return;
		    }
		    
		   	const paramName = sectionDiv.id.split("s")[0];
		   	console.log(paramName);
		    
		    targetDiv.innerHTML = '';
			
		    let checkedCheckboxes;
		    if(paramName == 'location'){
		    	checkedCheckboxes = document.querySelectorAll(`input[name='local']:checked`);
		    }else{
		    	checkedCheckboxes = document.querySelectorAll(`input[name='keyword']:checked`);
		    }
		    
		    //const checkedCheckboxes = document.querySelectorAll(`input[name=paramName]:checked`);
		    //console.log(checkedCheckboxes);
		    if (checkedCheckboxes.length > 0) {
		        sectionDiv.style.display = 'block';
		    } else {
		        sectionDiv.style.display = 'none';
		    }
	
		    checkedCheckboxes.forEach(checkbox => {
		    	
		        const tagDiv = document.createElement('div');
		        tagDiv.className = 'tag';
		        const tagText = document.createElement('div');
		        tagText.className = 'tag1';
		        tagText.innerText = checkbox.value;
		        const closeButton = document.createElement('img');
		        closeButton.className = 'x-icon';
		        closeButton.src = '${contextPath}/resources/Icon.png';
		        
		        closeButton.addEventListener('click', function() {
		            checkbox.checked = false;
		            updateTags(name, targetId, sectionId);
		        });
	
		        tagDiv.appendChild(tagText);
		        tagDiv.appendChild(closeButton);
		        targetDiv.appendChild(tagDiv);
		    });
		}
	</script>
</body>
</html>