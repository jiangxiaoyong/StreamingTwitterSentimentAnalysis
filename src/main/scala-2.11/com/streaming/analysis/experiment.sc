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

val slidingString = "aaa bbb, ccc ddd eee fff. ggg hhhh iiii" split("\\W+")
val s = slidingString.sliding(2) toList

val as = Array("aaa", "bbb", "ccc","ddd")
as.sliding(2) foreach{ case Array(a1, a2) => println(a1, a2)}

var ss = "my whole body feels itchy and like its on fire " split(" ")
ss.filter(x => x.matches("^[a-zA-Z0-9.]+$")).fold("")((a,b) => a + " " + b).trim

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


/*------------- filter only words --------*/
 val string = "aaa it's ??? dddd. eeee..."
string.split(" ").filter(_.matches("^[a-zA-Z0-9.]+")) mkString(" ") split("\\W+") mkString(" ")

/*---------- different reduce -------*/
val seq = Seq(1,2,3,4)
seq.reduce(_-_)
seq.reduceLeft(_-_)
seq.reduceRight(_-_)

seq.fold(1)(_-_)
LabeledPoint

/*------------ list of tuple -----------------*/
val list1 = List(("c1","m1"),  ("c2", "m2"))
("c1","c2") :: list1

val list2 = List(("c3", "m3"))

list1:::list2

val tup = Option(List("1", "2"))
tup.getOrElse(("0","0"))

val seq1 = Seq((1,2),(3,4))
seq1.map(x=> x._1 + 1)

2.8 toString




