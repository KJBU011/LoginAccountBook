package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import dao.LoginAccountDao;
import dto.AccountDto;
import dto.LoginDto;

public class FileProc {
	// 파일 생성
	File AccountData = new File("C:/AccountManager/Accounts.txt");
	
	public FileProc() {
		try {
			// 파일이 존재하지 않을 시 생성
			if(AccountData.exists() == false) {
				AccountData.createNewFile();
				System.out.println("------------------------------");
				System.out.println("[파일 생성 성공 : 첫 실행 시에만 출력됩니다]");
				System.out.println("------------------------------");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AccountData.setReadOnly();
		
	}
	
	
	// 저장
	public void save() {
		if (AccountData.getParentFile() != null && !AccountData.getParentFile().exists()) {
			// 파일의 상위 폴더를 생성(.mkdirs())
			AccountData.getParentFile().mkdirs();
		}
		try { // 예외 처리
			LoginAccountDao dao = LoginAccountDao.getInstance(); // singleton 호출
			// write 준비
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(AccountData)));
			
			// 반복문을 통해 write
			for (LoginDto ldto : dao.usrlogin) {
				String uid = ldto.getUsrId();
				String upw = ldto.getUsrPw();
				String ioKind;
				int money;
				String content;
				String date;
				// id, pw 를 맨 앞에 적기 -> 추후 불러올 때 가장 앞의 두 값만 쉽게 뽑기 위함
				pw.print(uid + "@" + upw);

				// 반복문 내의 반복문을 통해 dto 내부의 개인정보가 담긴 dto를 적기
				for (AccountDto udto : ldto.usr) {

					ioKind = udto.getIoKind();
					money = udto.getMoney();
					content = udto.getContent();
					date = udto.getAdate();

					pw.print("@" + ioKind + "@" + money + "@" + content + "@" + date);
				}
				
				// write 가 끝난 이후 다음 유저의 정보를 write 하기 위해 개행
				pw.println();

			}
			// 파일 닫기
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	// 불러오기
	public void load() {
		if (AccountData.getParentFile() != null && !AccountData.getParentFile().exists()) {
			// 파일의 상위 폴더를 생성(.mkdirs())
			AccountData.getParentFile().mkdirs();
		}
		
		
		try { // 예외처리
			LoginAccountDao dao = LoginAccountDao.getInstance(); // dao 호출
			// read 준비
			BufferedReader bw = new BufferedReader(new FileReader(AccountData));
			
			// read 시 조건문에 진입하기 위한 ""
			String str = "";
			int i = 0;
			
			while((str = bw.readLine()) != null) {
				
				String[] split = str.split("@");
				
				// 맨 앞의 두 데이터를 빼내 id 와 pw 로 저장
				dao.usrlogin.add(new LoginDto(split[0], split[1]));

				// index 번호가 2 부터 시작해 + 4 를 할 때마다 반복되는 점을 이용
				int seq = 2;
				
				// 전체 배열 길이에서 맨 앞의 id, pw 부분인 2를 빼고 4개의 데이터를 하나로 묶어 반복
				for (int j = 0; j < (split.length - 2) / 4; j++) {

					// List 의 값은 뒤로 하나씩 추가된다는 점을 이용해 리스트 하나 추가될 때마다 i 값을 하나씩 늘려 데이터 넣기
					dao.usrlogin.get(i).usr.add(new AccountDto(split[seq], Integer.parseInt(split[seq + 1]),
							split[seq + 2], split[seq + 3]));

					// seq 값에 4를 더해 데이터를 한 묶음씩 산출
					seq += 4;
				}

				// 로그인 정보가 담긴 리스트를 다음 index 로 넘겨주기
				i++;
			}

			// 파일 닫아주기
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
