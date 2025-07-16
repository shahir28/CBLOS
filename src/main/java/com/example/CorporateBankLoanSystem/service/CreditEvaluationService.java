package com.example.CorporateBankLoanSystem.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CorporateBankLoanSystem.model.CreditEvaluation;
import com.example.CorporateBankLoanSystem.repository.CreditEvaluationRepository;

@Service
public class CreditEvaluationService {
    @Autowired
    private CreditEvaluationRepository repository;

    public CreditEvaluation evaluate(CreditEvaluation evaluation) {
        evaluation.setEvaluationDate(LocalDate.now());
        return repository.save(evaluation);
    }
}
