import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.HashingTF
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}


/*---------- assigning a field to block ---*/
class foo {
  lazy val test =  {
    val a = 1
    val b = 1
    println(a + b)
  }
}

val f = new foo
f.test

Set("tweet")

object AddOne {
  def apply(m: Int): Int = m + 1
}

val plusOne = AddOne(1)

/*----------------  Option ------------------*/

def toOption(value: String): Option[String] = {
  if (value == null) None else Some(value)
}
def getUserLocation(location: Option[String]) = location match {
  case Some(s) => s
  case None => "?"
}

val x = getUserLocation(toOption(null))
println(x)


/*------------------- String --------------------*/
"some value  hah" contains ","

val value  = "some, value" split ","
value.isEmpty
value(0)

val line = "1 2 3"
Vectors.dense(line.split(" ").map(_ toDouble))

val slidingString = "abcdefg higk"
slidingString.sliding(3).toSeq

val tmp = "a, b,c" split(",") drop(1) mkString(" ")
val tmp2= "\"a\", \"b\", \"c\"" replaceAll("^\"|\"$", "")
val tmp3 = tmp2(0)
/*------------------- Tuple ---------------------*/

def getTuple() = {
   createAB
}

def createAB = (createA, createB)

def createA = "A"

def createB = "B"

val tupleValue = getTuple()

tupleValue._1

/*----------------- filter tuple ---------*/
val nums = ("?" , "haha")

/*------------- filter only words --------*/
 val string = "aaa bbb ??? dddd eeee"
string.split(" ").filter(_.matches("^[a-zA-Z0-9 ]+$"))

/*---------- different reduce -------*/
val seq = Seq(1,2,3,4)
seq.reduce(_-_)
seq.reduceLeft(_-_)
seq.reduceRight(_-_)

seq.fold(1)(_-_)
LabeledPoint


/*----------- hashingTF ---------*/
val conf = new SparkConf().setMaster("local").setAppName("My App")
val sc = new SparkContext(conf)
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
val sentenceData = sqlContext.createDataFrame(Seq(
  (0, "Hi I heard about Spark"),
  (0, "I wish Java could use case classes"),
  (1, "Logistic regression models are neat")
)).toDF("label", "sentence")

val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
val wordsData = tokenizer.transform(sentenceData)
val hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(20)
val featurizedData = hashingTF.transform(wordsData)
featurizedData.foreach(println)

