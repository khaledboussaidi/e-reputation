from datetime import timedelta

from flask import Flask,  request, jsonify

from functools import wraps
import requests
from ibm_watson import LanguageTranslatorV3
from ibm_cloud_sdk_core.authenticators import IAMAuthenticator, BasicAuthenticator
from textblob import TextBlob
app = Flask(__name__)

def required(fn):
    @wraps(fn)
    def wrapper(*args, **kwargs):
        r=requests.get("http://10.108.233.26:80/jwt",headers=request.headers)
        if r.status_code != 200:
            return jsonify(msg='acces denied'), 403
        else:
            return fn(*args, **kwargs)
    return wrapper

@app.route("/ok", methods=['POST'])

#producer=KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),
 #                      api_version=(0, 10, 1), bootstrap_servers=['0.0.0.0:29092','localhost:9092','kafka:29092','0.0.0.0:9092'])

#consumer = KafkaConsumer('toTranslate',
#    bootstrap_servers=['0.0.0.0:29092','localhost:9092','kafka:29092','0.0.0.0:9092'],api_version=(0, 10, 1),
#    auto_offset_reset='earliest',
#    group_id='toTranslate1',
#    #auto_commit_interval_ms=1000,
#    enable_auto_commit=True,
#    value_deserializer=lambda m: json.loads(m.decode('utf-8')))


#def translate(source_language,destination_language,text):
#    #caps = DesiredCapabilities.FIREFOX
 #   #chrome_options = webdriver.ChromeOptions()
#    chrome_options = Options()
#    chrome_options.add_argument('--headless')
#   chrome_options.add_argument('--no-sandbox')
#    chrome_options.add_argument('--disable-dev-shm-usage')
#    driver = webdriver.Chrome(chrome_options=chrome_options)
#    driver.get(f"https://translate.google.com/#view=home&op=translate&sl="+source_language+"&tl="+destination_language+"&text="+text)
#    output= driver.find_element_by_xpath("/html/body/div[2]/div[2]/div[1]/div[2]/div[1]/div[1]/div[2]/div[3]/div[1]/div[2]/div/span[1]")
#    translated=output.text
#    sleep(2)
#    driver.close()
#    return translated


#####traslate with textblob#########
@app.route("/translate", methods=['POST'])
@required
def trans():
    data = request.json
    try:
        data['text']= TextBlob(data['text'])
        data['text'] = data['text'].translate(from_lang=request.json['sl'], to=request.json['tl'])
        return str(data['text'])
    except:
        url = 'https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/d6835578-80d4-404f-a411-4b7b59a7c220'
        key = 'wotp8LS4SGYORf1a-TpqR9R5Bmeyg1Q3jbQjbueNc-Xi'
        model_id = request.json['sl'] + '-' + request.json['tl']
        authenticator = IAMAuthenticator(key)
        language_translator = LanguageTranslatorV3(
            version='2018-05-01',
            authenticator=authenticator
        )
        language_translator.set_service_url(url)
        translation = language_translator.translate(
            text=data['text'],
            model_id=model_id).get_result()
    return translation['translations'][0]['translation']


####### do nothing #####################################"
def translate(source_language,destination_language,text):
    return text



@app.route("/trans", methods=['POST'])
@required
def index():
    print(request.json)
    return translate(request.json['sl'],request.json['tl'],request.json["text"])

#def startListner():
 #   for message in consumer:
  #      try:
            #message.value["text"] = translate(message.value["lang"], 'en', message.value["text"])
            #message.value["lang"] = "en"
   #         producer.send("sentimentanalyse", message.value)
   #         print('traduire le texte :'+ message.value["text"])
    #    except BaseException as e:
     #       print("Error on_data %s" % str(e))




@required
def ok():
    return request.json

if __name__ == '__main__':
    app.run('0.0.0.0',port=5002)
