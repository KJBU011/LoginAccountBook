package dao;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import dto.AccountDto;
import dto.LoginDto;
import file.FileProc;

public class LoginAccountDao implements LoginAccountInterface{
	
	// Singleton 구조
	public static LoginAccountDao dao = null;

	// 스캐너 생성
	Scanner sc = new Scanner(System.in);

	// list 생성
	public LinkedList<LoginDto> usrlogin = new LinkedList<LoginDto>();

	// 로그인 성공 시 해당 index 값을 저장하기 위한 변수 생성 -> 기본값 : -1
	private int index = -1;
	
	// FileProc 호출
	FileProc file = new FileProc();

	// Singleton 구조로 사용하기 위해 기본 생성자 private
	private LoginAccountDao() {}

	// 객체 생성을 하나로 제한
	public static LoginAccountDao getInstance() {
		if (dao == null) {
			dao = new LoginAccountDao();
		}
		return dao;
	}
	
	// index 값을 Main 에서도 호출하기 위한 getter
	public int getIndex() {
		return index;
	}
	
	// 로그아웃 시 Main 에서 index 값을 다시 초기화 하기 위한 함수
	public void resetIndex() {
		index = -1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// Override
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override // 로그인
	public void logIn() {
		// 탐색 깃발 내려두기
		boolean finder = false;
		// 입력받을 변수 미리 선언해두기 -> 반복문 밖에서 다시 사용할 일이 없어서 입력받을 때 선언해도 무방
		String inputId;
		String inputPw;
		System.out.print("[아이디] : ");
		inputId = sc.next();
		// 입력받은 Id 가 List 에서 일치하는 항목이 있는지 탐색
		for (int i = 0; i < usrlogin.size(); i++) {
			// 탐색 성공시 깃발을 올린 후 Pw 입력
			if (usrlogin.get(i).getUsrId().equals(inputId)) {
				finder = true;
				System.out.print("[비밀번호] : ");
				inputPw = sc.next();
				// 입력받은 Pw 가 탐색했던 항목의 Pw 와 일치하는 지 검사
				if (usrlogin.get(i).getUsrPw().equals(inputPw)) {
					// 일치하다면 해당 index 값을 저장 후 탈출
					System.out.println("------------------------------");
					System.out.println("[로그인에 성공하였습니다]");
					index = i;
					break;
				}
				// Pw 불일치 시
				else {
					System.out.println("------------------------------");
					System.out.println("[아이디와 비밀번호가 일치하지 않습니다]");
				}
			}
		}
		// 탐색을 마친 후에 깃발이 내려가 있을 경우
		if (finder == false) {
			System.out.println("------------------------------");
			System.out.println("[존재하지 않는 ID 입니다]");
		}

	}

	@Override // 신규 등록
	public void signIn() {
		// 탐색 깃발 내려두기
		boolean finder = false;
		String inputId;
		String inputPw;
		String recheckPw;
		// 반복 작업을 위한 루프
		while (true) {
			System.out.println("===========[ 등록 ]===========");
			System.out.print("[아이디 등록] : ");
			inputId = sc.next();
			// 반복문을 통해 List 에 일치하는 Id 가 있는 지 확인 -> continue 를 사용하여 깃발이 없어도 가능
			for (LoginDto dto : usrlogin) {
				if (dto.getUsrId().equals(inputId)) {
					finder = true;
					System.out.println("------------------------------");
					System.out.println("[이미 존재하는 ID 입니다]");
					continue;
				}
			}
			// 같은 Id 가 존재하지 않을 때 신규 Id, Pw 생성
			if (finder == false) {
				while (true) {
					System.out.print("[비밀번호 등록] : ");
					inputPw = sc.next();
					// Pw 가 기준에 충족하지 못했을시 false 로 return 하는 함수
					if (makePw(inputPw, 7)) {
						System.out.print("[비밀번호 재확인] : ");
						recheckPw = sc.next();
						// 재입력 받은 Pw 가 그 전의 Pw 와 일치하면 신규 List 등록
						if (recheckPw.equals(inputPw)) {
							// LinkedList 의 특성을 이용해 인덱스 번호 마지막으로 추가 -> 그냥 add 로 사용해도 무방
							usrlogin.addLast(new LoginDto(inputId, inputPw)); // Id 와 Pw 만으로 생성하는 생성자
							System.out.println("------------------------------");
							System.out.println("[회원가입이 완료되었습니다]");
							break;
						} else {
							System.out.println("------------------------------");
							System.out.println("[비밀번호가 일치하지 않습니다]");
						}
					}
				}
			}
			// 신규 등록에 성공하고 나왔을 경우 메뉴로 돌아가기 위한 break
			break;
		}
	}
	
	@Override // 탈퇴
	public void signOut() {
		// 탐색 깃발 내려두기
		boolean finder = false;
		String inputId;
		String inputPw;
		System.out.println("===========[ 탈퇴 ]===========");
		System.out.print("[탈퇴 아이디] : ");
		inputId = sc.next();
		// 입력받은 Id 와 일치하는 Id 탐색
		for (int i = 0; i < usrlogin.size(); i++) {
			// 일치하는 Id 가 존재할 경우
			if (usrlogin.get(i).getUsrId().equals(inputId)) {
				finder = true; // 깃발 올리기
				System.out.print("[비밀번호] : ");
				inputPw = sc.next();
				// Id 와 Pw 가 일치하는 지 검사
				if (usrlogin.get(i).getUsrPw().equals(inputPw)) {
					while (true) {
						// 랜덤 문자열 검사
						String rans = randomString(5);
						System.out.println("------------------------------");
						System.out.println("[아래 문자를 동일하게 입력하면 정보가 삭제됩니다]");
						System.out.println("[돌아가시려면 \"취소\"를 입력해주십시오]");
						System.out.println("------------------------------");
						System.out.print("[" + rans + "] : ");
						String delTest = sc.next();
						if (delTest.equals(rans)) { // 검사 성공시
							usrlogin.remove(i);
							System.out.println("------------------------------");
							System.out.println("[탈퇴가 완료되었습니다]");
							file.save();
							break;
						} else if (delTest.equals("취소")) { // 취소 입력시
							System.out.println("------------------------------");
							System.out.println("[메뉴로 돌아갑니다]");
							break;
						} else {
							System.out.println("------------------------------");
							System.out.println("[문자가 동일하게 입력되지 않았습니다]");
							continue;
						}
					}
				}
				// Id 와 Pw 가 일치하지 않을 경우
				else {
					System.out.println("------------------------------");
					System.out.println("[아이디와 비밀번호가 일치하지 않습니다]");
				}
			}
		}
		// 일치하는 Id 를 탐색하지 못했을 경우
		if (finder == false) {
			System.out.println("------------------------------");
			System.out.println("[존재하지 않는 ID 입니다]");
		}
	}
	
	@Override
	public void search() {
		while (true) {
			try { // 예외 처리
				System.out.println("==============[ 가계부 검색 ]================");
				System.out.println("| 1. [내용 검색]");
				System.out.println("| 2. [날짜 검색]");
				System.out.println("| 3. [메뉴로 돌아가기]");
				System.out.println("==========================================");
				System.out.print("[입력] >> ");

				int selectSearch = sc.nextInt();
				System.out.println("------------------------------------------");

				// 내용 검색
				if (selectSearch == 1) {
					System.out.println("[검색할 내용을 입력해주십시오]");
					System.out.println("------------------------------------------");
					System.out.print("[입력] >> ");
					String contentInput = sc.next();

					// 함수를 통해 index 추출
					if (searchContent(contentInput) == -1) {
						System.out.println("------------------------------------------");
						System.out.println("[검색 결과를 찾을 수 없습니다]");
					} else { // 함수를 통해 추출한 index 를 이용한 함수를 통해 출력
						print(searchContent(contentInput));
					}

				}

				else if (selectSearch == 2) {
					System.out.println("[검색할 날짜를 입력해주십시오 < 예시) 2026-06 >]");
					System.out.println("------------------------------------------");
					String dateInput = sc.next();

					// 함수를 통해 index 추출
					if (searchDate(dateInput) == -1) {
						System.out.println("------------------------------------------");
						System.out.println("[검색 결과를 찾을 수 없습니다]");
					} else {
						print(searchDate(dateInput));
					}

				}
				
				// 메뉴로 돌아가기 -> 탈출
				else if(selectSearch == 3) {
					System.out.println("------------------------------------------");
					System.out.println("[메뉴로 돌아갑니다]");
					break;
				}

				else { // 선택지 외의 숫자 입력 시
					System.out.println("------------------------------------------");
					System.out.println("[메뉴의 숫자 중에 골라주십시오]");
				}

			} catch (InputMismatchException e) {
				System.out.println("------------------------------------------");
				System.out.println("[숫자로 입력해주십시오]");
				sc.nextLine();
			}
		}
		
	}
	
	@Override
	public void allPrint() { // 출력
		// 예외 시 돌아올 수 있도록 무한 루프
		while (true) {
			try { // 예외 처리
				System.out.println("==============[ 가계부 확인 ]================");
				System.out.println("| 1. [최신 순]");
				System.out.println("| 2. [과거 순]");
				System.out.println("| 3. [금액 높은 순]");
				System.out.println("| 4. [금액 낮은 순]");
				System.out.println("| 5. [입금 조회]");
				System.out.println("| 6. [출금 조회]");
				System.out.println("| 7. [메뉴로 돌아가기]");
				System.out.println("==========================================");
				System.out.print("[입력] >> ");

				int selectSearch = sc.nextInt();
				System.out.println("------------------------------------------");

				// 최신 순 
				if (selectSearch == 1) {
					UptoDown(); // 날짜 데이터를 뽑아내 내림차순으로 정렬하는 함수
					// 반복문으로 전체 출력
					for (AccountDto dto : usrlogin.get(index).usr) {
						print(dto);
					}

				}

				else if (selectSearch == 2) {
					DowntoUp(); // 날짜 데이터를 뽑아내 오름차순으로 정렬하는 함수
					// 반복문으로 전체 출력
					for (AccountDto dto : usrlogin.get(index).usr) {
						print(dto);
					}

				}

				else if (selectSearch == 3) {
					moneyUp();// 금액 데이터를 뽑아내 내림차순으로 정렬하는 함수
					// 반복문으로 전체 출력
					for (AccountDto dto : usrlogin.get(index).usr) {
						print(dto);
					}
				}
				
				else if (selectSearch == 4) {
					moneyDown(); // 금액 데이터를 뽑아내 오름차순으로 정렬하는 함수
					// 반복문으로 전체 출력
					for (AccountDto dto : usrlogin.get(index).usr) {
						print(dto);
					}
				}
				
				else if(selectSearch == 5) {
					UptoDown();
					for (AccountDto dto : usrlogin.get(index).usr) {
						if(dto.getIoKind().equals("입금")) {
							print(dto);
						}
					}
				}
				
				else if(selectSearch == 6) {
					UptoDown();
					for (AccountDto dto : usrlogin.get(index).usr) {
						if(dto.getIoKind().equals("출금")) {
							print(dto);
						}
					}
				}
				
				// 메뉴로 돌아가기 -> 탈출
				else if (selectSearch == 7) {
					System.out.println("------------------------------------------");
					System.out.println("[메뉴로 돌아갑니다]");
					break;
				}

				else { // 선택지 외의 숫자 입력 시
					System.out.println("------------------------------------------");
					System.out.println("[메뉴의 숫자 중에 골라주십시오]");
				}
			} catch (InputMismatchException e) {
				System.out.println("------------------------------------------");
				System.out.println("[숫자로 입력해주십시오]");
				sc.nextLine();
			}
		}
	}
	
	@Override
	public void insert() {
		while (true) {
			try { // 예외 처리
				System.out.println("==============[ 가계부 추가 ]================");
				System.out.print("[금액] : ");
				int inputMoney = sc.nextInt();
				
				String inputTime;
				while(true) { // 날짜를 20201122 의 형태로 입력하지 않았을 시 돌아오는 루프
				System.out.print("[날짜] : ");
				String tmpTime = sc.next();
				if(checkNum(tmpTime, 8)) { // 문자열과 글자수 제한을 넣으면 숫자로만 이루어져 있는지 검사하는 함수
					inputTime = addPoint(tmpTime); // 20201122 -> 2020-11-22 로 바꿔주는 함수
					break;
				}
				}
				
				String inputIoKind;
				while (true) { // 잘못 입력 시 재입력을 위한 루프
				System.out.print("[입/출금] : ");
				inputIoKind = sc.next();

				// 입급과 출금이 아니면 돌아가도록 하는 조건문
				if (inputIoKind.equals("입금") || inputIoKind.equals("출금")) {
					break;
				}

				else {
					System.out.println("[\"입금\" 혹은 \"출금\" 으로 적어주십시오]");
				}
				}
				
				System.out.print("[내용] : ");
				String inputContent = sc.next();
				System.out.println("==========================================");
				
				// 입력받은 내용을 토대로 리스트 생성 -> 날짜를 입력하지 않고 시스템 시간을 사용할 경우 addFirst 를 사용해 자연스럽게 최신순 정렬이 가능하다
				usrlogin.get(index).addList(inputIoKind, inputMoney, inputContent, inputTime);
				System.out.println("[가계부에 [" + inputContent + "] 항목이 추가되었습니다]");
				
				break;	

			} catch (InputMismatchException e) {
				System.out.println("------------------------------------------");
				System.out.println("[숫자로 입력해주십시오]");
				sc.nextLine();
			}
		}
	}
	
	@Override
	public void delete() {
		while (true) {
			System.out.println("==============[ 가계부 삭제 ]================");
			System.out.println("| 1. [삭제하기]");
			System.out.println("| 2. [가계부 요약]");
			System.out.println("| 3. [메뉴로 돌아가기]");
			System.out.println("==========================================");
			System.out.print("[입력] >> ");

			int selectDelete = sc.nextInt();
			System.out.println("------------------------------------------");

			if (selectDelete == 1) {
				System.out.println("------------------------------------------");
				System.out.println("[가계부에서 삭제할 항목의 내용과 날짜를 입력해주십시오]");
				System.out.print("[내용] : ");
				String inputTitle = sc.next();
				String inputTime;

				while (true) { // 날짜를 20201122 의 형태로 입력하지 않았을 시 돌아오는 루프
					System.out.print("[날짜] : ");
					String tmpTime = sc.next();
					if (checkNum(tmpTime, 8)) {
						inputTime = addPoint(tmpTime);
						break;
					}
				}

				System.out.println("------------------------------------------");

				// 입력받은 내용과 날짜가 일치하는 지 검사
				boolean find = false;
				for (int i = 0; i < usrlogin.get(index).usr.size(); i++) {
					if (usrlogin.get(index).usr.get(i).getContent().equals(inputTitle)
							&& usrlogin.get(index).usr.get(i).getAdate().equals(inputTime)) {
						String delTitle = usrlogin.get(index).usr.remove(i).getContent();
						System.out.println("[" + delTitle + " 항목이 삭제되었습니다]");
						find = true;
						break;
					}
				}
				if (find == false) {
					System.out.println("------------------------------------------");
					System.out.println("[검색 결과를 찾을 수 없습니다]");
				}
			} else if(selectDelete == 2) {
				System.out.println("==============[ 가계부 요약 ]================");
				dashboard();
				System.out.println("------------------------------------------");
			} else if(selectDelete == 3) {
				System.out.println("------------------------------------------");
				System.out.println("[메뉴로 돌아갑니다]");
				break;
			} else {
				System.out.println("------------------------------------------");
				System.out.println("[메뉴의 숫자 중에 골라주십시오]");
			}
		}
	}
	
	@Override
	public void update() {
		while (true) {
			System.out.println("==============[ 가계부 수정 ]================");
			System.out.println("| 1. [수정하기]");
			System.out.println("| 2. [가계부 요약]");
			System.out.println("| 3. [메뉴로 돌아가기]");
			System.out.println("==========================================");
			System.out.print("[입력] >> ");

			int selectUpdate = sc.nextInt();
			System.out.println("------------------------------------------");

			if (selectUpdate == 1) {
				System.out.println("------------------------------------------");
				System.out.println("[가계부에서 수정할 항목의 금액과 날짜를 입력해주십시오]");
				System.out.print("[내용] : ");
				String inputTitle = sc.next();
				String inputTime;

				while (true) { // 날짜를 20201122 의 형태로 입력하지 않았을 시 돌아오는 루프
					System.out.print("[날짜] : ");
					String tmpTime = sc.next();
					if (checkNum(tmpTime, 8)) {
						inputTime = addPoint(tmpTime);
						break;
					}
				}

				System.out.println("------------------------------------------");
				// 검색 결과가 있는지 알려주는 깃발
				boolean find = false;

				for (int i = 0; i < usrlogin.get(index).usr.size(); i++) {
					if (usrlogin.get(index).usr.get(i).getContent().equals(inputTitle)
							&& usrlogin.get(index).usr.get(i).getAdate().equals(inputTime)) {
						find = true; // 깃발 on
						while (true) { // 예외 처리될 경우 및 반복작업을 위한 루프
							try { // 예외 처리
								System.out.println("===============[ 검색 결과 ]=================");
								print(i); // 검색 결과 보여주기 -> 수정 작업에 도움
								System.out.println("==============[ 가계부 수정 ]================");
								System.out.println("| 1. [내용 수정]");
								System.out.println("| 2. [날짜 수정]");
								System.out.println("| 3. [금액 수정]");
								System.out.println("| 4. [이전으로 돌아가기]");
								System.out.println("==========================================");
								System.out.print("[입력] >> ");

								int selectSearch = sc.nextInt();
								System.out.println("------------------------------------------");

								// 내용 수정
								if (selectSearch == 1) {
									System.out.print("[수정할 내용] > ");
									String updateContent = sc.next();
									usrlogin.get(index).usr.get(i).setContent(updateContent);
									System.out.println("------------------------------------------");
									System.out.println("[수정이 완료되었습니다]");
								}

								// 날짜 수정
								else if (selectSearch == 2) {
									String updateTime;
									while (true) { // 날짜를 20201122 의 형태로 입력하지 않았을 시 돌아오는 루프
										System.out.print("[수정할 날짜] > ");
										String tmpTime = sc.next();
										if (checkNum(tmpTime, 8)) {
											updateTime = addPoint(tmpTime);
											break;
										}
									}
									usrlogin.get(index).usr.get(i).setAdate(updateTime);
									System.out.println("------------------------------------------");
									System.out.println("[수정이 완료되었습니다]");
								}
								
								else if (selectSearch == 3) {
									while (true) {
										try {
											System.out.print("[수정할 금액] > ");
											int updateMoney = sc.nextInt();
											usrlogin.get(index).usr.get(i).setMoney(updateMoney);
											System.out.println("------------------------------------------");
											System.out.println("[수정이 완료되었습니다]");
											break;
										} catch (InputMismatchException e) {
											System.out.println("------------------------------");
											System.out.println("[숫자로 입력해주십시오]");
											sc.nextLine();
										}
									}
								}

								// 메뉴로 돌아가기 -> 탈출
								else if (selectSearch == 4) {
									System.out.println("------------------------------------------");
									System.out.println("[이전으로 돌아갑니다]");
									break;
								}

								// 선택지에 없는 숫자 입력 시
								else {
									System.out.println("------------------------------------------");
									System.out.println("[메뉴의 숫자 중에 골라주십시오]");
								}
							} catch (InputMismatchException e) {
								System.out.println("------------------------------------------");
								System.out.println("[숫자로 입력해주십시오]");
								sc.nextLine();
							}
						}
					}
				}

				// 검색 결과를 찾지 못했을 경우
				if (find == false) {
					System.out.println("------------------------------------------");
					System.out.println("[검색 결과를 찾을 수 없습니다]");
				}
			} else if(selectUpdate == 2) {
				System.out.println("==============[ 가계부 요약 ]================");
				dashboard();
				System.out.println("------------------------------------------");
			} else if(selectUpdate == 3) {
				System.out.println("------------------------------------------");
				System.out.println("[메뉴로 돌아갑니다]");
				break;
			} else {
				System.out.println("------------------------------------------");
				System.out.println("[메뉴의 숫자 중에 골라주십시오]");
			}
		}
	}
	
	@Override
	public void fileSave() {
		file.save();
	}
	
	@Override
	public void fileLoad() {
		file.load();
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Function
	/////////////////////////////////////////////////////////////////////////////////////////////////

	// 문자열의 길이와 각 숫자, 알파벳 소문자, 대문자 의 개수를 세어 최소개수를 충족하는 지 확인하는 함수
	public boolean makePw(String str, int min) {
		boolean flag = true;
		boolean check = true;
		int num = 0;
		int sAlp = 0;
		int lAlp = 0;

		for (int i = 0; i < str.length(); i++) {
			// 문자열 길이 검사
			if (str.length() >= min) {
				// 숫자 검사
				if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
					num++;
				} else if (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') {
					lAlp++;
				} else if (str.charAt(i) >= 'a' && str.charAt(i) <= 'z') {
					sAlp++;
				} else {
					check = false;
				}
			} else {
				System.out.println("------------------------------");
				System.out.println("[최소 " + min + "개 이상의 비밀번호로 입력해주십시오]");
				flag = false;
				break;
			}
		}
		if (check == false) {
			System.out.println("------------------------------");
			System.out.println("[알파벳과 숫자로만 입력해주십시오]");
			flag = false;
		}
		if (lAlp >= 1 && sAlp >= 1 && num >= 1 && str.length() >= min) {

		} else {
			System.out.println("------------------------------");
			System.out.println("[대문자, 소문자, 숫자가 각각 1개씩 포함되어야 합니다]");
			System.out.println("------------------------------");
			flag = false;
		}

		return flag;
	}
	
	
	public int searchContent(String content) {
		// 검색에 실패했을 경우를 위한 -1
		int dex = -1;

		// 검사 후 index 값 담기
		for (int i = 0; i < usrlogin.get(index).usr.size(); i++) {
			if (usrlogin.get(index).usr.get(i).getContent().contains(content)) {
				dex = i;
			}
		}
		return dex;
	}
	
	public int searchDate(String date) {
		// 검색에 실패했을 경우를 위한 -1
		int dex = -1;

		// 검사 후 index 값 담기
		for (int i = 0; i < usrlogin.get(index).usr.size(); i++) {
			if (usrlogin.get(index).usr.get(i).getAdate().contains(date)) {
				dex = i;
			}
		}
		return dex;
	}
	
	public void print(int index) {
		
		System.out.println("-----------------[ "+ usrlogin.get(index).usr.get(index).getIoKind() +" ]-----------------");
		System.out.println("[내용] > [" + usrlogin.get(index).usr.get(index).getContent() + "]");
		System.out.println("[금액] > " + usrlogin.get(index).usr.get(index).getMoney() + "원");
		System.out.println("[날짜] > " + usrlogin.get(index).usr.get(index).getAdate());
		System.out.println("-----------------------------------------");
	}
	
	public void print(AccountDto dto) {
		System.out.println("-----------------[ "+ dto.getIoKind() +" ]-----------------");
		System.out.println("[내용] > [" + dto.getContent() + "]");
		System.out.println("[금액] > " + dto.getMoney() + "원");
		System.out.println("[날짜] > " + dto.getAdate());
		System.out.println("-----------------------------------------");
	}
	
	// 요약 보기
	public void dashboard() {
		UptoDown(); // 최신순으로 정렬
		int num = 1;
		for (AccountDto dto : usrlogin.get(index).usr) {
			System.out.println(num + ". [" + dto.getIoKind() + "]_" + dto.getContent() + "_" + dto.getMoney() + "원_" + dto.getAdate());
			num++;
		}
	}
	
	public boolean checkNum(String str, int limit) {
		boolean flag = true;
		
		for (int i = 0; i < str.length(); i++) {
			// 문자열 길이 검사
			if (str.length() == limit) {
				// 숫자 검사
				if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				}
				else {
					System.out.println("[숫자로만 입력해주십시오]");
					flag = false;
					break;
				}
			}
			else {
				System.out.println("[" + limit +"개의 숫자로 입력해주십시오]");
				flag = false;
				break;
			}
		}	
		return flag;
	}
	// 20201122 형태의 데이터를 2020 11 22 로 나누어서 return 하는 함수
	public String[] splitDate(String str) {
		String[] split = {str.substring(0, 4), str.substring(4, 6), str.substring(6)};
		return split;
	}
	
