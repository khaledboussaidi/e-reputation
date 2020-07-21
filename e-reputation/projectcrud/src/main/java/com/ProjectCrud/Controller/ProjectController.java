package com.ProjectCrud.Controller;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;


import com.ProjectCrud.security.AppSecurityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ProjectCrud.services.ProjectService;
import com.ProjectCrud.services.ProjectServices;
import com.ProjectCrud.models.Project;

import reactor.core.publisher.Flux;

import javax.xml.bind.DatatypeConverter;

import static com.ProjectCrud.security.SecurityConstants.SECRET;


@RestController
@CrossOrigin(origins = "*")
public class ProjectController {
	
	@Autowired
	private WebClient.Builder WebClientBuilder;

	@Autowired

	private ProjectService projectService;
	@Autowired
	private KafkaTemplate<String, JSONObject> kafkaTemplate;
	

	
	@GetMapping("/projects/{username}")
	@ResponseBody
	public List<Project> getAllProjects(@PathVariable("username") String username)
	{
		System.out.println("listerddddd");
		return  projectService.findByUsername(username);
	}
	
	@PostMapping(path="/addProject")
	public String addProject(@RequestBody Project p, @RequestHeader(value="Authorization") String jwt)
	{
		jwt=jwt.replaceFirst("Bearer","");
		String username= AppSecurityConfig.decodeJWT(jwt).getSubject();
		Date dateobj = new Date();
		p.setDate(dateobj);
		p.setUsername(username);
		projectService.save(p);
		System.out.println(p);
		return "project added";
	}
	
	@DeleteMapping("/deleteProject/{id}")
	public void deleteProject(@PathVariable("id") Long id)
	{
		projectService.deleteById(id);
		System.out.println("element effacé");
	}
	

	@GetMapping("/project/{id}")
	@ResponseBody
	public  Optional<Project> getProjecty(@PathVariable("id") Long id)
	{
		Optional<Project> p = projectService.findById(id);
	
	if(p==null)
	{
		return null; 
	}
	else
	{

		/*RestTemplate restTemplate = new RestTemplate();
		Project project=restTemplate.getForObject("http://127.0.0.1:5000/stop/"+id, Project.class);	*/
		System.out.println(p);
		return p;
	
	}
		
		//return (ArrayList<Project>) projectService.findAllById(id);
	}
	
	@GetMapping("/projectSearch/{name}")
	@ResponseBody
	public  List<Project> getProjectyName(@PathVariable("name") String name)
	{
		return projectService.findByName(name);
	}
	
	@GetMapping(path="project/stop/{id}")
	public int stopProject(@PathVariable("id") Long id)
	{
		Optional<Project> p = projectService.findById(id);
		
		if(p==null)
		{
			return 400; 
		}
		else
		{
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("project_id",p.get().getId());
			System.out.println("requet stopped");
			/*RestTemplate restTemplate = new RestTemplate();
			Project project=restTemplate.getForObject("http://127.0.0.1:5000/stop/"+id, Project.class);	*/
			kafkaTemplate.send("stop",jsonObject);
			System.out.println("requet stopped");
			return 200;
		
		}
		
	}
	
	
	@GetMapping(path="project/launch/{id}")
	public /*Flux<String>*/ int launchProject( @PathVariable("id") Long id,@RequestHeader(value="Authorization") String jwt)
	{
		Optional<Project> p = projectService.findById(id);
		
		if(p==null)
		{
			return 400; 
		}
		else
		{
			Flux<String> project = WebClientBuilder.defaultHeader("Authorization",jwt).build()
					.get()
					.uri("http://10.108.233.21:80/project/launch/"+id+"/"+p.get().getUsername()+"/"+p.get().getMotCle()+"/"+p.get().getLanguange())
					.retrieve()
					.bodyToFlux(String.class);
			project.subscribe();
			System.out.println("lancé");
			return 200;
		}
		
				
}
	
}