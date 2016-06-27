package com.streaming.analysis

import java.io.InputStream
import java.util.Properties

import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming._
import _root_.kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import twitter4j.TwitterObjectFactory

class StreamingAnalysis {
  println("Initializing Streaming Spark Context...")
  val sparkConf = new SparkConf().setAppName("Twitter sentiment analysis").setMaster("local[*]")
  val sc = new StreamingContext(sparkConf, Seconds(5))

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
    rawLocationText.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/countryText/ct")

    //filtering tuple, sanitise location
    val cityText = rawLocationText.filter(x => FilterUtils.sanitiseLocation(x._1)).map{ case (k,v) => (k.split(",")(0), v)}
    cityText.window(Seconds(5), Seconds(5)).saveAsTextFiles(s"/cleanCityText/ct")

    // Start the streaming computation
    println("Spark Streaming begin...")
    sc.start()
    sc.awaitTermination()
  }
}
