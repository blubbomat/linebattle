package com.linebattle.controller

import org.scalatest._
import com.linebattle.model.game.GameField
import com.linebattle.model.objects._
import com.linebattle.model.game.GameField

class MapLoaderTest extends FunSpec with Matchers {
	describe("the testmap loaded from file"){
	  val mapLoader = new MapLoader(	"testfiles/maps/",
	  		 							new GameObjectFactory("testfiles/objects"))
	  var gameField = mapLoader.loadMap("testmap")
	  
	  it("should load the gamegrid"){
	    gameField.gameGrid.width should be (30)
	    gameField.gameGrid.height should be (9)	    
	    
	    gameField.playerList.size should be (2)	    
	  }
	  
	  it("should load the bases"){
	    val baseOne = gameField.playerList(0).base	    
	    baseOne.orientation should be (East)
	    gameField.gameGrid.at(2, 4) should be (baseOne)
	    
	    val baseTwo = gameField.playerList(1).base
	    baseTwo.orientation should be (West)
	    gameField.gameGrid.at(27, 4) should be (baseTwo) 
	  }
	  
	  it("should load the tower"){
	    val tower = getTower(9, 2, gameField)
	    tower.orientation should be (South)
	    tower.lineId should be (0)
	  }
	  
	  it("should load the peon"){
	    val peon = getPeon(5, 4, gameField)
	    peon.orientation should be (East)
	    peon.playerId should be (1)
	    
	    gameField.playerList(1).peons.length should be (3)
	    gameField.playerList(1).peons(0) should be (peon)
	  }
	  
	}
	
	def getPeon(x:Int, y:Int, gameField: GameField) : Peon = {
	  gameField.gameGrid.at(x, y) match {
	    case peon : Peon => peon
	    case _ => null
	  }
	}
	
	def getTower(x:Int, y:Int, gameField: GameField) : Tower = {
	  gameField.gameGrid.at(x, y) match {
	    case tower : Tower => tower
	    case _ => null
	  }
	}
}