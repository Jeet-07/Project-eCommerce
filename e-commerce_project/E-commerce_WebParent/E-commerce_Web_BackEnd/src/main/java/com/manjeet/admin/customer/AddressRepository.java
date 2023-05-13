package com.manjeet.admin.customer;

import com.manjeet.common.entity.Address;
import com.manjeet.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    public List<Address> findByCustomer(Customer customer);
}
