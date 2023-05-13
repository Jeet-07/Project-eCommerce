package com.manjeet.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "states")
public class State extends IdBasedEntity {
	
	@Column(nullable = false, length = 45)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	public State() {}
	
	public State(String name, Country country) {
		this.name = name;
		this.country = country;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + "]";
	}
	
	
}
