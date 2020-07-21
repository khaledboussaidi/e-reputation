import requests
from flask import Flask, jsonify, request
from threading import Thread
from kafka import KafkaProducer, KafkaConsumer
from elasticsearch import Elasticsearch
from random import randint
import json
from functools import wraps

from tweepy import StreamListener, API, Cursor, OAuthHandler, Stream

ACCESS_TOKEN = "1168210697253244930-RSDdsMeJY1Wv5TwdCatAKrkg1E6GeZ"
ACCESS_TOKEN_SECRET = "TjGj4YwnXVzv92k9B2Bvu7biqTZlTN6YVbw0SxJQARjmv"
CONSUMER_KEY = "0kSn4U7h8g6cgscWcXmxD5Q0R"
CONSUMER_SECRET = "kB5iqYrMagftdqbiw7uYBF9MUDa3hWR2PzrniiCdNsiA2BTW7b"

class TwitterClient:
    def __init__(self, twitter_user=None):
        self.auth = TwitterAuthenticator().authenticate_twitter_app()
        self.twitter_client = API(self.auth)

        self.twitter_user = twitter_user

    def get_user_timeline_tweets(self, num_tweets):
        tweets = []
        for tweet in Cursor(self.twitter_client.user_timeline, id=self.twitter_user).items(num_tweets):
            tweets.append(tweet)
        return tweets

    def get_friend_list(self, num_friends):
        friend_list = []
        for friend in Cursor(self.twitter_client.friends, id=self.twitter_user).items(num_friends):
            friend_list.append(friend)
        return friend_list

    def get_home_timeline_tweets(self, num_tweets):
        home_timeline_tweets = []
        for tweet in Cursor(self.twitter_client.home_timeline, id=self.twitter_user).items(num_tweets):
            home_timeline_tweets.append(tweet)
        return home_timeline_tweets


# # # # TWITTER AUTHENTICATER # # # #
class TwitterAuthenticator:

    @staticmethod
    def authenticate_twitter_app():
        auth = OAuthHandler(CONSUMER_KEY,CONSUMER_SECRET)
        auth.set_access_token(ACCESS_TOKEN,ACCESS_TOKEN_SECRET)
        return auth


# # # # TWITTER STREAMER # # # #
class TwitterStreamer:
    """
    Class for streaming and processing live tweets.
    """

    def __init__(self,):
        self.twitter_aut = TwitterAuthenticator()
        self.stream = None

    def stream_tweets(self, fetched_tweets_filename, hash_tag_list,project_id,username,language):
        # This handles Twitter authetification and the connection to Twitter Streaming API
        listener = TwitterListener(fetched_tweets_filename,project_id,username,language)
        auth = self.twitter_aut.authenticate_twitter_app()
        self.stream = Stream(auth, listener)

        # This line filter Twitter Streams to capture data by the keywords:
        self.stream.filter(track=hash_tag_list)
    def stream_diconnect(self):
        self.stream.disconnect()


# # # # TWITTER STREAM LISTENER # # # #
class TwitterListener(StreamListener):
    """
    This is a basic listener that just prints received tweets to stdout.
    """

    def __init__(self, fetched,project_id,username,language):
        self.fetched = fetched
        self.project_id=project_id
        self.username=username
        self.language=language

    def on_data(self, data):
        try:
            data=json.loads(data)
            data['project_id']=self.project_id
            data['username'] = self.username
            #data['language'] = self.language
            #with open(self.fetched, 'a') as tf:
                #tf.write(data)

            if(data['lang']==self.language or self.language=='all'):
                print(data)
                self.fetched.send('onDataTweets', data)
                es.index(index="e_reputation", doc_type="test-type", id=data["id"], body=data)

            return True
        except BaseException as e:
            print("Error on_data %s" % str(e))
        return True

        #self.fetched.flush()

    def on_error(self, status):
        if status == 420:
            # Returning False on_data method in case rate limit occurs.
            return False
        print(status)





def hashTag(word):
    list=word.split(' ')
    while(True):
        try:
            list.remove('')
        except:
            break
    return list





app = Flask(__name__)
random=randint(0,100)

listOfStreamer= dict()
es = Elasticsearch([{'host':'a02e34849746b4f9898a61effd2fb4b1-77451157.eu-west-1.elb.amazonaws.com','port':9200}])
consumer = KafkaConsumer('stop',
    bootstrap_servers=['localhost:9092'],api_version=(0, 10, 1),
    auto_offset_reset='earliest',
    group_id='my-group'+str(random),
    auto_commit_interval_ms=1000,
    enable_auto_commit=True,
    value_deserializer=lambda m: json.loads(m.decode('utf-8')))
fetched_tweets = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                                                        api_version=(0, 10, 1), bootstrap_servers=['localhost:9092'])#,'kafka:29092','0.0.0.0:9092','0.0.0.0:29092'])
#twitter_streamer = TwitterStreamer()

def required(fn):
    @wraps(fn)
    def wrapper(*args, **kwargs):
        r=requests.get("http://localhost:8084/jwt",headers=request.headers)
        if r.status_code != 200:
            return jsonify(msg='acces denied'), 403
        else:
            return fn(*args, **kwargs)
    return wrapper

def start(project_id, username, cle, language):
        if cle != '':
            if (project_id in listOfStreamer):
                hash_tag_list = hashTag(cle)
                listOfStreamer[project_id].stream_diconnect()
                listOfStreamer[project_id].stream_tweets(fetched_tweets, hash_tag_list, project_id, username, language)
            else:
                listOfStreamer[project_id] = TwitterStreamer()
                hash_tag_list = hashTag(cle)
                listOfStreamer[project_id].stream_tweets(fetched_tweets, hash_tag_list, project_id, username, language)
        return 'ok', 200

@app.route('/project/launch/<int:project_id>/<string:username>/<string:cle>/<string:language>', methods=['GET'])
#@required
def do(project_id, username, cle, language):
    Thread(target=start, args=(project_id, username, cle, language)).start()
    return 'ok', 200




def stopListner(listOfStreamer):
    for message in consumer:
        try:
            listOfStreamer[message.value["project_id"]].stream_diconnect()
            print(message)
        except BaseException as e:
            print("Error on_data %s" % str(e))
            pass


Thread(target=stopListner,args=(listOfStreamer,)).start()

if __name__ == '__main__':
    app.run('0.0.0.0',port=5000)





