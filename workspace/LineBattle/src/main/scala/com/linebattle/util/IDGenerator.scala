package com.linebattle.util

object IDGenerator {
  
  private[this] var lastID : Int = -1
  
  def generateID() : Int = { this.synchronized{
	    lastID += 1
	    lastID
	  }
  }
  
  def reset() = lastID = -1

}