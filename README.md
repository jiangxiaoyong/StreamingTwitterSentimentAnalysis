Docker instructions
=====================================
Consumer Docker
-------------------------------------
- docker run -it --name consumer --expose 8888 --expose 4040 -p 8888:8888 -p 4040:4040 -v ~/IdeaProjects/StreamingTwitterSentimentAnalysis/:/app -d jiangxiaoyong/consumer