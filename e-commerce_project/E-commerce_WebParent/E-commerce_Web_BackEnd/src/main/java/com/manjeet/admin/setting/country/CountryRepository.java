package com.manjeet.admin.setting.country;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.manjeet.common.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();
}
