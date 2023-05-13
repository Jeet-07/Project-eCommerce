package com.manjeet.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerRestController {

	private final CustomerService service;
	
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(String email) {
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
