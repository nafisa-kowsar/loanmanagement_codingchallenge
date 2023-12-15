package entity;

public class HomeLoan extends Loan {
	private String propertyAddress;
    private int propertyValue;
	public HomeLoan() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HomeLoan(int loanId, Customer customer, double principalAmount, double interestRate, int loanTerm,
			String loanType, String loanStatus) {
		super(loanId, customer, principalAmount, interestRate, loanTerm, loanType, loanStatus);
		// TODO Auto-generated constructor stub
	}
	public HomeLoan(String propertyAddress, int propertyValue) {
		super();
		this.propertyAddress = propertyAddress;
		this.propertyValue = propertyValue;
	}
	public String getPropertyAddress() {
		return propertyAddress;
	}
	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}
	public int getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(int propertyValue) {
		this.propertyValue = propertyValue;
	}
    

}
