package com.signup.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signup.modele.MyUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        MyUser user =null;
        try{
            user=new ObjectMapper().readValue(request.getInputStream(), MyUser.class);


        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
        System.out.println("****************************");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println("****************************");

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User sptingUser = (User) authResult.getPrincipal();
        System.out.println(sptingUser.getUsername());
        System.out.println(sptingUser.getPassword());
        System.out.println(sptingUser.getAuthorities());
        String jwtToken= Jwts.builder()
                .setSubject(sptingUser.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, String.valueOf(SecurityConstants.SECRET))
                .claim("role",sptingUser.getAuthorities())
                .compact();
        response.addHeader(SecurityConstants.HEADER_STRING,
                SecurityConstants.TOKEN_PREFIX+jwtToken);
    }
}
