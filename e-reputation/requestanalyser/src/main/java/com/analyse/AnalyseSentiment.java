package com.analyse;
import com.security.AppSecurityConfig;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;


public class AnalyseSentiment {
    private static final RestTemplate restTemplate=new RestTemplate();


    public JSONObject getAnalysedTweets(JSONObject jsonObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", AppSecurityConfig.getToken());
        HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(jsonObject,headers);
        return  restTemplate.exchange("http://localhost:5001/analysesentiment", HttpMethod.POST, entity, JSONObject.class).getBody();




    }

}