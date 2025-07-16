package com.example.CorporateBankLoanSystem.repository;

import com.example.CorporateBankLoanSystem.model.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository; // Ensure this import is present
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> { // Must extend JpaRepository

    List<RepaymentSchedule> findByLoanApplication_ApplicationIdOrderByInstallmentNumberAsc(Long loanApplicationId);
}