	// 20201122 형태의 데이터를 2020-11-22 형태로 return 하는 함수
	public String addPoint(String str) {
		String[] split = splitDate(str);
		String sum = split[0] + "-" + split[1] + "-" + split[2];
		
		return sum;
		
	}
	
	// 2020-11-22 형태의 데이터를 20201122 형태로 return 하는 함수
	public int delPoint(String str) {	
		String[] split = str.split("-");	
		int sum = Integer.parseInt(split[0] + split[1] + split[2]);
		
		return sum;
	}
	
	// 날짜 내림차 순으로 List 를 정렬하는 함수
	public void UptoDown() {
		for (int i = 0; i < usrlogin.get(index).usr.size() - 1; i++) {
			for (int j = i + 1; j < usrlogin.get(index).usr.size(); j++) {
				int iDate = delPoint(usrlogin.get(index).usr.get(i).getAdate());
				int jDate = delPoint(usrlogin.get(index).usr.get(j).getAdate());
				
				if(iDate < jDate) {
					AccountDto dto = usrlogin.get(index).usr.get(i);
					usrlogin.get(index).usr.set(i, usrlogin.get(index).usr.get(j));
					usrlogin.get(index).usr.set(j, dto);
				}	
			}
		}
	}
	
	// 날짜 오름차 순으로 List 를 정렬하는 함수
	public void DowntoUp() {
		for (int i = 0; i < usrlogin.get(index).usr.size() - 1; i++) {
			for (int j = i + 1; j < usrlogin.get(index).usr.size(); j++) {
				int iDate = delPoint(usrlogin.get(index).usr.get(i).getAdate());
				int jDate = delPoint(usrlogin.get(index).usr.get(j).getAdate());
				
				if(iDate > jDate) {
					AccountDto dto = usrlogin.get(index).usr.get(i);
					usrlogin.get(index).usr.set(i, usrlogin.get(index).usr.get(j));
					usrlogin.get(index).usr.set(j, dto);
				}
			}
		}
	}
	// 금액 내림차 순으로 List 를 정렬하는 함수
	public void moneyUp() {
		for (int i = 0; i < usrlogin.get(index).usr.size() - 1; i++) {
			for (int j = i + 1; j < usrlogin.get(index).usr.size(); j++) {
				if(usrlogin.get(index).usr.get(i).getMoney() < usrlogin.get(index).usr.get(j).getMoney()) {
					AccountDto dto = usrlogin.get(index).usr.get(i);
					usrlogin.get(index).usr.set(i, usrlogin.get(index).usr.get(j));
					usrlogin.get(index).usr.set(j, dto);
				}
			}
		}
	}
	
