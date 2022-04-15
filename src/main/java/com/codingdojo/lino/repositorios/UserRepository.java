package com.codingdojo.lino.repositorios;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codingdojo.lino.modelos.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
}
