Docker instructions
=====================================
Consumer Docker
-------------------------------------
```
docker run -it --name consumer --expose 8888 --expose 4040 -p 8888:8888 -p 4040:4040 -v ~/IdeaProjects/StreamingTwitterSentimentAnalysis/:/app -d jiangxiaoyong/consumer
```

Running instructions
=====================================
start set of dockers
------------------------------------
```
docker start zookeeper kafka producer consumer
```

run producer inside producer docker
------------------------------------
```
docker exec -it producer /bin/bash
cd /app
sbt
>run
```

run consumer inside consumer docker
------------------------------------
```
docker exec -it consumer /bin/bash
cd /app
sbt
>run
```

Training Naive Bayes Model
=====================================
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