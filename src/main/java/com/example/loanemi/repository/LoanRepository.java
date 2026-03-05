package com.example.loanemi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanemi.model.Loan;

public interface LoanRepository extends JpaRepository<Loan,Long>{

}
