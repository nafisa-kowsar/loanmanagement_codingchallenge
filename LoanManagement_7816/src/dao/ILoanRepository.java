package dao;

import java.util.List;

import entity.Loan;
import exception.InvalidLoanException;

public interface ILoanRepository {
	double calculateInterest(int loanId) throws InvalidLoanException;
    double calculateInterest(int loanId, double principalAmount, double interestRate, int loanTerm) throws InvalidLoanException;
    void loanStatus(int loanId) throws InvalidLoanException;
    double calculateEMI(int loanId) throws InvalidLoanException;
    double calculateEMI(int loanId, double principalAmount, double interestRate, int loanTerm) throws InvalidLoanException;
    void loanRepayment(int loanId, double amount) throws InvalidLoanException;
    List<Loan> getAllLoans();
    Loan getLoanById(int loanId) throws InvalidLoanException;
    void applyLoan(Loan loan) throws InvalidLoanException;
    int getGeneratedLoanId() throws InvalidLoanException;
}
