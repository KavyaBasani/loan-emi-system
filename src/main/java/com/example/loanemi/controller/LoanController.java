package com.example.loanemi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loanemi.model.EMI;
import com.example.loanemi.model.Loan;
import com.example.loanemi.repository.EMIRepository;
import com.example.loanemi.service.LoanService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/loans")
public class LoanController {
	@Autowired
	private final LoanService loanService;
	@Autowired
	private final EMIRepository emiRepository;
	
	
	public LoanController(LoanService loanService, EMIRepository emiRepository) {
		this.loanService = loanService;
		this.emiRepository = emiRepository;
	}
	
	@PostMapping
	public Loan createLoan(@RequestBody Loan loan) {
		return loanService.saveLoan(loan);
	}
	
	@GetMapping
	public List<Loan> getAllLoans(){
		return loanService.getAllLoans();
	}
	
	// Get EMI amount for a loan
	@GetMapping("/{id}/emi")
	 public double getEMIAmount(@PathVariable Long id) {
        return loanService.calculateEMIByLoanId(id);
    }
	
	// Get all EMIs for a loan
    @GetMapping("/{id}/emis")
    public List<EMI> getAllEMIs(@PathVariable Long id) {
        // Use getLoanById for cleaner code
        Loan loan = loanService.getLoanById(id);
        return emiRepository.findByLoan(loan);
    }

}
