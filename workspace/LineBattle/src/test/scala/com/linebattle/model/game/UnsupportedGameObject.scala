package com.linebattle.model.game

import com.linebattle.model.objects.GameObject
import com.linebattle.util.Resetable
import com.linebattle.model.objects.Orientation
import com.linebattle.model.objects.North

case class UnsupportedGameObject ( id : Int = 0,
							 health : Resetable = false,
							 orientation : Orientation = North,
							 name : String = "" ) extends GameObject {
  
	def playerId = -1
	def takeDamage( damage : Int ) : UnsupportedGameObject = copy( health = health - damage )

	def isValid : Boolean = {
		true
	}
}