package com.example.CorporateBankLoanSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateBankLoanSystem.model.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
}