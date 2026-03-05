package com.example.loanemi.controller;

import java.util.List;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loanemi.model.EMI;

import com.example.loanemi.service.EMIService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/emis")
public class EMIController {
	 private final EMIService emiService;

	    public EMIController(EMIService emiService) {
	        this.emiService = emiService;
	    }
	    @PutMapping("/{id}/pay")
	    public EMI payEMI(@PathVariable Long id) {
	        return emiService.payEMI(id);
	    }

	    @GetMapping
	    public List<EMI> getAllEMIs() {
	        return emiService.getAllEMIs();
	    }

	    @PutMapping("/pay-multiple")
	    public List<EMI> payMultipleEMIs(@RequestBody List<Long> emiIds) {
	        return emiService.payMultipleEMIs(emiIds);
	    }
	    
}
