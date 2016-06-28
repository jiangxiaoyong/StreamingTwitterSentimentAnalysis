package com.streaming.analysis

object FilterUtils {
  def sanitiseLocation(s: String): Boolean= {
    filterUnknownLocation(s) && filterUnorganizedLocation(s)
  }

  def filterUnknownLocation(s: String): Boolean = {
    if (s != "?") true
    else false
  }

  def filterUnorganizedLocation(s: String): Boolean = {
    if (s.contains(",")) true
    else false
  }

  def filterOnlyWords(text: String): String = {
    text.split(" ").filter(_.matches("^[a-zA-Z0-9 ]+$")).fold("")((a,b) => a + " " + b).trim
  }

  def filterEmptyString(s: String): Boolean = {
    if ( s != "") true
    else false
  }

}
