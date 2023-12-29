import java.util.Scanner;

import dao.ILoanRepository;
import dao.ILoanRepositoryImpl;
import entity.Customer;
import entity.Loan;
import exception.InvalidLoanException;


public class LoanManagement {

    public static void main(String[] args) {
        ILoanRepository loanRepository = new ILoanRepositoryImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Loan Management System Menu:");
            System.out.println("1. Apply for a Loan");
            System.out.println("2. View All Loans");
            System.out.println("3. View Loan Details");
            System.out.println("4. Loan Repayment");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int ch = scanner.nextInt();
            scanner.nextLine(); 

            switch (ch) {
                case 1:
                    applyLoan(loanRepository, scanner);
                    break;
                case 2:
                    viewAllLoans(loanRepository);
                    break;
                case 3:
                    viewLoanDetails(loanRepository, scanner);
                    break;
                case 4:
                    loanRepayment(loanRepository, scanner);
                    break;
                case 5:
                    System.out.println("Exiting Loan Management System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    private static void applyLoan(ILoanRepository lr, Scanner scanner) {
        System.out.println("Applying for a Loan:");

        // Collect customer and loan details
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Principal Amount: ");
        double principalAmount = scanner.nextDouble();
        scanner.nextLine(); 

        System.out.print("Enter Interest Rate: ");
        double interestRate = scanner.nextDouble();
        scanner.nextLine(); 

        System.out.print("Enter Loan Term (in months): ");
        int loanTerm = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter Loan Type (CarLoan/HomeLoan): ");
        String loanType = scanner.nextLine();

        // Create a Customer object with default credit score for now
        Customer customer = new Customer(customerId, "", "", "", "", 0);

        // Create a Loan object with default loan status for now
        Loan loan = new Loan(0, customer, principalAmount, interestRate, loanTerm, loanType, "");

        try {
            // Apply for the loan
            lr.applyLoan(loan);

            // Retrieve the loan ID after applying for the loan
            int loanId = loan.getLoanId();

            // Check and update loan status based on credit score using loanStatus method
            lr.loanStatus(loanId);
        } catch (InvalidLoanException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void viewAllLoans(ILoanRepository loanRepository) {
        System.out.println("Viewing All Loans:");

        try {
            // Retrieve and display all loans
            loanRepository.getAllLoans();
        } catch (InvalidLoanException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void viewLoanDetails(ILoanRepository loanRepository, Scanner scanner) {
        System.out.println("Viewing Loan Details:");
        System.out.print("Enter the loan ID: ");
        int loanId = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        try {
            Loan loan = loanRepository.getLoanById(loanId);
            System.out.println("Loan Details:\n" + loan);
        } catch (InvalidLoanException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void loanRepayment(ILoanRepository loanRepository, Scanner scanner) {
        System.out.println("Loan Repayment:");

        // Collect user input for loan details and repayment amount
        System.out.print("Enter Loan ID: ");
        int loanId = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        System.out.print("Enter Repayment Amount: ");
        double repaymentAmount = scanner.nextDouble();
        scanner.nextLine(); // consume the newline character

        try {
            // Call loanRepayment method
            loanRepository.loanRepayment(loanId, repaymentAmount);
        } catch (InvalidLoanException e) {
            System.out.println(e.getMessage());
        }
    }


}

