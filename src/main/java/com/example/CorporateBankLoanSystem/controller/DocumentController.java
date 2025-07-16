package com.example.CorporateBankLoanSystem.controller;

import com.example.CorporateBankLoanSystem.model.Document;
import com.example.CorporateBankLoanSystem.model.LoanApplication;
import com.example.CorporateBankLoanSystem.model.User;
import com.example.CorporateBankLoanSystem.service.DocumentService;
import com.example.CorporateBankLoanSystem.service.LoanApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private LoanApplicationService loanApplicationService;

    
    private static String UPLOAD_DIR = "src/main/resources/static/uploads/";

  
    @GetMapping("/user/documents/upload")
    public String showUploadForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }
        
        
        Document document = new Document();
        document.setLoanApplication(new LoanApplication()); // Set a dummy LoanApplication to avoid NullPointerException for th:field="*{loanApplication.applicationId}"
        model.addAttribute("document", document);

        // Attributes for the modal (passed via RedirectAttributes from a previous POST)
        if (model.asMap().containsKey("uploadSuccess")) {
            model.addAttribute("uploadSuccess", model.asMap().get("uploadSuccess"));
            model.addAttribute("uploadedApplicationId", model.asMap().get("uploadedApplicationId"));
            model.addAttribute("uploadedDocumentType", model.asMap().get("uploadedDocumentType"));
            model.addAttribute("uploadedFileName", model.asMap().get("uploadedFileName"));
        }
        // Also pass error/success messages that might come from RedirectAttributes
        if (model.asMap().containsKey("errorMessage")) {
            model.addAttribute("errorMessage", model.asMap().get("errorMessage"));
        }
        if (model.asMap().containsKey("successMessage")) {
            model.addAttribute("successMessage", model.asMap().get("successMessage"));
        }

        return "document-upload"; // This must match the name of your HTML template file
    }

   
    @PostMapping("/document/submit")
    public String uploadDocument(@ModelAttribute Document document, // Binds form fields to Document object
                                 @RequestParam("file") MultipartFile file, // Binds the uploaded file
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user is not authenticated
        }

        // Extract applicationId from the bound Document object
        Long applicationId = null;
        if (document.getLoanApplication() != null && document.getLoanApplication().getApplicationId() != null) {
            applicationId = document.getLoanApplication().getApplicationId();
        }

        if (applicationId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Application ID is required. Please enter a valid Application ID.");
            return "redirect:/user/documents/upload"; // Redirect back to the form with error
        }

        // Verify that the loan application exists and belongs to the current user
        Optional<LoanApplication> loanOpt = loanApplicationService.getLoanApplicationById(applicationId);
        if (!loanOpt.isPresent() || !loanOpt.get().getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Loan application not found or you do not have permission to upload documents for this ID.");
            return "redirect:/user/documents/upload"; // Redirect back to the form with error
        }

        LoanApplication loanApplication = loanOpt.get();

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
            return "redirect:/user/documents/upload"; // Redirect back to the form with error
        }

        try {
            // Ensure the upload directory exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file to the server's filesystem
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), filePath);

            // Set the loanApplication and other details on the Document entity
            document.setLoanApplication(loanApplication); // Link the document to the fetched loan application
            document.setFileName(fileName);
            document.setFilePath("/uploads/" + fileName); // Store a web-accessible path
            document.setUploadDate(LocalDate.now());
            document.setValid(false); // Default to not validated

            documentService.saveDocument(document);

            // Add attributes for the modal on success, which will be picked up by the GET mapping
            redirectAttributes.addFlashAttribute("uploadSuccess", true);
            redirectAttributes.addFlashAttribute("uploadedApplicationId", applicationId);
            redirectAttributes.addFlashAttribute("uploadedDocumentType", document.getDocumentType());
            redirectAttributes.addFlashAttribute("uploadedFileName", document.getFileName());
            redirectAttributes.addFlashAttribute("successMessage", "Document uploaded successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload document: " + e.getMessage());
        }

        return "redirect:/user/documents/upload";
    }

 
    //  A mapping to download a document (simulated)
    @GetMapping("/download/{documentId}")
    public String downloadDocument(@PathVariable Long documentId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Document> docOpt = documentService.getDocumentById(documentId);
        if (docOpt.isPresent()) {
            Document document = docOpt.get();
            // Basic security check: ensure user owns the loan application linked to this document
            if (!document.getLoanApplication().getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to download this document.");
                return "redirect:/user/dashboard";
            }
            
            
            redirectAttributes.addFlashAttribute("successMessage", "Simulated download of " + document.getFileName() + ".");
            return "redirect:/loan/status?applicationId=" + document.getLoanApplication().getApplicationId();
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Document not found.");
            return "redirect:/user/dashboard";
        }
    }
}
