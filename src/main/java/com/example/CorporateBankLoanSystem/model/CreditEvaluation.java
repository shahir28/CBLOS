package com.example.CorporateBankLoanSystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name="CreditEvaluation")
public class CreditEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="credit_eval_seq")
    @SequenceGenerator(name="credit_eval_seq",sequenceName="seq_credit_evaluation",allocationSize=1)
    private Long evaluationId;

    @OneToOne
    @JoinColumn(name = "application_id")
    private LoanApplication application;

    private BigDecimal riskScore;
    private Integer creditScore;
    private LocalDate evaluationDate;
    
    
	//-------Constructors-------------------------------------------------------------------
	public CreditEvaluation() {}
	
	
	public CreditEvaluation(Long evaluationId, LoanApplication application, BigDecimal riskScore, Integer creditScore,
			LocalDate evaluationDate) {
		super();
		this.evaluationId = evaluationId;
		this.application = application;
		this.riskScore = riskScore;
		this.creditScore = creditScore;
		this.evaluationDate = evaluationDate;
	}
   
    
    //---------Getters and Setters-----------------------------------------------------
	public Long getEvaluationId() {
		return evaluationId;
	}
	public void setEvaluationId(Long evaluationId) {
		this.evaluationId = evaluationId;
	}
	public LoanApplication getApplication() {
		return application;
	}
	public void setApplication(LoanApplication application) {
		this.application = application;
	}
	public BigDecimal getRiskScore() {
		return riskScore;
	}
	public void setRiskScore(BigDecimal riskScore) {
		this.riskScore = riskScore;
	}
	public Integer getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}
	public LocalDate getEvaluationDate() {
		return evaluationDate;
	}
	public void setEvaluationDate(LocalDate evaluationDate) {
		this.evaluationDate = evaluationDate;
	}
	

	
	
}
