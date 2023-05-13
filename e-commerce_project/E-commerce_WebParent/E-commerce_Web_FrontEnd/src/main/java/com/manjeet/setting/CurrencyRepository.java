package com.manjeet.setting;

import org.springframework.data.repository.CrudRepository;

import com.manjeet.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

}
