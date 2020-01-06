package com.example.webfluxsseexample.repository;


import com.example.webfluxsseexample.model.PersonStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonStatusRepository extends ReactiveMongoRepository<PersonStatus, String> {

}
