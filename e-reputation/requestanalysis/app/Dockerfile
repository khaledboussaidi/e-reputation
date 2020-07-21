# using a python small basic image
FROM python:3.8
# exposing our stream port in docker internal network
EXPOSE 5001
# creates a dir for our application
WORKDIR /app
# copy our requirements.txt file and install dependencies
COPY requirements.txt .
RUN pip3 install -r requirements.txt
# copy the rest of our application
COPY . .
# run the application
CMD python app.py
