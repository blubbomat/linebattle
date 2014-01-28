package com.linebattle.model.objects

import com.linebattle.util.Resetable
import com.linebattle.util.IDGenerator

case class Tower( id : Int = IDGenerator.generateID,
				  name : String = "",
				  orientation : Orientation = North,
				  income: Int = 1,
				  lineId: Int = -1 ) extends IncomeGameObject {
	
	def health = false
	def playerId = -1
	def takeDamage( damage : Int ) = this
	
	def isValid : Boolean = {
		
		if( id >= 0 &&
			name.length > 0 &&
			income >= 0 &&
			lineId >= 0 ) {
			
			true
		}
		
		else {
			false
		}
		
	}
}