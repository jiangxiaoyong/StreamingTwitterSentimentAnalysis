package com.streaming.analysis
import kafka.producer.{ProducerConfig, Producer}

/**
  * Created by jxy on 2016-07-03.
  */
trait KafkaProducer {
  val producer = new Producer[String, String](new ProducerConfig(KafkaConfig.kafkaProps))
}
