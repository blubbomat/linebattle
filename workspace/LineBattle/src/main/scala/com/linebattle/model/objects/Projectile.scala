package com.linebattle.model.objects

import com.linebattle.util.Resetable
import com.linebattle.util.IDGenerator

case class Projectile( id : Int = IDGenerator.generateID,
					   health : Resetable = 1,
					   orientation: Orientation = North,	
					   name : String = "",
					   damage : Int = 1,
					   shootingRange : Resetable = 1,
					   damageRadius : Int = 1 ) extends GameObject {
  
	def playerId = -1
	
	def takeDamage( damage : Int ) : Projectile = copy( health = health - damage )
	
	def canMove : Boolean = shootingRange >= 1
	
	def hasMoved : Projectile = {
		
		if( canMove ) {
			copy( shootingRange = shootingRange - 1 )
		}
		
		else throw new Exception( "Not enough shooting range points!" )
	}
	
	def resetShootingRange : Projectile = copy( shootingRange = shootingRange.reset )
	
	def isValid : Boolean = {
		
		if( id >= 0 &&
			health.value >= 0 &&
			health.maximum >= 1 &&
			health.isValid &&
			name.length > 0 &&
			damage >= 1 &&
			shootingRange.value >= 0 &&
			shootingRange.maximum >= 1 &&
			shootingRange.valid == true &&
			damageRadius >= 0 ) {
			
			true
		}
		
		else {
			false
		}
	}
}