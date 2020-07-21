package com.signup.modele;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class MyUser {

	@Id
	private String username;
	@NotNull
	private String name;
	@NotNull
	private String password;
	@NotNull
	private String email;
	@NotNull
	private String role;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@NotNull
	private Date date;
	public String getUserName() {
		return username;
	}
	@JsonSetter
	public void setUserName(String userName) {
		this.username = userName;
	}
	public String getName() {
		return name;
	}
	@JsonSetter
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}



	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}

	@JsonSetter
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role;
	}

	@JsonSetter
	public void setRole(String role) {
		this.role = role;
	}

	public MyUser(String username, String name, String password, String email, String role, Date date) {
		this.username = username;
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.date = date;
	}
	public MyUser(){}

	@Override
	public String toString() {
		return "MyUser{" +
				"username='" + username + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", role='" + role + '\'' +
				'}';
	}
}