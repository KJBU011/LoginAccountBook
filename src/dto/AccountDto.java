package dto;

public class AccountDto extends LoginDto{
	
	private String ioKind;
	private int money;
	private String content;
	private String adate;
	
	public AccountDto() {}

	public AccountDto(String ioKind, int money, String content, String adate) {
		this.ioKind = ioKind;
		this.money = money;
		this.content = content;
		this.adate = adate;
	}

	public String getIoKind() {
		return ioKind;
	}

	public void setIoKind(String ioKind) {
		this.ioKind = ioKind;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAdate() {
		return adate;
	}

	public void setAdate(String adate) {
		this.adate = adate;
	}

}
