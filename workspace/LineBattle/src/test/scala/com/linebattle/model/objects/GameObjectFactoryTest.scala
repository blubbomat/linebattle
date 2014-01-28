package com.linebattle.model.objects

import org.scalatest._
import com.linebattle.util.IDGenerator

class GameObjectFactoryTest extends FunSpec with Matchers {
	
	describe( "A GameObjectFactory" ) {
		
		val factory = new GameObjectFactory( "testfiles/objects" )
		
		it( "should create a base with predefined stats!" ) {
			
			val base = factory.createBaseFromFile( 0, North, "base" )
			
			base.playerId should be ( 0 )
			base.id >= 0 should be ( true )
			base.health.value should be ( 500 )
			base.health.maximum should be ( 500 )
			base.health.isValid should be ( true )
			base.orientation should be ( North )
			base.name should be ( "base" )
			base.income should be ( 20 )
		}
		
		it( "should throw an Exception, if you try to load a bad base file." ) {
			
			a [Exception] should be thrownBy {
				factory.createBaseFromFile( 0, North, "bad" )
			}
		}
		
		it( "should create a tower with predefined stats!" ) {
			
			val tower = factory.createTowerFromFile( North, 0, "tower" )
			
			tower.playerId should be ( -1 )
			tower.id >= 0 should be ( true )
			tower.health.isValid should be ( false )
			tower.orientation should be ( North )
			tower.name should be ( "tower" )
			tower.income should be ( 10 )
		}
		
		it( "should throw an Exception, if you try to load a bad tower file." ) {
			
			a [Exception] should be thrownBy {
				factory.createTowerFromFile( North, 0, "bad" )
			}
		}
		
		it( "should create a static object with predefined stats!" ) {
			
			val tree = factory.createStaticObjectFromFile( North, "tree" )
			
			tree.playerId should be ( -1 )
			tree.id >= 0 should be ( true )
			tree.health.value should be ( 100 )
			tree.health.maximum should be ( 100 )
			tree.health.isValid should be ( true )
			tree.health.isValid should be ( true )
			tree.orientation should be ( North )
			tree.name should be ( "tree" )
		}
		
		it( "should throw an Exception, if you try to load a bad static object file." ) {
			
			a [Exception] should be thrownBy {
				factory.createStaticObjectFromFile( North, "bad" )
			}
		}
		
		it( "should create a peon with predefined stats!" ) {

			val tank = factory.createPeonFromFile( 0, North, "tank" )
			
			tank.orientation should be ( North )
			tank.playerId should be ( 0 )
			tank.health.value should be ( 100 )
			tank.actionPoints.value should be ( 10 )
			tank.movingCosts should be ( 3 )
			tank.buildCosts should be ( 10 )			
			tank.projectile should be ( "projectileTank" )
		}
		
		it( "should throw an Exception, if you try to load a bad peon file." ) {
			
			a [Exception] should be thrownBy {
				factory.createPeonFromFile( 0, North, "bad" )
			}
		}
		
		it( "should create a projectile with predefined stats!"){
		  
			val projectile = factory.createProjectileFromFile( South, "projectileTank" )
		  
		  projectile.health.value should be ( 1 )
		  projectile.damage should be ( 20 )
		  projectile.shootingRange.value should be ( 3 )
		  projectile.damageRadius should be ( 1 )
		}
		
		it( "should throw an Exception, if you try to load a bad projectile file." ) {
			
			a [Exception] should be thrownBy {
				factory.createProjectileFromFile( North, "bad" )
			}
		}
	}
}