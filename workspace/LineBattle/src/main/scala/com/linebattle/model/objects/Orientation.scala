package com.linebattle.model.objects

trait Orientation {
	def name : String
	def toDegree : Int
}

case object North extends Orientation{
  val name = "North"
  
  def toDegree = 0
}

case object South extends Orientation{
  val name = "South"
  	
  def toDegree = 180
}

case object West extends Orientation{
  val name = "West"
  
  def toDegree = 270
}

case object East extends Orientation{
  val name = "East"
  	
  def toDegree = 90
}