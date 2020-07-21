package com.model;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Document(indexName = "twitter", type = "json",shards=2)
public class Tweet{
    @Id
    Long id;
    int project_id;
    String username;
    String text;
    String lang;
    String OLDtext;
    String date;
    double sentiment_polarity;
    double sentiment_subjectivity;

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", project_id=" + project_id +
                ", username='" + username + '\'' +
                ", text='" + text + '\'' +
                ", lang='" + lang + '\'' +
                ", OLDtext='" + OLDtext + '\'' +
                ", date='" + date + '\'' +
                ", sentiment_polarity=" + sentiment_polarity +
                ", sentiment_subjectivity=" + sentiment_subjectivity +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOLDtext() {
        return OLDtext;
    }

    public void setOLDtext(String OLDtext) {
        this.OLDtext = OLDtext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getSentiment_polarity() {
        return sentiment_polarity;
    }

    public void setSentiment_polarity(double sentiment_polarity) {
        this.sentiment_polarity = sentiment_polarity;
    }

    public double getSentiment_subjectivity() {
        return sentiment_subjectivity;
    }

    public void setSentiment_subjectivity(double sentiment_subjectivity) {
        this.sentiment_subjectivity = sentiment_subjectivity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Tweet() {
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
