
<구글 드라이브 사진 업로드/삭제>

1. 깃에서 파일 받고 꼭 'credentials.json' 파일 추가하기
	>> 경로 : src/main/resources
	>> 파일은 단톡에 올려놓음

2. 구글 아이디 로그인 후 계속 사용 가능
	>> 1번 후 처음 서버켜면 콘솔에 url 뜸
	>> 생성된 url 복사 >> 인터넷에 복사한 url 넣고 구글 로그인 하기
	>> 계속 클릭 >> 계속 클릭
	>> 단톡에 알려준 구글 계정으로 로그인하면 흰 화면에 'Received verification code. You may now close this window' 뜨면 성공
	>> 서버 다시 껐다 켜면 됨
	(자세한건 단톡에도 올림)
	

3. 이미지 불러올때 경로 그대로 사용하기
	>> imageRename 넣으면 됨
	>> th:src="|https://drive.google.com/thumbnail?id=${i.imageRename}&sz=w1000|"
	>> sz=w1000 없으면 화질 깨져서 나옴

4. 본인 프로젝트를 본인 브랜치에 push 할때 'credentials.json' 포함하면 push가 안됩니다
	>> 비밀정보라 공개된 곳에 노출 불가