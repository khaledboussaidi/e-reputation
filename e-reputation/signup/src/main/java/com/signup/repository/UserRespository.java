package com.signup.repository;

import com.signup.modele.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRespository extends JpaRepository<MyUser, String> {
	public MyUser findByUsername(String username);

}
