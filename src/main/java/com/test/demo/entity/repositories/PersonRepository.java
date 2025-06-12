package com.test.demo.entity.repositories;

import com.test.demo.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
