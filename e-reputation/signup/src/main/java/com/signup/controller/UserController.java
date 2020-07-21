package com.signup.controller;


import org.json.simple.JSONObject;
import com.signup.modele.MyUser;
import com.signup.repository.UserRespository;
import com.signup.security.AppSecurityConfig;
import com.signup.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

import static com.signup.security.SecurityConstants.SECRET;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserRespository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @PostMapping("/register")
    public  int addNewUser (@RequestBody MyUser user) {

        MyUser u = userService.findUserByUserName(user.getUsername());
        if(u==null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return 200;
        }
        return 400;
    }

    @PostMapping("/userinfo")
    public  int UserInfo (@RequestBody MyUser user,@RequestHeader(value="Authorization") String jwt) {
        jwt=jwt.replaceFirst("Bearer","");
        MyUser u = userService.findUserByUserName(AppSecurityConfig.decodeJWT(jwt).getSubject());
        if(u!=null) {
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            u.setDate(user.getDate());
            userRepository.save(u);



            return 200;
        }
        return 400;
    }
    @PostMapping("/changepassword")
    public  int UserInfo (@RequestBody JSONObject jsonObject, @RequestHeader(value="Authorization") String jwt) {
        jwt=jwt.replaceFirst("Bearer","");
        MyUser user = userService.findUserByUserName(AppSecurityConfig.decodeJWT(jwt).getSubject());
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        if(user!=null) {
            if(bCryptPasswordEncoder.matches((CharSequence) jsonObject.get("password"),user.getPassword())){
                System.out.println(user+"ok");
                user.setPassword(passwordEncoder.encode((String) jsonObject.get("newpassword")));
                userRepository.save(user);
                return 200;
            }
            return 400;

        }
        return 400;
    }

    @GetMapping("/all")
    public List<MyUser> getAll(){
        return userRepository.findAll();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @GetMapping("/jwt")
    public void JWT(){
    }
    @GetMapping("/user")
    public MyUser getUser(@RequestHeader(value="Authorization") String jwt){
        jwt=jwt.replaceFirst("Bearer","");
        MyUser user = userService.findUserByUserName(AppSecurityConfig.decodeJWT(jwt).getSubject());
        user.setPassword(null);
        return user;
    }



}
