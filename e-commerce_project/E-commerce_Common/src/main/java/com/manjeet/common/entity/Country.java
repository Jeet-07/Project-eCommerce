package com.manjeet.common.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class Country extends IdBasedEntity {
	
	@Column(nullable = false, length = 45)
	private String name;
	
	@Column(nullable = false, length = 5)
	private String code;

	@JsonIgnore
	@OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
	private Set<State> states;

	public Country() {	}
	public Country(Integer id) {
		this.id = id;
	}
	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}
	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

	
}
