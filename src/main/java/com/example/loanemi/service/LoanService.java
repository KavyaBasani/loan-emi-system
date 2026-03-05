package com.example.loanemi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loanemi.model.Customer;
import com.example.loanemi.model.EMI;
import com.example.loanemi.model.Loan;
import com.example.loanemi.repository.CustomerRepository;
import com.example.loanemi.repository.EMIRepository;
import com.example.loanemi.repository.LoanRepository;

@Service
public class LoanService {
	
	private final LoanRepository loanRepository;
	private final CustomerRepository customerRepository;
	private final EMIRepository emiRepository;

	
	 public LoanService(LoanRepository loanRepository,CustomerRepository customerRepository,EMIRepository emiRepository) {
	        this.loanRepository = loanRepository;
	        this.customerRepository = customerRepository;
	        this.emiRepository = emiRepository;
	    }
	 public Loan saveLoan(Loan loan) {
		 Customer customer = customerRepository.findById(
		            loan.getCustomer().getId()
		    ).orElseThrow(() -> new RuntimeException("Customer not found"));

		    loan.setCustomer(customer);

		    Loan savedLoan = loanRepository.save(loan);

		    generateEMIs(savedLoan);   

		    return savedLoan;
	 }
	 
	 private void generateEMIs(Loan loan) {

		    double P = loan.getPrincipalAmount();
		    double r = loan.getInterestRate() / 12 / 100;
		    int n = loan.getTenureMonths();

		    double emiAmount = P * r * Math.pow(1 + r, n)
		            / (Math.pow(1 + r, n) - 1);

		    LocalDate dueDate = LocalDate.now().plusMonths(1);

		    for (int i = 1; i <= n; i++) {

		        EMI emi = new EMI();
		        emi.setAmount(emiAmount);
		        emi.setDueDate(dueDate);
		        emi.setPaid(false);
		        emi.setLoan(loan);

		        emiRepository.save(emi);

		        dueDate = dueDate.plusMonths(1);
		    }
		}
	 
	 public double calculateEMIByLoanId(Long loanId) {
		    Loan loan = loanRepository.findById(loanId)
		            .orElseThrow(() -> new RuntimeException("Loan not found"));

		    double P = loan.getPrincipalAmount();
		    double r = loan.getInterestRate() / 12 / 100;
		    int n = loan.getTenureMonths();

		    return P * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);
		}
	 
	 public List<Loan> getAllLoans(){
		 return loanRepository.findAll();
	 }
	 public Loan getLoanById(Long id) {
	        return loanRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
	    }
}
