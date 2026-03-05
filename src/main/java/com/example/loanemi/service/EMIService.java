package com.example.loanemi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loanemi.model.EMI;
import com.example.loanemi.model.Loan;
import com.example.loanemi.repository.EMIRepository;
import com.example.loanemi.repository.LoanRepository;

@Service
public class EMIService {
	 private final EMIRepository emiRepository;
	 private final LoanRepository loanRepository;

	 public EMIService(EMIRepository emiRepository, LoanRepository loanRepository) {
		    this.emiRepository = emiRepository;
		    this.loanRepository = loanRepository;
		}

	 public EMI payEMI(Long emiId) {

		    EMI emi = emiRepository.findById(emiId)
		            .orElseThrow(() -> new RuntimeException("EMI not found"));

		    emi.setPaid(true);

		    EMI updatedEMI = emiRepository.save(emi);

		    Loan loan = updatedEMI.getLoan();

		    boolean allPaid = loan.getEmis().stream()
		            .allMatch(e -> e.isPaid());

		    if (allPaid) {
		        loan.setStatus("COMPLETED");
		        loanRepository.save(loan);
		    }

		    return updatedEMI;
		}
	 
	    public List<EMI> getAllEMIs() {
	        return emiRepository.findAll();
	    }

	    public List<EMI> payMultipleEMIs(List<Long> emiIds) {

	        List<EMI> updatedEMIs = emiIds.stream()
	                .map(id -> emiRepository.findById(id)
	                        .orElseThrow(() -> new RuntimeException("EMI not found with id: " + id)))
	                .peek(emi -> emi.setPaid(true))
	                .map(emiRepository::save)
	                .toList();

	        updatedEMIs.forEach(this::updateLoanStatus);

	        return updatedEMIs;
	    }
	    private void updateLoanStatus(EMI emi) {

	        boolean allPaid = emi.getLoan()
	                .getEmis()
	                .stream()
	                .allMatch(EMI::isPaid);

	        if (allPaid) {
	            emi.getLoan().setStatus("COMPLETED");
	            loanRepository.save(emi.getLoan());
	        }
	    }
	    

}
