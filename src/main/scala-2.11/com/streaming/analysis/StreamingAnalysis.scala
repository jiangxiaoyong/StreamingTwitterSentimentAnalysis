package com.streaming.analysis

import java.io.InputStream
import java.util.Properties

import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming._
import _root_.kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka._
import org.apache.spark.mllib.classification.NaiveBayesModel
import twitter4j.TwitterObjectFactory
import org.apache.spark.{SparkConf, SparkContext}

class StreamingAnalysis {
  println("Initializing Streaming Spark Context...")
  val sparkConf = new SparkConf().setAppName("Twitter sentiment analysis").setMaster("local[*]")
  val sc = new StreamingContext(sparkConf, Seconds(5))
  sc.checkpoint("/SparkCheckPoint") //needed for stateful transformation

  //load stop words
//  println("loading stop words...")
//  val stopWordsStream: InputStream = getClass.getResourceAsStream("/stopwords.txt")
//  val stopWords = scala.io.Source.fromInputStream(stopWordsStream).getLines.toSet

  println("Initializing streaming parameters...")
  val props = new Properties()
  val stream : InputStream = getClass.getResourceAsStream("/consumer.properties")
  props.load(stream)
  val KAFKA_BROKERS = props.getProperty("metadata.broker.list")
//  val topic = Set(props.getProperty("tweet.topic"))
  val topic = Set("tweet")
  val kafkaParams = Map[String, String]("metadata.broker.list" -> KAFKA_BROKERS)


  def start = {
    val tweetsStreaming = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder] (sc, kafkaParams, topic)
    val rawTweets = tweetsStreaming.map(_._2)
    rawTweets.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/rawResults/tweet")

    val rawLocationText= rawTweets.map(TwitterObjectFactory.createStatus).map(CustomizeTweet(_))
    rawLocationText.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/rawLocationText/ct")

    //filtering tuple, sanitise location
    val cityText = rawLocationText.filter(x => TweetUtils.sanitiseLocation(x._1)).map{ case (k,v) => (k.split(",")(0), v)} //location from tweet, "city, country"
    cityText.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/cityText/ct")

    //filtering tuple, sanitise text
    val cleanCityText =  cityText.map{ case (k, v) => (k, TweetUtils.filterOnlyWords(v))}.filter(x => TweetUtils.filterEmptyString(x._2))
    cleanCityText.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/cleanCityText/ct")

    //loading Naive Bayes model
    println("Loading the Naive Bayes model...")
    val model = NaiveBayesModel.load(sc.sparkContext, "src/main/model/")

    println("Start predicting...")
    //data format:  (city, (msg, predicted value))
    val cityTextPredictedValue= cleanCityText.map(x => (x._1, (x._2, model.predict(TrainingUtils.featureVectorization(x._2)))))
    cityTextPredictedValue.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/cityTextPredictedValue/ct") // save this data structure for reference and debugging

    //predicted value for each city
    //data format: (city, 4.0) or (city, 1.0)
    val cityPredictedValue = cleanCityText.map(x => (x._1, model.predict(TrainingUtils.featureVectorization(x._2)).toString))

    //group predicted values for each city
    //data format: (city, 4.0 1.0 4.0)
    val reducedCityPredictedValue = cityPredictedValue.reduceByKey((x, y) => x + " " + y)
    reducedCityPredictedValue.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/reducedCityPredictedValue/ct")

    //classify positive predicted value and counting them
    //data format: (city, num of positive value) e.g. (city, 2) ...
    val numPositivePredictedValue = reducedCityPredictedValue.map(x => (x._1, x._2.split(" ").filter( x => x.toDouble == 4.0).mkString(" "))) // filtering negative value
                                                             .map{ case (k:String, v:String) => if(!v.isEmpty) (k, v.split(" ").length) else (k, 0)} //counting the number of positive predicted value
    numPositivePredictedValue.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/numPositivePredictedValue/ct")

    //accumulate counting for positive sentiment tweet to their cities, accumulate from beginning of the app
    val totalPositiveTweetPerCity = numPositivePredictedValue.updateStateByKey(TweetUtils.accumulateSentimentCount _ )
    totalPositiveTweetPerCity.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/totalPositiveTweetPerCity/ct")

    //classify negative predicted value and counting them
    //data format: (city, num of negative value) e.g. (city, 2) ...
    val numNegativePredictedValue = reducedCityPredictedValue.map(x => (x._1, x._2.split(" ").filter( x => x.toDouble == 0.0).mkString(" "))) //filtering positive value
                                                             .map{ case (k:String, v:String) => if(!v.isEmpty) (k, v.split(" ").length) else (k, 0)} //counting the number of positive predicted value
    numNegativePredictedValue.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/numNegativePredictedValue/ct")

    //accumulate counting for positive sentiment tweet to their cities, accumulate from beginning of the app
    val totalNegativeTweetPerCity = numNegativePredictedValue.updateStateByKey(TweetUtils.accumulateSentimentCount _ )
    totalNegativeTweetPerCity.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/totalNegativeTweetPerCity/ct")

    //count positive and negative predicted vale for each city

    // Start the streaming computation
    println("Spark Streaming begin...")
    sc.start()
    sc.awaitTermination()
  }
}
