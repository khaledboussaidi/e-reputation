package com.ProjectCrud.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table(name="project")
public class Project {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date dateobj) {
		this.date = dateobj;
	}
	@NotNull
	private String name;
	@NotNull
	private Date date;
	@NotNull
	private String motCle;
	@NotNull
	private String language;
	
	@NotNull
	private String type;
	@NotNull
	private String username;
	

	public Project( String name, Date date, String motCle, String language, String type, String username) {
		super();
		this.name = name;
		this.date = date;
		this.motCle = motCle;
		this.language = language;
		this.type = type;
		this.username = username;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLanguange() {
		return language;
	}
	public void setLanguange(String languange) {
		this.language = languange;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMotCle() {
		return motCle;
	}
	public void setMotCle(String motCle) {
		this.motCle = motCle;
	}
	
	
	public Project()
	{
		super();
	}

	@Override
	public String toString() {
		return "Project{" +
				"id=" + id +
				", name='" + name + '\'' +
				", date=" + date +
				", motCle='" + motCle + '\'' +
				", language='" + language + '\'' +
				", type='" + type + '\'' +
				", username='" + username + '\'' +
				'}';
	}
}
