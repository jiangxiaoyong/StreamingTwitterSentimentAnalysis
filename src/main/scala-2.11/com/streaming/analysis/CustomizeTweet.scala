package com.streaming.analysis

object CustomizeTweet extends Serializable{
  def apply(status: twitter4j.Status) = createCountryTextTuple(status)

  def toOption(value: String): Option[String] = {
    if (value == null) None else Some(value)
  }

  def getTweetValue(value: Option[String]) = value match {
    case Some(s) => s
    case None => "?"
  }

  def createCountryTextTuple(status: twitter4j.Status) = (getTweetValue(toOption(status.getUser.getLocation)), getTweetValue(toOption(status.getText)))

}
