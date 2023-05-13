package com.manjeet.address;

import java.util.List;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manjeet.common.entity.Address;
import com.manjeet.common.entity.Customer;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {
	
	private final AddressRepository repo;
	
	public List<Address> listAddressBook(Customer customer) {
		return repo.findByCustomer(customer);
	}

	public void save(Address address) {
		repo.save(address);
	}
	
	public Address get(Integer addressId, Integer customerId) {
		return repo.findByIdAndCustomer(addressId, customerId);
	}
	
	public void delete(Integer addressId, Integer customerId) {
		repo.deleteByIdAndCustomer(addressId, customerId);
	}
	
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if (defaultAddressId > 0) {
			repo.setDefaultAddress(defaultAddressId);
		}
		
		repo.setNonDefaultForOthers(defaultAddressId, customerId);
	}
	
	public Address getDefaultAddress(Customer customer) {
		return repo.findDefaultByCustomer(customer.getId());
	}
}
