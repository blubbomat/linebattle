package com.linebattle.model.objects

import org.scalatest._
import com.linebattle.util._

class PeonTest extends FunSpec with Matchers {
	
	describe( "A peon" ) {
		
		it( "should initialize its' parameter with default values." ) {
			val peon = Peon( 1 )
			
			peon.playerId should be ( 1 )
			peon.id >= 0 should be ( true )
			peon.health.value should be ( 1 )
			peon.health.valid should be ( true )
			peon.orientation should be ( North )
			peon.name should be ( "" )
			peon.actionPoints.value should be ( 1 )
			peon.actionPoints.valid should be ( true )
			peon.movingCosts should be ( 1 )
			peon.shootingCosts should be ( 1 )
			peon.buildCosts should be ( 1 )
			peon.projectile should be ( "" )
		}		
		
		it( "should be not valid, if somebody uses the default values." ) {
			
			val peon = Peon( 1 )
			peon.isValid should be ( false )
		}
		
		it( "should return a new peon with lower health, if takeDamage is called." ) {
			
			val peon1 = Peon( 0, health = 10 )
			val peon2 = peon1.takeDamage( 5 )
			
			peon1.health.value should be ( 10 )
			peon2.health.value should be ( 5 )
		}
		
		it( "should be dead, if the health is <= 0." ) {
			
			val peon = Peon( 0, health = 0 )
			peon.isDead should be ( true )
		}
		
		it( "should return a new peon, if the peon change its' orientation." ) {
			
			val peon1 = Peon( 0 )
			
			peon1.canMove should be ( true )
			
			val peon2 = peon1.changeOrientation( East )
			
			peon1.orientation should be ( North )
			peon1.actionPoints.value should be ( 1 )
			peon2.orientation should be ( East )
			peon2.actionPoints.value should be ( 0 )
		}
		
		it( "should not be able to change the orientation, if there are not enough action points." ) {
			
			val peon1 = Peon( 0, movingCosts = 2 )
			
			peon1.canMove should be ( false )
			
			val peon2 = peon1.changeOrientation( East )
			
			peon2.actionPoints.value should be ( 1 )
			peon2.orientation should be ( North )
		}
		
		it( "should decrease the action points, if the peon has moved." ) {
			
			val peon1 = Peon( 0 )
			val peon2 = peon1.hasMoved
			
			peon1.actionPoints.value should be ( 1 )
			peon2.actionPoints.value should be ( 0 )
		}
		
		it( "should throw an Exception, if there are not enough action points to move!" ) {
			
			a [Exception] should be thrownBy {
				val peon = Peon( 0, actionPoints = 0 )
				peon.canMove should be ( false )
				peon.hasMoved
			}
		}
		
		it( "should return a new peon, if the peon has shooted." ) {
			
			val peon1 = Peon( 0 )
			
			peon1.canShoot should be ( true )
			
			val peon2 = peon1.hasShooted
			
			peon1.actionPoints.value should be ( 1 )
			peon2.actionPoints.value should be ( 0 )
		}
		
		it( "should throw an Exception, if there are not enough action points." ) {
			
			a [Exception] should be thrownBy {
				val peon = Peon( 0, actionPoints = 0 )
				peon.canShoot should be ( false )
				peon.hasShooted
			}
		}
		
		it( "should be able to reset the action points." ) {
			
			val peon1 = Peon( 0, actionPoints = new Resetable( 1, 0, true ) )
			val peon2 = peon1.resetActionPoints
			
			peon1.actionPoints.value should be ( 0 )
			peon2.actionPoints.value should be ( 1 )
		}
		
		it( "should be invalid, if the player id is negative." ) {
			
			val peon = Peon( -1 )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the id is negative." ) {
			
			val peon = Peon( 0, -1 )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the health is <= 0." ) {
			
			val peon = Peon( 0, health = 0 )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the health is invalid." ) {
			
			val peon = Peon( 0, health = Resetable( 1, 1, false ) )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the name is an empty string." ) {
			
			val peon = Peon( 0, name = "" )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the displayId is < 0." ) {
			
			val peon = Peon( 0, name = "1")
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the actionPoints value is < 0" ) {
			
			val peon = Peon( 0, name = "1", actionPoints = -1 )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the actionPoints are invalid." ) {
			
			val peon = Peon( 0, name = "1", actionPoints = new Resetable( 1, 1, false ) )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the movingCosts are >= 0." ) {
			
			val peon = Peon( 0, name = "1", movingCosts = 0 )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the shootingCosts are >= 0." ) {
			
			val peon = Peon( 0, name = "1", shootingCosts = 0 )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the buildCosts are >= 0." ) {
			
			val peon = Peon( 0, name = "1", buildCosts = 0 )
			peon.isValid should be ( false )
		}
		
		it( "should be invalid, if the projectile string is empty." ) {
			
			val peon = Peon( 0, name = "1", projectile = "" )
			peon.isValid should be ( false )
		}
		
		it( "should be able to construct a valid peon." ) {
			
			val peon = Peon( 0, 0, 1, North, "1", 1, 1, 1, 1, "bang" )
			peon.isValid should be ( true )
		}
	}
}