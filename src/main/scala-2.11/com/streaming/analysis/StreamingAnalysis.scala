package com.streaming.analysis

import java.io.InputStream
import java.util.Properties

import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming._
import _root_.kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.classification.NaiveBayesModel
import twitter4j.TwitterObjectFactory

class StreamingAnalysis {
  println("Initializing Streaming Spark Context...")
  val sparkConf = new SparkConf().setAppName("Twitter sentiment analysis").setMaster("local[*]")
  val sc = new StreamingContext(sparkConf, Seconds(5))
  sc.checkpoint("/SparkCheckPoint") //needed for stateful transformation

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
//    cleanCityText.print()
    cleanCityText.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/cleanCityText/ct")

    //loading Naive Bayes model
    println("Loading the Naive Bayes model...")
    val model = NaiveBayesModel.load(sc.sparkContext, "src/main/model/")

    println("Start predicting...")
    //data structure:  (city, (msg, predicted value))
    val cityTextPredictedValue= cleanCityText.map( x => (x._1, (x._2, model.predict(TrainingUtils.featureVectorization(x._2)))))
//    cityTextPredictedValue.print()
    cityTextPredictedValue.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/cityTextPredictedValue/ct") // save this data structure for debugging

    //predicted value for each city
    val cityPredictedValue = cleanCityText.map( x => (x._1, model.predict(TrainingUtils.featureVectorization(x._2)).toString))
//    cityPredictedValue.print()

    //group messages to their cities, accumulated from beginning of the app
    val allMessagesPerCity = cityPredictedValue.updateStateByKey(TweetUtils.groupMessages _)
    allMessagesPerCity.print()
    allMessagesPerCity.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/allMessagesPerCity/ct")

    // Start the streaming computation
    println("Spark Streaming begin...")
    sc.start()
    sc.awaitTermination()
  }
}
