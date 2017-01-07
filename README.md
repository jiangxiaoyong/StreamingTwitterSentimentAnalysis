Docker instructions
=====================================
Overview
-------------------------------------
A big data project combined with Machine leaning algorithm running on Apache Spark

Here is [my post](https://jiangxiaoyong.github.io/portfolio/sentimentAnalysis/) explaining detail of this project

Kafka Consumer Docker
-------------------------------------
```
docker run -it --name consumer --expose 8888 --expose 4040 -p 8888:8888 -p 4040:4040 -v ~/IdeaProjects/StreamingTwitterSentimentAnalysis/:/app -d jiangxiaoyong/consumer
```
- 4040 is spark UI
- 8888 is debugging port

Streaming Twitter Sentiment Analysis project complete running instructions
=====================================
The complete project comprise four different parts of modules as below
- [Twitter producer](https://github.com/jiangxiaoyong/TwitterProducer)
- Kafka cluster
  - Apache zookeeper
  - Apache Kafka
- Kafka consumer
  - Spark Streaming
  - Naive Bayes Model
- [Scala-play server](https://github.com/jiangxiaoyong/play-scala)

Running procedural and instructions
------------------------------------
- start set of dockers
```
$ docker start zookeeper kafka producer consumer playScala
```
- run zookeeper and kafka servers inside docker
```
$ docker exec zookeeper /zookeeper-3.4.8/bin/zkServer.sh start
$ docker exec -it kafka /kafka_2.11-0.10.0.0/bin/kafka-server-start.sh /kafka_2.11-0.10.0.0/config/server.properties -d --override zookeeper.connect=${ZOOKEEPER_PORT_2181_TCP_ADDR}:${ZOOKEEPER_PORT_2181_TCP_PORT}

```
- run producer inside of producer container
```
$ docker exec -it producer /bin/bash
$ cd /app
$ sbt
>run
```

- run consumer inside of consumer container
```
$ docker exec -it consumer /bin/bash
$ cd /app
$ sbt
>run
```

- run scala-play server inside of playScala container
```
$ docker exec -it playScala /bin/bash
$ cd /app
$ activator run
```

- connect and send message to play server via WebSocket at index home page of web client
  - see detail instructions at [Scala-play repo](https://github.com/jiangxiaoyong/play-scala)

Training Naive Bayes Model
------------------------------------
- modify sbt file to specify which main file as the entry point
- training data set from Sentiment140, total training data volume 1,600,000
- run consumer inside consumer docker

SBT instructions
====================================
Debug mode
-----------------------------------
```
cd /app
sbt -jvm-debug 8888
```
