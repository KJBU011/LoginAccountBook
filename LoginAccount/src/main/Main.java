package main;

import java.util.InputMismatchException;
import java.util.Scanner;

import dao.LoginAccountDao;

public class Main {
	public static void main(String[] args) {
		
		// 스캐너 생성
		Scanner sc = new Scanner(System.in);
		
		// dao 호출
		LoginAccountDao dao = LoginAccountDao.getInstance();
		
		// 파일 불러오기
		dao.fileLoad();
		
		// 반복 작업을 위한 무한루프
		while (true) {
			System.out.println("===========[ 로그인 ]===========");
			System.out.println("| 1. 로그인");
			System.out.println("| 2. 등록");
			System.out.println("| 3. 탈퇴");
			System.out.println("| 4. 종료");
			System.out.println("==============================");
			System.out.print("[입력] >> ");
			String loginMenu = sc.next();
			System.out.println("------------------------------");

			if (loginMenu.equals("1")) {
				dao.logIn();
			} else if (loginMenu.equals("2")) {
				dao.signIn();
			} else if (loginMenu.equals("3")) {
				dao.signOut();
			} else if (loginMenu.equals("4")) {
				dao.fileSave();
				break;
			} else {
				System.out.println("------------------------------");
				System.out.println("[올바른 선택지를 입력해주십시오]");
			}

			// 로그인 성공 시 -> dao 내부의 index 값이 입력한 id, pw 와 일치하는 List 항목의 index 값으로 지정됨
			if(dao.getIndex() != -1) { // dao 내부의 index 값은 기본적으로 -1 이고 로그인 성공 시 해당 객체의 index 값으로 변경
				
				// 반복 작업을 위한 무한 루프
				while (true) {
					try { // 예외 처리 -> String 으로 받으면 안해도 될 부분
						// 메뉴 최상단에 로그인한 유저의 ID 표시
						System.out.println("===========[ " + dao.usrlogin.get(dao.getIndex()).getUsrId() +" ]===========");
						System.out.println("| 1. [가계부 검색]");
						System.out.println("| 2. [가계부 확인]");
						System.out.println("| 3. [가계부 추가]");
						System.out.println("| 4. [가계부 삭제]");
						System.out.println("| 5. [가계부 수정]");
						System.out.println("| 6. [로그아웃]");
						System.out.println("==============================");
						System.out.print("[입력] >> ");

						int selectMenu = sc.nextInt();
						System.out.println("------------------------------");

						switch (selectMenu) {
						case 1: // 검색
							dao.search();
							continue;
						case 2: // 조회
							dao.allPrint();
							continue;
						case 3: // 추가
							dao.insert();
							continue;
						case 4: // 삭제
							dao.delete();
							continue;
						case 5: // 수정
							dao.update();
							continue;
						case 6: // 종료
							// index 값 초기화
							dao.resetIndex();
							dao.fileSave();
							break;
						default:
							System.out.println("------------------------------");
							System.out.println("[메뉴의 숫자 중에 골라주십시오]");
							continue;
						}
						
						// continue 가 아닌 break 로만 도달할 수 있는 탈출(로그아웃)
						break;

					} catch (InputMismatchException e) {
						System.out.println("------------------------------");
						System.out.println("[숫자로 입력해주십시오]");
						sc.nextLine();
					}
				}
			}
		}

	}

}
