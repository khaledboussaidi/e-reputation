import re
from datetime import datetime
from functools import wraps

from threading import Thread

#import pymongo
import requests
from textblob import TextBlob
from flask import Flask, jsonify
from kafka import KafkaConsumer, KafkaProducer

import json
from flask import request



app = Flask(__name__)

#myclient = pymongo.MongoClient("mongodb://localhost:27017/")
#mydb = myclient["streamdatabase"]

"""def PolygonToPoint(poly_array):
    try:
        coo = np.array(poly_array)
        center = np.mean(coo, 0)
    except:
        center = [0, 0]
    return center

def TimesTamp(value):
    return datetime.strptime(
        value,"%a %b %d %H:%M:%S %z %Y"
        ).strftime('%Y/%m/%d %H:%M:%S')

def PlaceCenter(message):
    if message.get('place') and message.get('place').get('bounding_box') and message.get('place').get(
            'bounding_box').get('coordinates'):

        try:
            coo = np.array(message['place']['bounding_box']['coordinates'][0])
            center = PolygonToPoint(coo)
            geo_point = Point((center[0], center[1]))
        except:
            geo_point =  Point((41.12,-71.34))
        return geo_point

    return Point((41.12,-71.34))"""

#producer=KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),
#                       api_version=(0, 10, 1), bootstrap_servers=['0.0.0.0:29092','localhost:9092','kafka:29092'])


#consumer = KafkaConsumer('sentimentanalyse',
#    bootstrap_servers=['0.0.0.0:29092','localhost:9092','kafka:29092'],api_version=(0, 10, 1),
#    auto_offset_reset='earliest',
#    group_id='my-group1',
#    auto_commit_interval_ms=1000,
#    enable_auto_commit=True,
#    value_deserializer=lambda m: json.loads(m.decode('utf-8')))
def clean_tweets(text):
    return ' '.join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Aa-z\t])|(\w+:\/\/\S+)", " ", text ).split())


class LiveConsummerAnalyser:

    def __init__(self, message=None):
        self.message=message


    def AnalyseRequest(self):
        try:
            timestamp = datetime.strptime(
                self.message['created_at'], "%a %b %d %H:%M:%S %z %Y"
            ).strftime('%Y/%m/%d %H:%M:%S')
            tweet_text = clean_tweets(str(self.message["text"]))
            tweet_text_blob = TextBlob(tweet_text)
            self.message['date']=timestamp
            self.message['sentiment.polarity'] = tweet_text_blob.sentiment.polarity
            self.message['sentiment.subjectivity'] = tweet_text_blob.sentiment.subjectivity

        except BaseException as e:
            print("Error on_data %s" % str(e))


    def getMessage(self):
        return self.message
    def setMessage(self,message):
        self.message=message





def required(fn):
    @wraps(fn)
    def wrapper(*args, **kwargs):
        r=requests.get("http://localhost:8084/jwt",headers=request.headers)
        if r.status_code != 200:
            return jsonify(msg='acces denied'), 403
        else:
            return fn(*args, **kwargs)
    return wrapper







#kafkaliveConsumer=LiveConsummerAnalyser()
RestliveConsumer=LiveConsummerAnalyser()

#def sendAnalysedTweets(task):
 #   requests.post("http://localhost:8080/analysedtweets",json=task)


@app.route('/analysesentiment',methods=['POST'])
##@required
def AnalyseSentimet():
    try:
                # print(message.value)
                if (request.json["lang"] != "en"):
                    data={"sl":request.json["lang"],"tl":"en","text":request.json["text"]}
                    r = requests.post("http://10.108.233.23:30003/trans",json=data, headers=request.headers)
                    request.json["OLDlang"]=request.json["lang"]
                    request.json["lang"]="en"
                    request.json["OLDtext"]=request.json["text"]
                    request.json["text"]=r.text
                    print(r.text)
                    RestliveConsumer.setMessage(request.json)
                    RestliveConsumer.AnalyseRequest()
                    #producer.send('sentimentanalysed', RestliveConsumer.getMessage())
                    #print('analyser le sentiment : ' + RestliveConsumer.getMessage()["text"])
                    return request.json

                else:
                    RestliveConsumer.setMessage(request.json)
                    RestliveConsumer.AnalyseRequest()
                    #producer.send('sentimentanalysed', RestliveConsumer.getMessage())
                    print('analyser le sentiment : '+str(RestliveConsumer.getMessage()))
                    return request.json
    except BaseException as e:
        print("Error on_data %s" % str(e))


#def consume():
#    for message in consumer:
#        # message.value["text"]=clean_tweets(str(message.value["text"]))
#        try:
#            # print(message.value)
#            if (message.value["lang"] != "en"):
#                producer.send('toTranslate', message.value)
#            else:
#                kafkaliveConsumer.setMessage(message.value)
#                kafkaliveConsumer.AnalyseRequest()
#                producer.send('sentimentanalysed', kafkaliveConsumer.getMessage())
#                print('analyser le sentiment'+kafkaliveConsumer.getMessage()["text"])
#        except BaseException as e:
#            print("Error on_data %s" % str(e))


#Thread(target=consume).start()



if __name__ == '__main__':
    app.run('0.0.0.0',port=5001)
