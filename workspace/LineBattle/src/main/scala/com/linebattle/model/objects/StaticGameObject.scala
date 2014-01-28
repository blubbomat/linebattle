package com.linebattle.model.objects

import com.linebattle.util.Resetable
import com.linebattle.util.IDGenerator

case class StaticGameObject( id : Int = IDGenerator.generateID,
							 health : Resetable = false,
							 orientation : Orientation = North,
							 name : String = "" ) extends GameObject {
  
	def playerId = -1
	def takeDamage( damage : Int ) : StaticGameObject = copy( health = health - damage )

	def isValid : Boolean = {
		
		if( id >= 0 &&
			name.length > 0 &&
			( ( health.value >= 0 && health.maximum >= 1 )
			|| !health.isValid ) ) {
			
			true
		}
		
		else {
			false
		}
	}
}