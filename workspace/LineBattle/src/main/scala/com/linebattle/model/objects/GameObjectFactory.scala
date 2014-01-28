package com.linebattle.model.objects

import com.linebattle.util.IDGenerator
import com.linebattle.util.Resetable
import scala.io.Source

class GameObjectFactory( val objectFolder : String = "gamefiles/objects/" ) {
	
	def createBaseFromFile( playerId : Int, orientation : Orientation, factoryId : String ) : Base = {
		
		var newBase = Base( orientation = orientation, playerId = playerId, name = factoryId )
		
		val filePath = objectFolder + "/bases/" + factoryId + ".base"
		for( line <- Source.fromFile( filePath ).getLines ) {
		
			val valuePair = line.split( " : " )
			newBase = setBaseAttribute( newBase, valuePair )
		}
		 
		if( newBase isValid ) {
			newBase
		}
		
		else {
			throw new Exception( "File " + filePath + " is invalid!" )
		}
	}
	
	private[this] def setBaseAttribute( base : Base, valuePair : Array[String] ) : Base = {
		
		if( valuePair.length == 2 ) {
			
			val key = valuePair( 0 )
			val value = valuePair( 1 )
			
			key match {
				case "income" => base.copy( income = value.toInt )
				case "health" => base.copy( health = value.toInt )
				case _ => base
			}
		}
		
		else {
			base
		}
	}
	
	def createTowerFromFile( orientation : Orientation, lineId : Int, factoryId : String ) : Tower = {
		
		var newTower = Tower( orientation = orientation, lineId = lineId, name = factoryId )
		
		val filePath = objectFolder + "/bases/" + factoryId + ".tower"
		for( line <- Source.fromFile( objectFolder + "/towers/" + factoryId + ".tower" ).getLines ) {
		
			val valuePair = line.split( " : " )
			newTower = setTowerAttribute( newTower, valuePair )
		}
		
		if( newTower isValid ) {
			newTower
		}
		
		else {
			throw new Exception( "File " + filePath + " is invalid!" )
		}
	}
	
	private[this] def setTowerAttribute( tower : Tower, valuePair : Array[String] ) : Tower = {
		
		if( valuePair.length == 2 ) {
			
			val key = valuePair( 0 )
			val value = valuePair( 1 )
			
			key match {
				case "income" => tower.copy( income = value.toInt )
				case _ => tower
			}
		}
		
		else {
			tower
		}
	}
	
	def createStaticObjectFromFile( orientation : Orientation, factoryId : String ) : StaticGameObject = {
	  
		var newStaticObject = new StaticGameObject( health = false, orientation = orientation, name = factoryId )
	  
		val filePath = objectFolder + "/staticgameobjects/" + factoryId + ".static" 
		for( line <- Source.fromFile( filePath ).getLines ) {
		
			val valuePair = line.split( " : " )
			newStaticObject = setStaticObjectAttribute( newStaticObject, valuePair )
		}
		
		if(  newStaticObject isValid ) {
			newStaticObject
		}
		  
		else {
			throw new Exception( "File " + filePath + " is invalid!" )
		}
	}
	
	private[this] def setStaticObjectAttribute( staticObject : StaticGameObject, valuePair : Array[String] ) : StaticGameObject = {
		
		if( valuePair.length == 2 ) {
				
				val key = valuePair( 0 )
				val value = valuePair( 1 )
				
				key match {
					case "health" => staticObject.copy( health = value.toInt )
					case _ => staticObject
				}
			}
		
		else {
			staticObject
		}
	}
	
	def createPeonFromFile( playerId : Int, orientation : Orientation, factoryId:String ) : Peon = {
		
		var newPeon = new Peon( playerId = playerId, orientation = orientation, name = factoryId )
		
		val filePath = objectFolder + "/peons/" + factoryId + ".peon"
		for( line <- Source.fromFile( filePath ).getLines ) {
			
			val valuePair = line.split( " : " )
			newPeon = setPeonAttribute( newPeon, valuePair )
		}
		
		if( newPeon isValid ) {
			newPeon
		}
		  
		else {
			throw new Exception( "File " + filePath + " is invalid!" )
		}
	}
	
	private[this] def setPeonAttribute( peon : Peon, valuePair : Array[ String ] ) : Peon = {
		
		if( valuePair.length == 2 ) {
				
			val key = valuePair( 0 )
			val value = valuePair( 1 )
			
			key match {
				case "health" => peon.copy( health = value.toInt )
				case "actionPoints" => peon.copy( actionPoints = value.toInt )
				case "movingCosts" => peon.copy( movingCosts = value.toInt )
				case "shootingCosts" => peon.copy( shootingCosts = value.toInt )
				case "buildCosts" => peon.copy( buildCosts = value.toInt )
				case "projectile" => peon.copy( projectile = value )
				case _ => peon
			}
		}
		
		else {
			peon
		}
	}
	
	def createProjectileFromFile( orientation : Orientation, factoryId : String ) : Projectile = {
		
		var newProjectile = new Projectile( orientation = orientation, name = factoryId )
		
		val filePath = objectFolder + "/projectiles/" + factoryId + ".projectile"
		for( line <- Source.fromFile( filePath  ).getLines ) {
			
			val valuePair = line.split( " : " )
			newProjectile = setProjectileAttribute( newProjectile, valuePair )
		}
		
		if( newProjectile isValid ) {
			newProjectile
		}
		  
		else {
			throw new Exception( "File " + filePath + " is invalid!" )
		}
	}
	
	private[this] def setProjectileAttribute( projectile : Projectile, valuePair : Array[ String ] ) : Projectile = {
		
		if( valuePair.length == 2 ) {
				
			val key = valuePair( 0 )
			val value = valuePair( 1 )
			
			key match {
				case "health" => projectile.copy( health = value.toInt )
				case "damage" => projectile.copy( damage = value.toInt )
				case "shootingRange" => projectile.copy( shootingRange = value.toInt )
				case "damageRadius" => projectile.copy( damageRadius = value.toInt )
				case _ => projectile
			}
		}
		
		else {
			projectile
		}
	}
}