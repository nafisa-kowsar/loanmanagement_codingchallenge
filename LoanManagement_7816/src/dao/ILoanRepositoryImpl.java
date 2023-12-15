package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.DBUtil;

import entity.Customer;
import entity.Loan;
import exception.InvalidLoanException;

public class ILoanRepositoryImpl implements ILoanRepository{
	
	private static Connection connection = DBUtil.getConnection();
	
	 @Override
	    public void applyLoan(Loan loan) throws InvalidLoanException {
		 boolean confirmation = getConfirmationFromUser();

	        if (confirmation) {
	            loan.setLoanStatus("Pending");

	            try {
	                 String query = "INSERT INTO loans (customer_id, principal_amount, interest_rate, loan_term_months, loan_type, loan_status) " +
	                               "VALUES (?, ?, ?, ?, ?, ?)";
	                try (PreparedStatement ps = connection.prepareStatement(query)) {
	                    //ps.setInt(1, loan.getLoanId());
	                    ps.setInt(1, loan.getCustomer().getCustomerId());
	                    ps.setDouble(2, loan.getPrincipalAmount());
	                    ps.setDouble(3, loan.getInterestRate());
	                    ps.setInt(4, loan.getLoanTerm());
	                    ps.setString(5, loan.getLoanType());
	                    ps.setString(6, loan.getLoanStatus());
	                    ps.executeUpdate();
	                }

	                System.out.println("Loan application submitted successfully!");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                throw new InvalidLoanException("Error applying for the loan. Please try again.");
	            }
	        } else {
	            System.out.println("Loan application canceled by the user.");
	        }
	    }
	 private boolean getConfirmationFromUser() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Do you want to apply for this loan? (Yes/No)");
	        String userInput = scanner.nextLine().toLowerCase();
	        return userInput.equals("yes");
	    }
	
	    

	    @Override
	    public double calculateInterest(int loanId) throws InvalidLoanException {
	    	 Loan loan = getLoanById(loanId);
	    	 double interest = (loan.getPrincipalAmount() * loan.getInterestRate() * loan.getLoanTerm()) / 12;
	    	 return interest;
	    }

	    @Override
	    public double calculateInterest(int loanId, double principalAmount, double interestRate, int loanTerm) throws InvalidLoanException {
	    	double interest = (principalAmount * interestRate * loanTerm) / 12;
            return interest;
	    }

	    @Override
	    public void loanStatus(int loanId) throws InvalidLoanException {
	       Loan loan = getLoanById(loanId);
	       if (loan.getCustomer().getCreditScore() > 650) {
	            updateLoanStatus(loanId, "Approved");
	            System.out.println("Loan with ID " + loanId + " is approved.");
	        } else {
	            updateLoanStatus(loanId, "Rejected");
	            System.out.println("Loan with ID " + loanId + " is rejected due to low credit score.");
	        }
	    }

	    @Override
	    public double calculateEMI(int loanId) throws InvalidLoanException {
	        Loan loan = getLoanById(loanId);
	        double monthlyInterestRate = loan.getInterestRate() / 12 / 100;
            double emi = (loan.getPrincipalAmount() * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loan.getLoanTerm()))
	                / (Math.pow(1 + monthlyInterestRate, loan.getLoanTerm()) - 1);
            return emi;
	    }

	    @Override
	    public double calculateEMI(int loanId, double principalAmount, double interestRate, int loanTerm) throws InvalidLoanException {
	    	double monthlyInterestRate = interestRate / 12 / 100;
	        double emi = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTerm))
	                / (Math.pow(1 + monthlyInterestRate, loanTerm) - 1);

	        return emi;
	    }

	    @Override
	    public void loanRepayment(int loanId, double amount) throws InvalidLoanException {
	    	Loan loan = getLoanById(loanId);
	    	double emi = calculateEMI(loanId, loan.getPrincipalAmount(),loan.getInterestRate(),loan.getLoanTerm());
	    	int noOfEmiToPay = (int) (amount / emi);
            if (noOfEmiToPay > 0) {
	            updateRemainingEmi(loanId, loan.getLoanTerm() - noOfEmiToPay);

	            System.out.println("Paid " + noOfEmiToPay + " EMIs. Remaining EMIs: " + (loan.getLoanTerm() - noOfEmiToPay));
	        } else {
	            System.out.println("Payment rejected. Amount insufficient for one EMI.");
	        }
	    }

	    @Override
	    public List<Loan> getAllLoans() {
	    	List<Loan> loans = new ArrayList<>();

	        try {
	            String query = "SELECT loans.*, customer.* FROM loans JOIN customer ON loans.customer_id = customer.customer_id";
	            try (PreparedStatement ps = connection.prepareStatement(query);
	                 ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    loans.add(getLoanByIdFromResultSet(rs));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        for (Loan loan : loans) {
	            System.out.println(loan);
	        }

	        return loans;
	    }

	    @Override
	    public Loan getLoanById(int loanId) throws InvalidLoanException {
	    	try{
	            String query = "SELECT loans.*, customer.* FROM loans JOIN customer ON loans.customer_id = customer.customer_id WHERE loan_id = ?";
	            try (PreparedStatement ps = connection.prepareStatement(query)) {
	                ps.setInt(1, loanId);
	                try (ResultSet rs = ps.executeQuery()) {
	                    if (rs.next()) {
	                        return getLoanByIdFromResultSet(rs);
	                    } else {
	                        throw new InvalidLoanException("Loan not found with ID: " + loanId);
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new InvalidLoanException("Error retrieving loan. Please try again.");
	        }
	    }
	    
	    @Override
	    public int getGeneratedLoanId() throws InvalidLoanException {
	        try{
	            String query = "SELECT LAST_INSERT_ID() as generated_id";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    if (resultSet.next()) {
	                        return resultSet.getInt("generated_id");
	                    } else {
	                        throw new InvalidLoanException("Error retrieving generated loan ID.");
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new InvalidLoanException("Error retrieving generated loan ID. Please try again.");
	        }
	    }
	    
	    
	    private Loan getLoanByIdFromResultSet(ResultSet rs) throws SQLException {
	        int loanId = rs.getInt("loan_id");
	        Customer customer = new Customer(
	                rs.getInt("customer_id"),
	                rs.getString("name"),
	                rs.getString("email_address"),
	                rs.getString("phone_number"),
	                rs.getString("address"),
	                rs.getInt("credit_score")
	        );
            return new Loan(
	                loanId,
	                customer,
	                rs.getDouble("principal_amount"),
	                rs.getDouble("interest_rate"),
	                rs.getInt("loan_term_months"),
	                rs.getString("loan_type"),
	                rs.getString("loan_status")
	        );
	    }
	    
	    private void updateLoanStatus(int loanId, String status) throws InvalidLoanException {
	        try{
	            String query = "UPDATE loans SET loan_status = ? WHERE loan_id = ?";
	            try (PreparedStatement ps = connection.prepareStatement(query)) {
	                ps.setString(1, status);
	                ps.setInt(2, loanId);

	                int rowsUpdated = ps.executeUpdate();
	                if (rowsUpdated == 0) {
	                    throw new InvalidLoanException("Error updating loan status. Loan not found with ID: " + loanId);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new InvalidLoanException("Error updating loan status. Please try again.");
	        }
	    }
	    
	    private void updateRemainingEmi(int loanId, int remainingEmi) throws InvalidLoanException {
	        try{
	            String query = "UPDATE loans SET remaining_emi = ? WHERE loan_id = ?";
	            try (PreparedStatement ps = connection.prepareStatement(query)) {
	                ps.setInt(1, remainingEmi);
	                ps.setInt(2, loanId);

	                int rowsUpdated = ps.executeUpdate();
	                if (rowsUpdated == 0) {
	                    throw new InvalidLoanException("Error updating remaining EMIs. Loan not found with ID: " + loanId);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new InvalidLoanException("Error updating remaining EMIs. Please try again.");
	        }
	    }

}
