package com.hse.iphreactive.repository;

import com.hse.iphreactive.entity.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, Long> {
}
