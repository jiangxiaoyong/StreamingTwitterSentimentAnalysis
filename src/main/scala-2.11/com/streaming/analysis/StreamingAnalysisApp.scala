package com.streaming.analysis

import org.apache.kafka.clients.producer.KafkaProducer

/**
  * Created by jxy on 2016-06-22.
  */
object StreamingAnalysisApp {
  def main(args: Array[String]): Unit = {
    val streamingApp = new StreamingAnalysis with KafkaProducer
    streamingApp.start
  }

}
