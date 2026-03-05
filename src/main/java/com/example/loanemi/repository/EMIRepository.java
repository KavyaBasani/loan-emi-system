package com.example.loanemi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.loanemi.model.EMI;
import com.example.loanemi.model.Loan;

@Repository
public interface EMIRepository extends JpaRepository<EMI,Long> {
	List<EMI> findByLoan(Loan loan);

}
