package com.ProjectCrud.Controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
public class KafkaConsumer {
	
	@Autowired
    private KafkaTemplate<String, JSONObject> kafkaTemplate;
	/*
	@PostMapping(path="project/stop/{id}")
	 @KafkaListener(topics = "stop", groupId  = "streamer-group", containerFactory = "streamerKafkaListenerFactory")
	    public void streamerconsumeJson( @PathVariable("id") String id ) {
		 JSONObject jsonObject = new JSONObject();
		 jsonObject.put("id", id);
		 
	        kafkaTemplate.send("sentimentanalyse",jsonObject);
	        System.out.println(jsonObject.get("id"));
	    }*/

}
