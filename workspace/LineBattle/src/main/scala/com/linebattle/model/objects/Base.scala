package com.linebattle.model.objects

import com.linebattle.util.Resetable
import com.linebattle.util.IDGenerator

case class Base( playerId : Int,
				 id : Int = IDGenerator.generateID,
				 health : Resetable = 1,
				 orientation : Orientation = North,
				 name : String = "",
				 income: Int = 1 ) extends IncomeGameObject {
  
	def takeDamage( damage : Int ) : Base = copy( health = health - damage )
	
	def isValid : Boolean = {
		
		if( id >= 0 &&
			playerId >= 0 &&
			health >= 1 &&
			health.isValid == true &&
			name.length > 0 &&
			income >= 0 ) {
			
			true
		}
		
		else {
			false
		}
	}
}