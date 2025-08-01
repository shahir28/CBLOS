package com.example.CorporateBankLoanSystem.repository;

import com.example.CorporateBankLoanSystem.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByLoanApplication_ApplicationId(Long loanApplicationId);
}
