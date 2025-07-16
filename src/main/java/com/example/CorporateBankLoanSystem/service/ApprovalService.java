package com.example.CorporateBankLoanSystem.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CorporateBankLoanSystem.model.Approval;
import com.example.CorporateBankLoanSystem.repository.ApprovalRepository;

@Service
public class ApprovalService {
    @Autowired
    private ApprovalRepository repository;

    public Approval approve(Approval approval) {
        approval.setApprovalDate(LocalDate.now());
        return repository.save(approval);
    }
}