package com.example.loanemi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.loanemi.model.Customer;
import com.example.loanemi.repository.CustomerRepository;

@Service
public class CustomerService {
	
	private final CustomerRepository customerRepository;
	
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository= customerRepository;
	}
	
	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	public List<Customer> getAllCustomers(){
		return customerRepository.findAll();
	}
	public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }
}
