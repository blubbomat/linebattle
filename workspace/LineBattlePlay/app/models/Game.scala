package models

import com.linebattle.controller.GameController
import com.linebattle.model.objects._

case class Game( controller : GameController )

object Game {
	
	implicit def convert( game : GameController ) : Game = new Game( game )
	
	def getPlayerId( game : Game ) : Int = {
		val playerId = game.controller.gameField.activePlayer + 1
		playerId
	}
	
	def getPlayerRessources( game : Game ) : Int = {
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		player.ressources
	}
	
	def getPlayerIncome( game : Game ) : Int = {
		game.controller.gameField.getActivePlayerIncome
	}
	
	def getPlayerPeons( game : Game ) : Int = {
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		player.peons.length
	}
	
	def getPlayerBaseHealth( game : Game ) : String = {
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		player.base.health.value + "/" + player.base.health.maximum
	}
	
	def isPeonSelected( game : Game ) : Boolean = {
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		player.isPeonSelected
	}
	
	def getPeonName( game : Game ) : String = {
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		
		if( !player.isPeonSelected ) {
			"N/A"
		}
		
		else {
			val peon = player.selectedPeon
			peon.name
		}
	}
	
	def getPeonActionPoints( game : Game ) : String = {
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		
		if( !player.isPeonSelected ) {
			"N/A"
		}
		
		else {
			val peon = player.selectedPeon
			peon.actionPoints.value + " / " + peon.actionPoints.maximum
		}
	}
	
	def getPeonHealth( game : Game ) : String = {
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		
		if( !player.isPeonSelected ) {
			"N/A"
		}
		
		else {
			val peon = player.selectedPeon
			peon.health.value + " / " + peon.health.maximum
		}
	}
	
	def getPeonOrientation( game : Game ) : String = {
		
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		
		if( !player.isPeonSelected ) {
			"N/A"
		}
		
		else {
			val peon = player.selectedPeon
			peon.orientation.toString 
		}
	}
	
	def getPeonMovingCosts( game : Game ) : String = {
		
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		
		if( !player.isPeonSelected ) {
			"N/A"
		}
		
		else {
			val peon = player.selectedPeon
			peon.movingCosts.toString
		}
	}
	
	def getPeonShootingCosts( game : Game ) : String = {
		
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		
		if( !player.isPeonSelected ) {
			"N/A"
		}
		
		else {
			val peon = player.selectedPeon
			peon.shootingCosts.toString
		}
	}
	
	def getPeonShootingRange( game : Game ) : Int = {
		
		val playerId = game.controller.gameField.activePlayer
		val player = game.controller.gameField.playerList( playerId )
		
		if( !player.isPeonSelected ) {
			0
		}
		
		else {
			val peon = player.selectedPeon
			val projectile = game.controller.mapLoader.gameObjectFactory.createProjectileFromFile( North, peon.projectile )
			projectile.shootingRange.value
		}
	}
	
	def getFieldWidth( game : Game ) = game.controller.gameField.gameGrid.width
	
	def getFieldHeight( game : Game ) = game.controller.gameField.gameGrid.height
	
	def isFieldEmpty( game : Game, row : Int, col : Int ) : Boolean = {
		val gameGrid = game.controller.gameField.gameGrid
		!gameGrid.hasObjectAt( col, row )
	}
	
	def getName( game : Game, row : Int, col : Int ) : String = {
		
		val gameGrid = game.controller.gameField.gameGrid
		val peon = game.controller.gameField.gameGrid.at( col, row )
		peon.name
	}
	
	def isTank( game : Game, row : Int, col : Int ) : Boolean = {
		!Game.isFieldEmpty( game, row, col ) && 
		Game.getName( game, row, col ) == "tank"
	}
	
	def isLauncher( game : Game, row : Int, col : Int ) : Boolean = {
		!Game.isFieldEmpty( game, row, col ) && 
		Game.getName( game, row, col ) == "launcher"
	}
	
	def isInfantry( game : Game, row : Int, col : Int ) : Boolean = {
		!Game.isFieldEmpty( game, row, col ) && 
		Game.getName( game, row, col ) == "infantry"
	}
	
	def isBase( game : Game, row : Int, col : Int ) : Boolean = {
		!Game.isFieldEmpty( game, row, col ) && 
		Game.getName( game, row, col ) == "base"
	}
	
	def isTree( game : Game, row : Int, col : Int ) : Boolean = {
		!Game.isFieldEmpty( game, row, col ) && 
		Game.getName( game, row, col ) == "tree"
	}
	
	def isTower( game : Game, row : Int, col : Int ) : Boolean = {
		!Game.isFieldEmpty( game, row, col ) && 
		Game.getName( game, row, col ) == "tower"
	}
	
	def getObjectRotation( game : Game, row : Int, col : Int ) : String = {
		
		val gameGrid = game.controller.gameField.gameGrid
		val peon = game.controller.gameField.gameGrid.at( col, row )
		peon.orientation match {
			case North => "0deg"
			case East => "90deg"
			case South => "180deg"
			case West => "270deg"
		}
	}
	
	def getFieldColor( game : Game, row : Int, col : Int ) : String = {
		
		val gameField = game.controller.gameField
		val gameGrid = gameField.gameGrid
		val playerId = gameField.activePlayer
		val player = gameField.playerList( playerId )
		
		var color = ""
		
		if( !gameGrid.hasObjectAt( col, row ) ) {
			color = "#AA6600"
		}
		
		else if( player.isPeonSelected ) {
			val selectedPeon = player.selectedPeon
			val position = gameGrid.getPosition( selectedPeon.id )
			
			if( col == position.x && row == position.y ) {
				color = "#00FF00"
			}
		}
		
		if( color == "" ) {
			val obj = gameGrid.at( col, row )
			
			if( obj.name == "tower" ) {
				
				if( gameField.isTowerConquered( obj, 0 ) ) color = "#FF0000"
				else if( gameField.isTowerConquered( obj, 1 ) ) color = "#00CCFF"
				else if( gameField.isTowerConquered( obj, 2 ) ) color = "#FFFF00"
				else if( gameField.isTowerConquered( obj, 3 ) ) color = "#CC00FF"
				else color = "#AA6600"

			}
			
			else {
				obj.playerId match {
				case 0 => color = "#FF0000"
				case 1 => color = "#00CCFF"
				case 2 => color = "#FFFF00"
				case 3 => color = "#CC00FF"
				case _ => color = "#AA6600"
				}
			}
		}
		
		color
	}
}