	// 금액 오름차 순으로 List 를 정렬하는 함수
	public void moneyDown() {
		for (int i = 0; i < usrlogin.get(index).usr.size() - 1; i++) {
			for (int j = i + 1; j < usrlogin.get(index).usr.size(); j++) {
				if(usrlogin.get(index).usr.get(i).getMoney() > usrlogin.get(index).usr.get(j).getMoney()) {
					AccountDto dto = usrlogin.get(index).usr.get(i);
					usrlogin.get(index).usr.set(i, usrlogin.get(index).usr.get(j));
					usrlogin.get(index).usr.set(j, dto);
				}
			}
		}
	}
	
	// 글자를 랜덤으로 뽑아 원하는 길이의 숫자, 소문자, 대문자로 이루어진 랜덤한 문자열을 만드는 함수
	public String randomString(int n) {
		String ranS = "";
		if (n > 0) {
			char[] ch = new char[n];
			for (int i = 0; i < n; i++) {
				int num = (int) (Math.random() * 10) + 48;
				int alp = (int) (Math.random() * 26) + 65;
				int Alp = (int) (Math.random() * 26) + 97;
				int ran = (int)(Math.random() * 3);
				if(ran == 0) {
					ch[i] = (char)num;
				} else if(ran == 1) {
					ch[i] = (char)alp;
				} else if(ran == 2) {
					ch[i] = (char)Alp;
				}
			}
			for (int i = 0; i < ch.length; i++) {
				ranS = ranS + ch[i];
			}
		} else {
			System.out.println("[1 이상의 값을 넣어주십시오]");
		}
		return ranS;
	}

}
