package com.example.CorporateBankLoanSystem.model;

import jakarta.persistence.*;
import java.time.LocalDate; // Ensure LocalDate is imported

@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "application_id", nullable = false) 
    private LoanApplication loanApplication; 

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String fileName;

    private String filePath; // Path where the document is stored (simulated)

    @Column(nullable = false)
    private LocalDate uploadDate;

    private boolean isValid; // To mark if document is verified/valid

    // Constructors
    public Document() {
        this.uploadDate = LocalDate.now();
        this.isValid = false;
    }

    public Document(LoanApplication loanApplication, String documentType, String fileName, String filePath) {
        this.loanApplication = loanApplication;
        this.documentType = documentType;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadDate = LocalDate.now();
        this.isValid = false;
    }

    // Getters and Setters
    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "Document{" +
               "documentId=" + documentId +
               ", loanApplicationId=" + (loanApplication != null ? loanApplication.getApplicationId() : "null") +
               ", documentType='" + documentType + '\'' +
               ", fileName='" + fileName + '\'' +
               ", filePath='" + filePath + '\'' +
               ", uploadDate=" + uploadDate +
               ", isValid=" + isValid +
               '}';
    }
}
