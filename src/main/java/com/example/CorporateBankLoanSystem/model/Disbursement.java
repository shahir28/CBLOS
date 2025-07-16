package com.example.CorporateBankLoanSystem.model;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="Disbursement")
public class Disbursement {
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="disbursement_seq")
	@SequenceGenerator(name="disbursement_seq",sequenceName="seq_disbursement",allocationSize=1)
    private Long disbursementId;

    @OneToOne
    @JoinColumn(name = "application_id")
    private LoanApplication application;

    private BigDecimal disbursedAmount;
    private LocalDate disbursementDate;
    private String repaymentSchedule;
  
	//------------Constructors----------------------------------------
    public Disbursement() {}
    
    public Disbursement(Long disbursementId, LoanApplication application, BigDecimal disbursedAmount,
			LocalDate disbursementDate, String repaymentSchedule) {
		super();
		this.disbursementId = disbursementId;
		this.application = application;
		this.disbursedAmount = disbursedAmount;
		this.disbursementDate = disbursementDate;
		this.repaymentSchedule = repaymentSchedule;
	}
    //------------Getters and Setters---------------------------------------------
	public Long getDisbursementId() {
		return disbursementId;
	}
	public void setDisbursementId(Long disbursementId) {
		this.disbursementId = disbursementId;
	}
	public LoanApplication getApplication() {
		return application;
	}
	public void setApplication(LoanApplication application) {
		this.application = application;
	}
	public BigDecimal getDisbursedAmount() {
		return disbursedAmount;
	}
	public void setDisbursedAmount(BigDecimal disbursedAmount) {
		this.disbursedAmount = disbursedAmount;
	}
	public LocalDate getDisbursementDate() {
		return disbursementDate;
	}
	public void setDisbursementDate(LocalDate disbursementDate) {
		this.disbursementDate = disbursementDate;
	}
	public String getRepaymentSchedule() {
		return repaymentSchedule;
	}
	public void setRepaymentSchedule(String repaymentSchedule) {
		this.repaymentSchedule = repaymentSchedule;
	}
}

