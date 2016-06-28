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