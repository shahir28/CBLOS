package com.example.CorporateBankLoanSystem.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CorporateBankLoanSystem.model.Disbursement;
import com.example.CorporateBankLoanSystem.repository.DisbursementRepository;

@Service
public class DisbursementService {
    @Autowired
    private DisbursementRepository repository;

    public Disbursement track(Disbursement disbursement) {
        disbursement.setDisbursementDate(LocalDate.now());
        return repository.save(disbursement);
    }
}
