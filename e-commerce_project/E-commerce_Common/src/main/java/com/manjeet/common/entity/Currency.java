package com.manjeet.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "currencies")
public class Currency extends IdBasedEntity {
	
	@Column(nullable = false, length = 64)
	private String name;
	
	@Column(nullable = false, length = 3)
	private String symbol;
	
	@Column(nullable = false, length = 4)
	private String code;

	public Currency() {
		
	}
	
	public Currency(String name, String symbol, String code) {
		super();
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}


	@Override
	public String toString() {
		return name + " - " + code + " - " + symbol;
	}

	
}
