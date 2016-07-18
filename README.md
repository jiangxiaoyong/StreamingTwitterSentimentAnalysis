Docker instructions
=====================================
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
1. [Twitter producer](https://github.com/jiangxiaoyong/TwitterProducer)
2. Kafka cluster
⋅⋅* Apache zookeeper
⋅⋅* Apache Kafka
3. Kafka consumer
⋅⋅* Spark Streaming
⋅⋅* Naive Bayes Model
4. [Scala-play server](https://github.com/jiangxiaoyong/play-scala)

Running procedural and instructions
------------------------------------
- start set of dockers
```
docker start zookeeper kafka producer consumer playScala
```

- run producer inside of producer container
```
docker exec -it producer /bin/bash
cd /app
sbt
>run
```

- run consumer inside of consumer container
```
docker exec -it consumer /bin/bash
cd /app
sbt
>run
```

- run scala-play server inside of playScala container
```
docker exec -it playScala /bin/bash
cd /app
activator run
```

- connect and send message to play server via WebSocket at index home page of web client
⋅⋅* see detail instructions at [Scala-play repo](https://github.com/jiangxiaoyong/play-scala)

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