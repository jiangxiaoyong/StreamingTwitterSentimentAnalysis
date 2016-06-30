package com.streaming.analysis

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.classification.{NaiveBayes}


/**
  * Created by jxy on 2016-06-30.
  */
object Train {
  def main(args: Array[String]): Unit = {
    println("Initializing Spark Context for training machine learning model...")
    val sparkConf = new SparkConf().setAppName("Train Naive Bayes model").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //load training data set
    println("Loading training data set ...")
    val rawTrainingDataSet = sc.textFile("src/main/resources/training.dataset.csv")
    val trainingDataSet = rawTrainingDataSet.map(lines => TrainingUtils.toTuple(lines))
                                            .map(x => (x._1, TrainingUtils.featureVectorization(x._2)))
                                            .map(x => new LabeledPoint((x._1).toDouble, x._2))
    trainingDataSet.foreach(print)

    //Begin training model
    println("******** Training *********")
    val model =NaiveBayes.train(trainingDataSet, lambda = 1.0, modelType = "multinomial")
    println("******** Finish Training *******")
  }
}
