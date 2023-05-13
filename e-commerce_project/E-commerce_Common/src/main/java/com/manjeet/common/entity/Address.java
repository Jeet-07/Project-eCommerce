package com.manjeet.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address extends AbstractAddressWithCountry {
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(name = "default_address")
	private boolean defaultForShipping;

}
