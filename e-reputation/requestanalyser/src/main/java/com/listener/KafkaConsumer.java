package com.listener;

import com.analyse.AnalyseSentiment;
import com.model.Tweet;
import com.repository.TweetRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {
    @Autowired
    private KafkaTemplate<String, JSONObject> kafkaTemplate;
    @Autowired
    private TweetRepository tweetRepository;

    private static final AnalyseSentiment analyseSentiment =new AnalyseSentiment();

    /*@KafkaListener(topics = "nintendo_tweets_raw", group = "my-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }*/


    @KafkaListener(topics = "onDataTweets", groupId = "streamer-group",
            containerFactory = "streamerKafkaListenerFactory")
    public void streamerConsumeJsonTweets(JSONObject jsonObject) {
        try{
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("text",jsonObject.get("text"));
            jsonObject1.put("lang",jsonObject.get("lang"));
            jsonObject1.put("created_at",jsonObject.get("created_at"));
            //jsonObject.remove("text");
            //jsonObject.remove("lang");
            JSONObject jsonObject2=new JSONObject();
            jsonObject2=analyseSentiment.getAnalysedTweets(jsonObject1);

            Tweet tweet=new Tweet();
            tweet.setDate((String) jsonObject2.get("date"));
            tweet.setLang((String) jsonObject2.get("text"));
            tweet.setOLDtext((String) jsonObject2.get("OLDtext"));
            tweet.setSentiment_polarity((Double) jsonObject2.get("sentiment.polarity"));
            tweet.setSentiment_subjectivity((Double) jsonObject2.get("sentiment.subjectivity"));
            tweet.setId((Long)jsonObject.get("id"));
            tweet.setProject_id((int) jsonObject.get("project_id"));
            tweet.setUsername((String)jsonObject.get("username"));
            tweetRepository.save(tweet);
            System.out.println("saved");

        } catch (Exception e) {
            System.out.println(e);
        }


    }

        //kafkaTemplate.send("sentimentanalyse",jsonObject);
        //System.out.println(jsonObject.get("text"));


    /*@KafkaListener(topics = "sentimentanalysed", group = "analyser-group",
            containerFactory = "streamerKafkaListenerFactory")
    public void SentimentAnalyserConsumeJson(JSONObject jsonObject) {
        System.out.println("analysed Message: " + jsonObject.get("text"));
    }*/
}
