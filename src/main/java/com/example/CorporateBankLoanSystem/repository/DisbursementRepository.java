package com.example.CorporateBankLoanSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateBankLoanSystem.model.Disbursement;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
}