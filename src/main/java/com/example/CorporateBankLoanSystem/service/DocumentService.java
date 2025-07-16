package com.example.CorporateBankLoanSystem.service;

import com.example.CorporateBankLoanSystem.model.Document;
import com.example.CorporateBankLoanSystem.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    //Saves a document to the database
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

   //Retrieves a document by its ID.
    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    //Retrieves all documents associated with a specific loan application.
    public List<Document> getDocumentsByLoanApplicationId(Long loanApplicationId) {
        return documentRepository.findByLoanApplication_ApplicationId(loanApplicationId);
    }

    //Deletes a document by its ID.
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

}
