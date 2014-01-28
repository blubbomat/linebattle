package com.linebattle.model.objects

import org.scalatest._
import com.linebattle.util.Resetable
import com.linebattle.util.IDGenerator

class BaseTest extends FunSpec with Matchers {
	
	describe( "A base" ) {
		
		val factory = new GameObjectFactory
		
		it( "should initialize its' parameter with default values." ) {
			
			val base = Base( 1 )
			
			base.id >= 0 should be( true )
			base.playerId should be ( 1 )
			base.health.value should be ( 1 )
			base.health.maximum should be ( 1 )
			base.health.valid should be ( true )
			base.orientation should be ( North )
			base.name should be ( "" )
			base.income should be ( 1 )
		}
		
		it( "should be not valid, if somebody uses the default values." ) {
			
			val base = Base( 1 )
			base.isValid should be ( false )
		}
		
		it( "should return a new base with lower health if it takes damage.") {
			
			val base1 = Base( 0, health = 10 )
			val base2 = base1.takeDamage( 5 )
			
			base1.health.value should be ( 10 )
			base2.health.value should be ( 5 )
		}
		
		it( "should be dead, if the health is <= 0." ) {
			
			val base = Base( 0, health = 0 )
			base.isDead should be ( true )
		}
		
		it( "should be invalid, if the player id is negative." ) {
			
			val base = Base( -1 )
			base.isValid should be ( false )
		}
		
		it( "should be invalid, if the id is negative." ) {
			
			val base = Base( 0, -1 )
			base.isValid should be ( false )
		}
		
		it( "should be invalid, if the health is <= 0." ) {
			
			val base = Base( 0, health = 0 )
			base.isValid should be ( false )
		}
		
		it( "should be invalid, if the health is invalid." ) {
			
			val base = Base( 0, health = Resetable( 1, 1, false ) )
			base.isValid should be ( false )
		}
		
		it( "should be invalid, if the name is an empty string." ) {
			
			val base = Base( 0, name = "" )
			base.isValid should be ( false )
		}
		
		it( "should be invalid, if the income is < 0." ) {
			
			val base = Base( 0, name = "1", income = -1 )
			base.isValid should be ( false )
		}

		it( "should be able to create a valid base." ) {			
			val base = Base( 0, 0, 1, North, "1", 1 )
			base.isValid should be ( true )
		}
	}
}