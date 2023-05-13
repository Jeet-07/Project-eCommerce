package com.manjeet.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.manjeet.common.entity.Customer;
import com.manjeet.customer.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
	
	private final CustomerRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = repo.findByEmail(email);
		if (customer == null) 
			throw new UsernameNotFoundException("No customer found with the email " + email);
		
		return new CustomerUserDetails(customer);
	}

}
