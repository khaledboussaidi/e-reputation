package com.repository;

import com.model.Tweet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TweetRepository extends ElasticsearchRepository<Tweet, String> {

    ArrayList<Tweet> findByUsername(String name);

    @Override
    <S extends Tweet> S save(S s);
}
