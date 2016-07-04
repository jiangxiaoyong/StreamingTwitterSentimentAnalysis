package com.streaming.analysis

import java.util.Properties
import java.io.InputStream
/**
  * Created by jxy on 2016-07-03.
  */
object KafkaConfig {
  val props = new Properties()
  val stream : InputStream = getClass.getResourceAsStream("/consumer.properties")
  props.load(stream)

  val KAFKA_SENTIMENT_TOPIC = props.getProperty("twitter.sentiment.analysis.topic")

  val kafkaProps = new Properties()
  List(
    "serializer.class",
    "metadata.broker.list",
    "request.required.acks"
  ) foreach(s => kafkaProps.put(s, props.get(s)))
}
