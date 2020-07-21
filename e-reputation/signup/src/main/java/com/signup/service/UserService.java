package com.signup.service;

import com.signup.modele.MyUser;
import com.signup.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRespository userRepository;

    public MyUser findUserByUserName(String username){
        return userRepository.findByUsername(username);
    }
    public void createUser(MyUser newUser) {
        userRepository.save(newUser);
    }
}
