package com.linebattle.model.objects

import org.scalatest._
import com.linebattle.util.Resetable

class ProjectileTest extends FunSpec with Matchers {

	describe( "A projectile" ) {
		
		it( "should initialize its' parameter with default values." ) {
			
			val projectile = new Projectile
			
			projectile.id >= 0 should be ( true )
			projectile.health.value should be ( 1 )
			projectile.health.maximum should be ( 1 )
			projectile.health.isValid should be ( true )
			projectile.orientation should be ( North )
			projectile.name should be ( "" )
			projectile.damage should be ( 1 )
			projectile.shootingRange.value should be ( 1 )
			projectile.shootingRange.maximum should be ( 1 )
			projectile.shootingRange.valid should be ( true )
			projectile.damageRadius should be ( 1 )
			projectile.isValid should be ( false )
		}
		
		it( "should always return the neutral player id." ) {
			
			val projectile = new Projectile
			projectile.playerId should be ( -1 )
		}
		
		it( "should return a new projectile, if it takes damage." ) {
			
			val projectile1 = new Projectile
			val projectile2 = projectile1.takeDamage( 1 )
			
			projectile1.health.value should be ( 1 )
			projectile2.health.value should be ( 0 )
		}
		
		it( "should be dead, if the health is <= 0." ) {
			
			val projectile = new Projectile( health = 0 )
			projectile.isDead should be ( true )
		}
		
		it( "should reduce the shooting range by 1, if there are enough shooting range points." ) {
			
			val projectile1 = new Projectile
			val projectile2 = projectile1.hasMoved
			
			projectile1.shootingRange.value should be ( 1 )
			projectile2.shootingRange.value should be ( 0 )
		}
		
		it( "should throw an Exception, if it has moved and there are not enough shooting range points." ) {
			
			a [Exception] should be thrownBy {
				val projectile = new Projectile( shootingRange = 0 )
				projectile.canMove should be ( false )
				projectile.hasMoved
			}
		}
		
		it( "should be able to reset the shooting range points." ) {
			
			val projectile1 = new Projectile( shootingRange = new Resetable( 1, 0, true ) )
			val projectile2 = projectile1.resetShootingRange
			
			projectile1.shootingRange.value should be ( 0 )
			projectile2.shootingRange.value should be ( 1 )
		}
		
		it( "should be invalid, if the id is < 0." ) {
			
			val projectile = new Projectile
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the health value is <= 0." ) {
			
			val projectile = new Projectile( health = -1 )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the health maximum is <= 0." ) {
			
			val projectile = new Projectile( health = new Resetable( 0, 1, true ) )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the health is invalid." ) {
			
			val projectile = new Projectile( health = new Resetable( 1, 1, false ) )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the name string is empty." ) {
			
			val projectile = new Projectile( name = "" )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the display damage is <= 0." ) {
			
			val projectile = new Projectile( name = "1", damage = 0 )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the shooting range value is < 0." ) {
			
			val projectile = new Projectile( name = "1", shootingRange = new Resetable( 1, -1, true ) )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the shooting range maximum is <= 0." ) {
			
			val projectile = new Projectile( name = "1", shootingRange = new Resetable( 0, 0, true ) )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the shooting range is invalid." ) {
			
			val projectile = new Projectile( name = "1",shootingRange = new Resetable( 1, 1, false ) )
			projectile.isValid should be ( false )
		}
		
		it( "should be invalid, if the damage radius is < 0." ) {
			
			val projectile = new Projectile( name = "1", shootingRange = 1, damageRadius = -1 )
			projectile.isValid should be ( false )
		}
		
		it( "should be able to construct a valid projectile." ) {
			
			val projectile = new Projectile( 0, 1, North, "1", 1, 1, 1 )
			projectile.isValid should be ( true )
		}
	}
}