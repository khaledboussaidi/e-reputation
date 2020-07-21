package com.ProjectCrud.services;

import java.util.*;
import org.springframework.stereotype.Service;

import com.ProjectCrud.models.Project;


@Service
public class ProjectServices{
	
	private ProjectService projectService;
	
	
	public Project createProject(Project p)
	{
		return projectService.save(p);
	}
	
	

}
