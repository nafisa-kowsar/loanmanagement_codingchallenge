package entity;

public class CarLoan extends Loan{
	 private String carModel;
	 private int carValue;
	public CarLoan() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CarLoan(int loanId, Customer customer, double principalAmount, double interestRate, int loanTerm,
			String loanType, String loanStatus) {
		super(loanId, customer, principalAmount, interestRate, loanTerm, loanType, loanStatus);
		// TODO Auto-generated constructor stub
	}
	public CarLoan(String carModel, int carValue) {
		super();
		this.carModel = carModel;
		this.carValue = carValue;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public int getCarValue() {
		return carValue;
	}
	public void setCarValue(int carValue) {
		this.carValue = carValue;
	}
	 
	 
	 
	 

}
