package com.linebattle.model.objects

import com.linebattle.util.Resetable

trait GameObject {
  
  def health : Resetable
  def playerId : Int
  def name : String
  def id : Int
  def orientation : Orientation
  def isValid : Boolean
  def takeDamage( damage : Int ) : GameObject
  def isDead : Boolean = health <= 0 && health.valid == true
}