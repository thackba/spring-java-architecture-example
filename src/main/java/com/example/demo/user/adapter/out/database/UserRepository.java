package com.example.demo.user.adapter.out.database;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findByName(String name);
}
