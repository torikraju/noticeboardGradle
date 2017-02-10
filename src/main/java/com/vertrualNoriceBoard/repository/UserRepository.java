package com.vertrualNoriceBoard.repository;

import org.springframework.data.repository.CrudRepository;

import com.vertrualNoriceBoard.user.User;



public interface UserRepository extends CrudRepository<User, String> {

	public User findByUserName(String username);

	public User findByEmail(String email);

	public User findByUserId(String userId);
	

}
