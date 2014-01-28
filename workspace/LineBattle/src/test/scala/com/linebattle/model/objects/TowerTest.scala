package com.linebattle.model.objects

import org.scalatest._

class TowerTest extends FunSpec with Matchers {
	
	describe( "A tower" ) {

		it( "should initialize its' parameter with default values." ) {
			
			val tower = new Tower
			
			tower.id >= 0 should be ( true )
			tower.name should be ( "" )
			tower.orientation should be ( North )
			tower.income should be ( 1 )
			tower.lineId should be ( -1 )
			tower.isValid should be ( false )
		}
		
		it( "should has no health" ) {
			
			val tower = new Tower
			tower.health.isValid should be ( false )
		}
		
		it( "should has the neutral player id." ) {
			
			val tower = new Tower
			tower.playerId should be ( -1 )
		}
		
		it( "should not take damage." ) {
			
			val tower1 = new Tower
			val tower2 = tower1.takeDamage( 100 )
			
			tower1.equals( tower2 ) should equal( true )
		}
		
		it( "should be invalid, if the id is < 0." ) {
			
			val tower = new Tower( id = -1 )
			tower.isValid should be ( false )
		}
		
		it( "should be invalid, if the name is an empty string." ) {
			
			val tower = new Tower( name = "" )
			tower.isValid should be ( false )
		}
		
		it( "should be invalid, if the displayId is < 0." ) {
			
			val tower = new Tower( name = "1")
			tower.isValid should be ( false )
		}
		
		it( "should be invalid, if the income is < 0." ) {
			
			val tower = new Tower( name = "1", income = -1 )
			tower.isValid should be ( false )
		}
		
		it( "should be invalid, if the lineId is < 0." ) {
			
			val tower = new Tower( name = "1", income = 1, lineId = -1 )
			tower.isValid should be ( false )
		}
		
		it( "should be able to construct a valid tower." ) {
			
			val tower = new Tower( 0, "1", North, 1, 0 )
			tower.isValid should be ( true )
		}
		
		it( "should not equal another tower." ) {
			
			val tower1 = new Tower
			val tower2 = new Tower
			
			tower1.equals( tower2 ) should be ( false )
		}
	}
}