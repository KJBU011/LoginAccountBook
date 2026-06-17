package dao;

public interface LoginAccountInterface {
	
	// 로그인
	public void logIn();
	
	// 회원가입
	public void signIn();

	// 회원탈퇴
	public void signOut();

	// 검색
	public void search();

	// 조회
	public void allPrint();

	// 추가
	public void insert();

	// 삭제
	public void delete();

	// 수정
	public void update();

	// 저장
	public void fileSave();

	// 불러오기
	public void fileLoad();
}
