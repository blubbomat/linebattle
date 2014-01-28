package com.linebattle.view

import com.linebattle.model.game.GameGrid
import com.linebattle.model.objects.GameObject
import com.linebattle.model.objects._
import com.linebattle.controller.GameController
import com.linebattle.util.Observer
import com.linebattle.util.Resetable
import scala.collection.mutable.StringBuilder
import com.linebattle.model.game.GameField
import scala.Console
import com.linebattle.controller.GameController
import com.linebattle.controller.GameController

object TextualUI extends App with Observer[GameController]{  
	
	var gameController:GameController = null
	
	
	override def main( args:Array[String] ) = {		
		TextualUI.initTextualUI(new GameController)
		TextualUI.readGameInput
	}
	
	def initTextualUI(newGameController : GameController){
		gameController = newGameController
		gameController.addObserver(this)
	}
	
	def drawGameGrid(gameGrid : GameGrid, selectedObject: Option[Peon]) = {
		  drawFields(gameGrid, selectedObject)	 
		  
		  drawLine(gameGrid width)
		  drawHorizontalIndex(gameGrid width)
	}
	
	private[this] def getStringNumberWithLength(number:Int, maxLength : Int): String = {
		val lengthBeforeNumber = maxLength - number.toString.length
		
		var stringBuilder = new StringBuilder
		
		for(i <- 0 until lengthBeforeNumber){
			stringBuilder ++= " "
		}
		
		stringBuilder ++= number.toString
		
		stringBuilder.toString
	}
	
	private[this] def drawHorizontalIndex(length : Int){
		var lineToDraw = new StringBuilder
		
		lineToDraw ++= "    "
		
		for(index <- 0 until length){
			lineToDraw ++= getStringNumberWithLength(index, 5) ++= " "
		}
		
		println(lineToDraw)
	}
	
	private[this] def drawBaseInfo(gameField: GameField) = {
		var lineToDraw = new StringBuilder
		
		for(playerId <- 0 until gameField.playerList.length){
			val base = gameField.playerList(playerId).base
			if(gameField.activePlayer == playerId){
				lineToDraw ++= "*"
			}
			lineToDraw ++= "["  + gameField.playerList(playerId).toString
			
			lineToDraw ++= "]   "
		}
		
		println(lineToDraw)
	}
	
	
	private[this] def drawLine(fieldWidthCount: Int) = {
		var stringBuilder = new StringBuilder
		stringBuilder ++= "    "

		for(i <- 1 to fieldWidthCount){
			stringBuilder ++= "+-----"
		}
			
		stringBuilder ++= "+"
		  
		println(stringBuilder)
	}
	
	/* 	Output per field:
	 *  |	playerID		*direction		*selected	|
	 * 	|	*direction		ViewID			*direction	|
	 * 	|					*direction					|
	 *       
	 *  * is optional
	 */
	private[this] def drawFields(gameGrid : GameGrid, selectedPeon: Option[Peon]) = {
	  var didDrawPeonInfo1 = false
	  var didDrawPeonInfo2 = false
	  	
	  var rowNumber = 0
	 
	  gameGrid.grid.foreach(rowList => {
	  	drawLine(gameGrid width)
	  	
	  	
	  	var line1 = new StringBuilder
		var line2 = new StringBuilder
		var line3 = new StringBuilder
		
		line1 ++= "    "
		line2 ++= getStringNumberWithLength(rowNumber, 3) ++= " "
	  	line3 ++= "    "
	  
		rowNumber = rowNumber + 1
	  	
	  	rowList.foreach(gameObject => {   	
	  		// |playerID	*direction	*selected|
		    line1 ++= "| "	
		    if(gameObject != null && gameObject.playerId >= 0)
		    	line1 ++= gameObject.playerId.toString	
		    else
		    	line1 ++= " "		    	
		   line1 ++= getDirectionChar(gameObject, North).toString
		    if(gameObject != null && selectedPeon != None && gameObject.id == selectedPeon.get.id)
		    	line1 ++= "* "
		    else
		    	line1 ++= "  "

		    // |*direction  ViewID  *direction|
		    
		    line2 ++= "| "
		    		
		    line2 ++= getDirectionChar(gameObject, West).toString
		    if(gameObject != null)
		    	line2 ++= getDisplayCharacter(gameObject).toString
		    else
		    	line2 ++= " "	    
		    line2 ++= getDirectionChar(gameObject, East).toString ++= " "
		    
		    // |		*direction(south)      |
		    line3 ++= "|  "
		    line3 ++= getDirectionChar(gameObject, South).toString
		    line3 ++= "  "		    
	  	})
	  	
	  	line1 ++= "|"
	  	line2 ++= "|"
	  	line3 ++= "|"

	    if(selectedPeon != None){
		  	if(!didDrawPeonInfo1){
		  		didDrawPeonInfo1 = true
		  		
		  		line1 ++= "  Peon name: " ++= selectedPeon.get.name
		  		line2 ++= "  Health: [" ++= selectedPeon.get.health.value.toString ++= "/" ++= selectedPeon.get.health.maximum.toString ++= "]"
		  	    line3 ++= "  ActionPoints: [" ++= selectedPeon.get.actionPoints.value.toString ++= "/" ++= selectedPeon.get.actionPoints.maximum.toString ++= "]"
		  	}
		  	else if(!didDrawPeonInfo2){
		  		didDrawPeonInfo2 = true
		  		
		  		line1 ++= "  Projectile: " ++= selectedPeon.get.projectile
		  		line2 ++= "  Movingcosts: " ++= selectedPeon.get.movingCosts.toString
		  		line3 ++= "  Shootingcosts: " ++= selectedPeon.get.shootingCosts.toString
		  	}
	    }
	  	
	  	println(line1)
	  	println(line2)
	  	println(line3)
	  })
	}
	
