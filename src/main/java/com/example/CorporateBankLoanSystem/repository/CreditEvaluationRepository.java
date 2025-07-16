package com.example.CorporateBankLoanSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateBankLoanSystem.model.CreditEvaluation;

public interface CreditEvaluationRepository extends JpaRepository<CreditEvaluation, Long> {
}
