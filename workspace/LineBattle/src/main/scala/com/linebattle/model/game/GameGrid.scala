package com.linebattle.model.game

import scala.collection.immutable.List
import scala.collection.immutable.Map
import com.linebattle.model.objects._
import scala.collection.immutable.HashMap

case class GameGrid( grid : List[ List[ GameObject ] ], val map : Map[Int,Position] = new HashMap ) {
	
	def width : Int = {
		if( grid.length > 0 ) {
			grid( 0 ).length
		}
		else 0
	}
	
	def height : Int = grid.length
	
	def isValid : Boolean = {
		val wrongRows = grid filter ( row => row.length != width )
		wrongRows.length == 0
	}
	
	def isInside( position : Position ) : Boolean = isInside( position.x, position.y )
	
	def isInside( x : Int, y : Int ) : Boolean = {
		if( x < 0 || x >= width || y < 0 || y >= height ) false
		else true
	}
	
	def at( x : Int, y : Int ) : GameObject = {
		validateCoordinates( x, y )
		grid( y )( x )
	}
	
	def hasObjectAt( position : Position ) : Boolean = hasObjectAt( position.x, position.y )
	
	def hasObjectAt( x : Int, y : Int ) : Boolean = {
		validateCoordinates( x, y )
		grid( y )( x ) != null
	}
	
	def set( position : Position, gameObject : GameObject ) : GameGrid = set( position.x, position.y, gameObject )
	
	def set( x : Int, y : Int, gameObject : GameObject ) : GameGrid = {
		
		if( gameObject != null ) {
			
			validateCoordinates( x, y )
		
			val cleanMap = removeMapPosition( x, y )
			val newGrid = updateGrid( x, y, gameObject )
			val newMap = cleanMap + ( gameObject.id -> new Position( x, y ) )
			copy( grid = newGrid, map = newMap )
		}
		else this
	}
	
	def remove(gameObject : GameObject ) : GameGrid = {
		remove(getPosition(gameObject.id))
	}
	
	def remove( position : Position ) : GameGrid = remove( position.x, position.y )
	
	def remove( x : Int, y : Int ) : GameGrid = {
		
		validateCoordinates( x, y )
		
		if( hasObjectAt( x, y ) ) {
			val gameObject = at ( x, y )
			val newMap = map - gameObject.id
			val newGrid = updateGrid( x, y, null )
			copy( grid = newGrid, map = newMap )
		}
		else this		
	}
	
	private[this] def validateCoordinates( x : Int, y : Int ) : Unit = {
		
		if( !isInside( x, y ) ) {
			throw new IllegalArgumentException( "x or y index is out of bounds!" )
		}
	}
	
	private[this] def removeMapPosition( x : Int, y : Int ) : Map[Int,Position] = {
		val oldObject = grid( y )( x )
		if( oldObject != null ) {
			map - oldObject.id
		}
		else map
	}
	
	private[this] def updateGrid( x : Int, y : Int, gameObject : GameObject ) : List[List[GameObject]] = {
		val newRow = grid( y ) updated( x, gameObject )
		grid updated( y, newRow )
	}
	
	def neighborPosition( position : Position, orientation : Orientation ) : Position = {
		orientation match {
			case North => new Position( position.x, position.y - 1 )
			case West => new Position( position.x - 1, position.y )
			case South => new Position( position.x, position.y + 1 )
			case East => new Position( position.x + 1, position.y )
		}
	}
	
	def getPosition( id : Int ) : Position = {
		val result = map get ( id )
		if( result != None ) {
			result get
		}
		else throw new Exception( "Object not found in GameGrid!" )
	}
	
	def isPositionFree( position : Position ) : Boolean = isPositionFree( position.x, position.y )
	
	def isPositionFree( x : Int, y : Int ) : Boolean = {
		validateCoordinates( x, y )
		grid( y )( x ) == null
	}
}