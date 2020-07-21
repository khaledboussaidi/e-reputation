package com.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{

	private static final RestTemplate restTemplate=new RestTemplate();

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/login/**","/register","/all", "/projects").permitAll();
		http.authorizeRequests().antMatchers("/projects", "/addProject", "/deleteProject/**", "/project/**", "/projectSearch/**","project/stop/**","/project/launch/**").hasAuthority("USER");
		http.addFilterBefore(new JWTAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	public static Claims decodeJWT(String jwt) {
		//This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(SecurityConstants.SECRET))
				.parseClaimsJws(jwt).getBody();
		return claims;
	}
	public static String getToken() {
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("username","khaled1");
		jsonObject.put("password","boussaidi");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(),headers);
		return restTemplate.exchange("http://localhost:8084/login", HttpMethod.POST, entity, String.class).getHeaders().get("Authorization").get(0);


	}

}