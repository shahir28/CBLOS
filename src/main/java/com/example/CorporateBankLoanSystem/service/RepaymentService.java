package com.example.CorporateBankLoanSystem.service;

import com.example.CorporateBankLoanSystem.model.LoanApplication;
import com.example.CorporateBankLoanSystem.model.RepaymentSchedule;
import com.example.CorporateBankLoanSystem.repository.RepaymentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RepaymentService {

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

  
    public List<RepaymentSchedule> generateRepaymentSchedule(LoanApplication loanApplication,
                                                             double annualInterestRate,
                                                             int loanTermMonths) {
        List<RepaymentSchedule> schedule = new ArrayList<>();

        // Use BigDecimal for principal to maintain precision and also to calculate the yearly interest to monthly interest
        BigDecimal principal = loanApplication.getLoanAmount();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal monthlyPayment;
        //If interst is no
        if (annualInterestRate == 0) {
            monthlyPayment = principal.divide(BigDecimal.valueOf(loanTermMonths), 2, RoundingMode.HALF_UP);
        } else {
            BigDecimal onePlusI = BigDecimal.ONE.add(monthlyInterestRate);
            BigDecimal onePlusIPowN = onePlusI.pow(loanTermMonths);
            BigDecimal numerator = monthlyInterestRate.multiply(onePlusIPowN);
            BigDecimal denominator = onePlusIPowN.subtract(BigDecimal.ONE);

            if (denominator.compareTo(BigDecimal.ZERO) == 0) {
                monthlyPayment = principal.divide(BigDecimal.valueOf(loanTermMonths), 2, RoundingMode.HALF_UP);
            } else {
                monthlyPayment = principal.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
            }
        }
        
//        remainingBalance: Tracks how much of the loan is left to repay.
//        startDate: Uses the loan approval date or today's date if not available.
        BigDecimal remainingBalance = principal;
        LocalDate startDate = loanApplication.getApprovalRejectionDate() != null ? loanApplication.getApprovalRejectionDate() : LocalDate.now();


        for (int i = 1; i <= loanTermMonths; i++) {
        	
//        	interestForPeriod: Interest is calculated on the remaining balance.
//        	principalForPeriod: The rest of the monthly payment goes toward reducing the principal.
        	BigDecimal interestForPeriod = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalForPeriod = monthlyPayment.subtract(interestForPeriod);
            
//            In the last month, the remaining balance is paid off completely.
//            The monthly payment is recalculated to ensure it exactly covers the final principal and interest.
            if (i == loanTermMonths) {
                principalForPeriod = remainingBalance;
                monthlyPayment = principalForPeriod.add(interestForPeriod).setScale(2, RoundingMode.HALF_UP);
                
                
//                This is a safety check: if due to rounding the interest becomes negative, it's reset to zero.
                if (interestForPeriod.compareTo(BigDecimal.ZERO) < 0) {
                    interestForPeriod = BigDecimal.ZERO;
                }
            }
            
            
            //After calculating how much of the monthly payment goes toward the principal, this line reduces the remaining loan balance accordingly.
            remainingBalance = remainingBalance.subtract(principalForPeriod);
            
            
//            	This block ensures that rounding errors don’t leave a small negative balance:
//
//            	If it's the last month and the balance is slightly negative (e.g., -₹0.01), just set it to zero.
//            	If it's not the last month, adjust the principal for the current month by adding the negative balance (effectively reducing it), and then set the balance to zero.
            if (remainingBalance.compareTo(BigDecimal.ZERO) < 0 && i == loanTermMonths) {
                remainingBalance = BigDecimal.ZERO;
            } else if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
                principalForPeriod = principalForPeriod.add(remainingBalance);
                remainingBalance = BigDecimal.ZERO;
            }

            //Calculates the due date for the current installment by adding i months to the loan start date.
            LocalDate dueDate = startDate.plusMonths(i);

            // Creates a new RepaymentSchedule object for the current month.
            RepaymentSchedule entry = new RepaymentSchedule(
                loanApplication,
                i,
                dueDate,
                principalForPeriod.doubleValue(),
                interestForPeriod.doubleValue(),
                monthlyPayment.doubleValue(),
                remainingBalance.doubleValue(),
                "PENDING"
            );
            //Adds the current month’s repayment entry to the list.
            schedule.add(entry);
        }

        //After the loop finishes, all repayment entries are saved to the database using the repository.
        return repaymentScheduleRepository.saveAll(schedule);
    }

   
    //This method fetches the repayment schedule for a specific loan, ordered by installment number.
    public List<RepaymentSchedule> getRepaymentScheduleByLoanApplicationId(Long loanApplicationId) {
        return repaymentScheduleRepository.findByLoanApplication_ApplicationIdOrderByInstallmentNumberAsc(loanApplicationId);
    }
}
