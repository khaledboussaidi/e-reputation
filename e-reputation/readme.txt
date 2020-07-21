docker-compose up --build -d --scale app=2  --scale requestanalysis=2 --scale traduction=2

version: '3.3'
services:

    zookeeper:
        image: confluentinc/cp-zookeeper:latest
        ports:
            - 2181:2181
        environment:
            ZOOKEEPER_CLIENT_PORT: 2181
            ZOOKEEPER_TICK_TIME: 2000
    kafka:
            image: confluentinc/cp-kafka:latest
            ports:
                - 9092:9092
            depends_on:
                - zookeeper
            environment:
                KAFKA_BROKER_ID: 1
                KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
                # For more details see See https://rmoff.net/2018/08/02/kafka-listeners-explained/
                KAFKA_LISTENERS: LISTENER_INSIDE://0.0.0.0:29092,LISTENER_HOST://0.0.0.0:9092
                KAFKA_ADVERTISED_LISTENERS: LISTENER_INSIDE://kafka:29092,LISTENER_HOST://localhost:9092
                KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: LISTENER_INSIDE:PLAINTEXT,LISTENER_HOST:PLAINTEXT
                KAFKA_INTER_BROKER_LISTENER_NAME: LISTENER_INSIDE
                KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
                KAFKA_HEAP_OPTS: " -Xmx256m -Xms256m"
    elasticsearch:
        image: docker.elastic.co/elasticsearch/elasticsearch:7.1.1
        ports:
            - 9200:9200
            - "9300:9300"
        environment:
            discovery.type: single-node
            http.cors.enabled: "true"
            http.cors.allow-origin: "*"
            ES_JAVA_OPTS: " -Xms256m -Xmx256m"
    app:
        build: app
    requestanalysis:
        build: requestanalysis/app
    nginxapp:
        container_name: straem_nginx_container
        build: nginx
        ports:
            - 8080:8080
        depends_on:
           - app
           - kafka
    nginxrequestanalysis:
        container_name: requestanalysis_nginx_container
        build: nginxRequestAnalysis
        ports:
            - 8081:8081
        depends_on:
            - requestanalysis
            - kafka
    traduction:
        build: traduction/app
    ngnixtarduction:
        container_name: tarduction_ngnix_container
        build: ngnixtarduction
        ports:
            - 8082:8082
        depends_on:
            - requestanalysis
            - kafka

    signup:
        build: signup
        depends_on:
            - kafka
        ports:
            - '8083:8083'
    projectcrud:
        build: projectcrud
        depends_on:
            - kafka
        ports:
            - '8084:8084'

    requestanalyser:
        build: requestanalyser
        depends_on:
            - kafka
        ports:
            - '8085:8085'
    db:
        image: mysql:5.7
        restart: always
        environment:
            MYSQL_DATABASE: 'db'
            # So you don't have to use root, but you can if you like
            MYSQL_USER: 'khaled'
            # You can use whatever password you like
            MYSQL_PASSWORD: '98553393houssem'
            # Password for root access
            MYSQL_ROOT_PASSWORD: '98553393houssem'
        ports:
            # <Port exposed> : < MySQL Port running inside container>
            - '3308:3308'
        expose:
            # Opens port 3306 on the container
            - '3306'
            # Where our data will be persisted
        volumes:
            - my-db:/var/lib/mysql

# Names our volume
volumes:
    my-db:
