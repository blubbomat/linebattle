package com.linebattle.model.objects

import org.scalatest.Matchers
import org.scalatest.FunSpec
import com.linebattle.util.Resetable

class StaticGameObjectTest extends FunSpec with Matchers {
	
	describe( "A StaticGameObject" ) {
		
		it( "should initialize its' parameter with default values." ) {
			
			val static = new StaticGameObject
			
			static.id >= 0 should be ( true )
			static.health.valid should be ( false )
			static.orientation should be ( North )
			static.name should be ( "" )
			static.isValid should be ( false )
		}
		
		it( "should always return the neutral player id." ) {
			val static = new StaticGameObject
			static.playerId should be ( -1 )
		}
		
		it( "should reduce health, if it can take damage." ) {
			val static1 = new StaticGameObject( health = 1 )
			val static2 = static1.takeDamage( 1 )
			
			static1.health.value should be ( 1 )
			static2.health.value should be ( 0 )
		}
		
		it( "should not reduce health, if the health is invalid." ) {
			val static1 = new StaticGameObject( health = false )
			val static2 = static1.takeDamage( 1 )
			
			static1.health.value should be ( 0 )
			static2.health.value should be ( 0 )
			static1.equals( static2 ) should be ( true )
		}
		
		it( "should be dead, if the health is 0 and the health is valid." ) {
			val static1 = new StaticGameObject( health = 1 )
			val static2 = new StaticGameObject( health = false )
			val static3 = new StaticGameObject( health = new Resetable( 1, 0, true ) )
			
			static1.isDead should be ( false )
			static2.isDead should be ( false )
			static3.isDead should be ( true )
		}
		
		it( "should be invalid, if the id is < 0." ) {
			val static = new StaticGameObject( -1 )
			static.isValid should be ( false )
		}
		
		it( "should be invalid, if the health is valid and maximum <= 0." ) {
			val static = new StaticGameObject( name = "1", health = 0 )
			static.isValid should be ( false )
		}
		
		it( "should be invalid, if the name is an empty string." ) {
			val static = new StaticGameObject( name = "" )
			static.isValid should be ( false )
		}
		
		it( "should be able to construct a valid object." ) {
			val static = new StaticGameObject( 0, false, North, "1")
			static.isValid should be ( true )
		}
	}
}