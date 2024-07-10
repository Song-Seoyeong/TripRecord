<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
    footer {
        width: 100%;
        height: 230px;
        display: flex;
        justify-content: flex-end; /* Right-align the column-container */
        align-items: center; /* Center vertically */
        background: #2F6598;
        position: relative;
        padding-right: 15px; /* Optional: add some padding to the right */
    }
    .logo {
        width: 145px;
        height: 59px;
        position: absolute;
        top: 50%;
        left: 15%;
        transform: translate(-50%, -50%);
    }
    .column-container {    
        display: flex;
        justify-content: flex-end; 
        width: 100%;
        max-width: 900px;
        height: 100%; 
        align-items: center;
        gap: 10px; 
    }
    .column {
        width: 400px;
        height: 120px;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: flex-start;
        gap: 12px;
    }
    .column-title {
        align-self: stretch;
        height: 38px;
        display: flex;
        justify-content: flex-start;
        align-items: flex-start;
        gap: 8px;
        color: white;
        font-weight: 700;
        font-size: 14px;
        word-wrap: break-word;
    }
    .column-item {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        color: white;
        font-size: 14px;
        line-height: 22.4px;
        word-wrap: break-word;
    }
</style>
</head>
<body>
    <footer>
        <img class="logo" src="resources/Logo-white.png" alt="Logo" />
        <div class="column-container">
            <div class="column">
                <div class="column-title">Info</div>
                <div class="column-item">사업자 번호 : 0123456789</div>
                <div class="column-item">주소 : 서울특별시 종로구 종로동 종로빌딩 3F</div>
                <div class="column-item">상호 : 여기 | 대표 : 박신우 | 개인정보</div>
            </div>
            <div class="column">
                <div class="column-item">문의센터 : 02)999-9999</div>
                <div class="column-item">Copyright 2024 TripRecord</div>
                <div class="column-item">이용약관</div>
                <div class="column-item">개인정보처리방침</div>
            </div>
        </div>
    </footer>
</body>
</html>
