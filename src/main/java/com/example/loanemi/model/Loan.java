package com.example.loanemi.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Loan {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String loanType;
	private double principalAmount;
	private int tenureMonths;
	private Double interestRate;
	
	@Column(name="status")
	private String status = "ACTIVE"; // default
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@JsonIgnore
	@OneToMany(mappedBy ="loan", cascade = CascadeType.ALL)
	private List<EMI> emis;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public double getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(double principalAmount) {
		this.principalAmount = principalAmount;
	}

	public int getTenureMonths() {
		return tenureMonths;
	}

	public void setTenureMonths(int tenureMonths) {
		this.tenureMonths = tenureMonths;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<EMI> getEmis() {
		return emis;
	}

	public void setEmis(List<EMI> emis) {
		this.emis = emis;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	
}
