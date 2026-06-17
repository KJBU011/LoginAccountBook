package dto;

import java.util.LinkedList;
import java.util.List;

public class LoginDto {
	
	private String usrId;
	private String usrPw;
	// dto 내부에 list 를 만들어 list 내부의 list 구성
	public List<AccountDto> usr = new LinkedList<AccountDto>();
	
	public LoginDto() {}

	public LoginDto(String usrId, String usrPw) {
		this.usrId = usrId;
		this.usrPw = usrPw;
	}


	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getUsrPw() {
		return usrPw;
	}

	public void setUsrPw(String usrPw) {
		this.usrPw = usrPw;
	}
	
	public void addList(String ioKind, int money, String content, String adate) {
		usr.add(new AccountDto(ioKind, money, content, adate));
	}
}
