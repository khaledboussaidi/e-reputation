package com.ProjectCrud.services;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.ProjectCrud.models.*;


public interface ProjectService extends JpaRepository<Project, Long> {
	
	public List<Project> findByName(String name);
	public ArrayList<Project> findByUsername(String name);
	


}
