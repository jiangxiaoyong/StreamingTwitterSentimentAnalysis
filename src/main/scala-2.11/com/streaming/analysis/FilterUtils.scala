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

}
