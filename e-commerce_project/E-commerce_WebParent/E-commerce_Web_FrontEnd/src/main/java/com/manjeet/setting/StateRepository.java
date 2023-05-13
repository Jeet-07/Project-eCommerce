package com.manjeet.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.manjeet.common.entity.Country;
import com.manjeet.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
