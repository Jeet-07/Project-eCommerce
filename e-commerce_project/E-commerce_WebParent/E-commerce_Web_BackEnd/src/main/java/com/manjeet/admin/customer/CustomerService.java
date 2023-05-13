package com.manjeet.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.manjeet.admin.order.OrderRepository;
import com.manjeet.common.entity.Address;
import com.manjeet.common.entity.order.Order;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.manjeet.admin.paging.PagingAndSortingHelper;
import com.manjeet.admin.setting.country.CountryRepository;
import com.manjeet.common.entity.Country;
import com.manjeet.common.entity.Customer;
import com.manjeet.common.exception.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;
	
	@Autowired private CustomerRepository customerRepo;
	@Autowired private CountryRepository countryRepo;	
	@Autowired private PasswordEncoder passwordEncoder;

	@Autowired private AddressRepository addressRepo;

	@Autowired private OrderRepository orderRepo;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepo);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}		
	
	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);

		if (existCustomer != null && existCustomer.getId() != id) {
			// found another customer having the same email
			return false;
		}
		
		return true;
	}
	
	public void save(Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);			
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}		
		
		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
		
		customerRepo.save(customerInForm);
	}
	
	public void delete(Integer id) throws CustomerNotFoundException {
		Optional<Customer> optCustomer = customerRepo.findById(id);
		if ( ! optCustomer.isPresent() ) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}

		List<Address> addressList = addressRepo.findByCustomer(optCustomer.get());
		List<Order> orderList = orderRepo.findByCustomer(optCustomer.get());
		addressRepo.deleteAll(addressList);
		orderRepo.deleteAll(orderList);
		customerRepo.deleteById(id);
	}

	public Long countAll(){
		return customerRepo.count();
	}
	public Long countEnabled(){
		return customerRepo.countEnabled();
	}
}
