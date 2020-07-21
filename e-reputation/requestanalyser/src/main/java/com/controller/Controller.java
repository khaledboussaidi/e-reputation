package com.controller;

import com.model.Tweet;
import com.repository.TweetRepository;
import com.security.AppSecurityConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")

public class Controller {
    @Autowired
    TweetRepository tweetRepository;
    @GetMapping("/")
    public void afficher(@RequestHeader(value="Authorization") String jwt)
    {
        System.out.println("yesss");
    }

    @PostMapping("/search")
    public ArrayList<String> search(@RequestHeader(value="Authorization") String jwt)
    {
		/*JSONObject metric = new JSONObject();
		metric.add("int", "")*/
        //System.out.println(jwt);
        ArrayList<String> metrcis=new ArrayList<String>();
        //metrcis.add("int");
        metrcis.add("location");
        metrcis.add("sentiment.polarity");
        metrcis.add("sentiment.subjectivity");
        return metrcis;
    }
    @PostMapping("/query")
    public ArrayList<JSONObject> query(@RequestBody String o, @RequestHeader(value="Authorization") String jwt) throws org.json.simple.parser.ParseException
    {
        //get username form json

        jwt=jwt.replaceFirst("Bearer","");
        String username= AppSecurityConfig.decodeJWT(jwt).getSubject();
        System.out.println(username);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(o);
        System.out.println(o);


        //Get the project Id form json
        ArrayList<JSONObject> target=  (ArrayList<JSONObject>) json.get("targets");
        Long projectId = (Long) ((HashMap) target.get(0).get("data")).get("projectId");
        System.out.println(projectId);
ArrayList<JSONObject> finalVar = new ArrayList<JSONObject>();
//count metric
        if(o.indexOf("sentiment.polarity")!=-1){
            ArrayList<Tweet> all=tweetRepository.findByUsername(username);
            JSONObject contenu = new JSONObject();

            ArrayList<ArrayList<Object>> listOLists = new ArrayList<>();

            Map<Object, Object> sorted = new HashMap<>();

            for(Tweet tweet:all)
            {
                if(tweet.getProject_id()==projectId)
                {
                    sorted.put(new Date((String) tweet.getDate()).getTime(), tweet.getSentiment_polarity());
                }
            }
            TreeMap<Object, Object> sortedTree = new TreeMap<>(sorted);
            sortedTree.putAll(sorted);

            for(Map.Entry<Object, Object> entry : sortedTree.entrySet()) {
                Object key = entry.getKey();

                Object value = entry.getValue();
                ArrayList<Object> list = new ArrayList<>();
                JSONObject data= new JSONObject();
                list.add( value);
                list.add( key);
                listOLists.add(list);
            }
            //for the json response
            contenu.put("target", "sentiment.polarity");
            contenu.put("datapoints", listOLists);
            System.out.println(contenu);

            //System.out.println(returning);
            
            finalVar.add(contenu);
        }
        if(o.indexOf("sentiment.subjectivity")!=-1){
            ArrayList<Tweet> all=tweetRepository.findByUsername(username);
            JSONObject contenu = new JSONObject();

            ArrayList<ArrayList<Object>> listOLists = new ArrayList<>();

            Map<Object, Object> sorted = new HashMap<>();

            for(Tweet tweet:all)
            {
                if(tweet.getProject_id()==projectId)
                {
                    sorted.put(new Date((String) tweet.getDate()).getTime(), tweet.getSentiment_subjectivity());
                }
            }
            TreeMap<Object, Object> sortedTree = new TreeMap<>(sorted);
            sortedTree.putAll(sorted);

            for(Map.Entry<Object, Object> entry : sortedTree.entrySet()) {
                Object key = entry.getKey();

                Object value = entry.getValue();
                ArrayList<Object> list = new ArrayList<>();
                JSONObject data= new JSONObject();
                list.add( value);
                list.add( key);
                listOLists.add(list);
            }
            //for the json response
            contenu.put("target", "sentiment.subjectivity");
            contenu.put("datapoints", listOLists);
            System.out.println(contenu);

            //System.out.println(returning);
            finalVar.add(contenu);
            
            
        }
System.out.println(finalVar);
        return finalVar;






    }
    @PostMapping("/annotation")
    public ArrayList<JSONObject> annotation(@RequestBody JSONObject requet, @RequestHeader(value="Authorization") String jwt)
    {
        ArrayList<JSONObject> response = new ArrayList<JSONObject>();
        JSONObject o = new JSONObject();
        o.put("text", "sentiments");
        o.put("title", "sentiments from twitter");
        o.put("time", "timestamp");
        response.add(o);
        return response;
    }



        }





