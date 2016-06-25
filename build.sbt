name := "StreamingTwitterSentimentAnalysis"

version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "1.6.1"
libraryDependencies ++=  Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,

  "org.twitter4j" % "twitter4j-core" % "4.0.4",
  "org.twitter4j" % "twitter4j-stream" % "4.0.4"
)

