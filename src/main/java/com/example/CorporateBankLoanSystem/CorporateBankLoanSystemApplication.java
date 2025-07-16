package com.example.CorporateBankLoanSystem;

import com.example.CorporateBankLoanSystem.model.LoanApplication;
import com.example.CorporateBankLoanSystem.model.User;
import com.example.CorporateBankLoanSystem.repository.LoanApplicationRepository;
import com.example.CorporateBankLoanSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class CorporateBankLoanSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorporateBankLoanSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(UserRepository userRepository, LoanApplicationRepository loanApplicationRepository) {
        return args -> {
            User user1 = new User("Hari", "12345", "HARI PRASATH", "hari@gmail.com");
            User user2 = new User("Nandhu", "12345", "NANDHAKISHORE", "nandhu@gmail.com");
            User user3 = new User("Shahir", "12345", "SHAHIR ALI", "shahir@gmail.com");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            System.out.println("Dummy Users created: Hari, Nandhu, Shahir"); 

            LoanApplication loan1 = new LoanApplication();
            loan1.setCompanyName("Tech Innovators Inc.");
            loan1.setLoanType("Startup Capital");
            loan1.setLoanAmount(new BigDecimal("150000.00"));
            loan1.setStatus("PENDING");
            loan1.setSubmissionDate(LocalDate.now().minusDays(5));
            loan1.setUser(user1);
            loanApplicationRepository.save(loan1);

            LoanApplication loan2 = new LoanApplication();
            loan2.setCompanyName("Global Logistics Solutions");
            loan2.setLoanType("Fleet Expansion");
            loan2.setLoanAmount(new BigDecimal("750000.00"));
            loan2.setStatus("APPROVED");
            loan2.setSubmissionDate(LocalDate.now().minusDays(10));
            loan2.setApprovalRejectionDate(LocalDate.now().minusDays(7));
            loan2.setAdminCreditScore(780.0); // FIXED: Changed to double literal
            loan2.setAdminEvaluationNotes("Strong financials, excellent repayment history.");
            loan2.setInterestRate(0.05); // ADDED: Interest rate for repayment analysis
            loan2.setLoanTermMonths(60); // ADDED: Loan term in months for repayment analysis
            loan2.setUser(user2);
            loanApplicationRepository.save(loan2);

            LoanApplication loan3 = new LoanApplication();
            loan3.setCompanyName("Local Bakery Co.");
            loan3.setLoanType("Equipment Upgrade");
            loan3.setLoanAmount(new BigDecimal("50000.00"));
            loan3.setStatus("REJECTED");
            loan3.setSubmissionDate(LocalDate.now().minusDays(3));
            loan3.setApprovalRejectionDate(LocalDate.now().minusDays(2));
            loan3.setAdminCreditScore(450.0); // FIXED: Changed to double literal
            loan3.setAdminEvaluationNotes("High debt-to-income ratio, insufficient collateral.");
            loan3.setUser(user1); // User1 has two loans
            loanApplicationRepository.save(loan3);

            LoanApplication loan4 = new LoanApplication();
            loan4.setCompanyName("Digital Marketing Agency");
            loan4.setLoanType("Working Capital");
            loan4.setLoanAmount(new BigDecimal("200000.00"));
            loan4.setStatus("PENDING");
            loan4.setSubmissionDate(LocalDate.now().minusDays(1));
            loan4.setUser(user3);
            loanApplicationRepository.save(loan4);

            System.out.println("Dummy Loan Applications created.");
        };
    }
}