	private[this] def getColor(gameObject: GameObject):String = {
		
		gameObject.playerId match{
			case 0 =>
				Console.BLUE
			case 1 =>
				Console.RED
			case 2 =>
				Console.MAGENTA
			case _ =>
				Console.BLACK
		}
		
		
	}
	
	private[this] def getDirectionChar(gameObject:GameObject, orientation: Orientation):Char = {
	  var retval = ' '
	  if(gameObject != null){
		  orientation match {
		    case North => if(gameObject.orientation == North) retval = '^'
		    case South => if(gameObject.orientation == South) retval = 'v'
		    case West => if(gameObject.orientation == West) retval = '<'
		    case East => if(gameObject.orientation == East) retval = '>'
		  }
	  }
	  
	  retval
	}
	
	private[this] def getDisplayCharacter(gameObject : GameObject):Char = {
	  gameObject match  {
	    case base : Base => base.name.toUpperCase.charAt(0) // Base
	    case tower : Tower => tower.name.toUpperCase.charAt(0)
	    case peon : Peon => peon.name.toLowerCase.charAt(0)
	    case projectile : Projectile => projectile.name.toLowerCase.charAt(0)
	    case static : StaticGameObject => 
	    	static.name match{
	    		case "tree" => '&'
	    		case _ => static.name.toUpperCase.charAt(0)
	    	}
	    case _ =>
	      'x' // error
	  }
	}
	
	def printDebug = {
		println(gameController.gameField.playerList)
	}
	
	def readGameInput : Boolean = {
	  if(gameController.gameField == null) printLogo
		
	  var gameRunning = true;
	  
	  val input = Console.readLine
	  
	  
	  input.split(" ").toList match {
	    case "w" :: rest => gameController movePeon(North)
	    case "a" :: rest => gameController movePeon(West)
	    case "s" :: rest => gameController movePeon(South)
	    case "d" :: rest => gameController movePeon(East)
	    
	    case "b" :: peonName :: rest => gameController spawnPeon(peonName)
	    case "f" :: rest => gameController shoot
	    case "q" :: rest => gameController selectNextPeon
	    case "o" :: rest => gameController endRound
	    
	    case "pl" :: rest => printDebug
	    
	    case "new" :: mapName :: rest => gameController createNewGame(mapName)
	    case "end" :: rest => 
	    	gameController.endGame
	    	gameRunning = false
	    
	    
	    case _ =>
	      println("+-=================== Help ===================-+")
	      println("| w, a, s, d = moving in direction             |")
	      println("| q = select next peon                         |")
	      println("| f = shoot with selected peon                 |")
	      println("| o = end round                                |")
	      println("| b <peon name> = spawn peon (tank, rocketguy) |")
	      println("+----------------------------------------------+")
	      println("| new <map name> = create new Game             |")
	      println("| end = end the current game                   |")
	      println("+-============================================-+")
	      
	      
	  }
	  if(gameRunning)
		  this.readGameInput
	  
	  true
	}
	
	def printLogo = {
		println("+-============================================-+")
		println("#                                              #")
		println("#                Line Battle   (sn)            #")
		println("#                                              #")
		println("+-============================================-+")
	}
	
	def receiveUpdate(gameControllerObs: GameController) = {
		if(gameController.gameField != null){
			val selectedPeon = gameController.gameField.selectedPeon
			drawBaseInfo(gameController.gameField)
			drawGameGrid(gameController.gameField.gameGrid, selectedPeon)
		}
	}
	
	
}