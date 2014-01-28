package com.linebattle.model.objects

import com.linebattle.util.Resetable
import com.linebattle.util.IDGenerator

case class Peon( playerId : Int,
				 id : Int = IDGenerator.generateID,
				 health : Resetable = 1,
				 orientation : Orientation = North,
				 name : String = "",
				 actionPoints : Resetable = 1,
				 movingCosts : Int = 1,
				 shootingCosts : Int = 1,
				 buildCosts : Int = 1,
				 projectile : String = "" ) extends GameObject {
		
	def takeDamage( damage : Int ) : Peon = copy( health = health - damage )
	
	def changeOrientation( newOrientation : Orientation ) : Peon = {
		
		if( canMove ) {
			copy( actionPoints = actionPoints - movingCosts, orientation = newOrientation )
		}
		else this
	}
	
	def canMove : Boolean = actionPoints >= movingCosts
	
	def hasMoved : Peon = {
		
		if( canMove ) {
			copy( actionPoints = actionPoints - movingCosts )
		}
		else throw new Exception( "Peon has not enough action points!" )
	}
	
	def canShoot : Boolean = actionPoints >= shootingCosts
	
	def hasShooted : Peon = {
		if( canShoot ) {
			copy( actionPoints = actionPoints - shootingCosts )
		}
		else throw new Exception( "Peon has not enough action points!" )
	}
	
	def resetActionPoints() : Peon = {
		copy( actionPoints = actionPoints.reset )
	}
	
	def isValid() : Boolean = {
		
		if( id >= 0 &&
			playerId >= 0 &&
			health >= 1 && 
			health.isValid &&
			name.length > 0 &&
			actionPoints >= 1 && 
			actionPoints.isValid &&
			movingCosts >= 1 &&
			shootingCosts >= 1 &&
			buildCosts >= 1 &&
			projectile.length > 0 ) {
			
			true
		}
		
		else {
			false
		}
	}
}