package com.linebattle.controller
import com.linebattle.model.game.GameField
import scala.io.Source
import scala.collection.mutable.LinkedList
import com.linebattle.model.objects.Tower
import com.linebattle.model.objects.GameObjectFactory
import com.linebattle.model.objects._
import com.linebattle.model.game.GameField
import com.linebattle.model.game.GameGrid
import com.linebattle.model.game.GameGrid
import scala.collection.immutable.HashMap
import com.linebattle.model.game.GameField
import com.linebattle.model.game.GameGrid
import com.linebattle.model.game.PlayerData
import com.linebattle.model.game.PlayerData
import scala.util.control.Exception

class MapLoader(	val mapFolderPath : String = "gamefiles/maps/", 
					val gameObjectFactory : GameObjectFactory = new GameObjectFactory ) {
	
	
	def loadMap(mapName: String) : GameField = {	  
	  var gameGrid:GameGrid = null
	  
	  
	  
	  var towerList = List.fill[Tower](0)(null)
	  var playerList = List.fill[PlayerData](0)(null)
	  	  
	  try{
		  for( line <- Source.fromFile( mapFolderPath + mapName + ".map" ).getLines ) {
		    val command = line.split(" ").toList
		    
		    command match {
		      case "#" :: rest => // commentary
		      
		      case "map" :: x :: y :: rest =>		      	
		      	gameGrid = new GameGrid( List.fill[ GameObject ](y.toInt, x.toInt)(null), new HashMap) 
		      	
		      case "base" :: x :: y :: orientation :: startressourcen :: rest =>
		        val base = gameObjectFactory.createBaseFromFile(playerList.length, toOrientation(orientation), "base")
		        gameGrid = gameGrid.set(x.toInt, y.toInt, base)
		        val newPlayer = new PlayerData(	base, 
		        								List.fill[Peon](0)(null),
		        								playerList.length,
		        								0,
		        								startressourcen.toInt)
		        
		        playerList = playerList.:+(newPlayer)
		        
		      case "tower" :: factoryId :: x :: y :: orientation :: towerLineId :: rest =>
		        val tower = gameObjectFactory.createTowerFromFile(toOrientation(orientation), towerLineId.toInt, factoryId)
		        gameGrid = gameGrid.set(x.toInt, y.toInt, tower)
		        towerList = towerList.+:(tower)	   
	
		      case "peon" :: factoryId :: x :: y :: orientation :: playerId :: rest =>
		        val peon = gameObjectFactory.createPeonFromFile(playerId.toInt, toOrientation(orientation), factoryId)
		        gameGrid = gameGrid.set(x.toInt, y.toInt, peon)
		        val newPlayerData = playerList(playerId.toInt).addPeon(peon)
		        playerList = playerList.updated(playerId.toInt, newPlayerData)
		        
		      case "staticobject" :: factoryId :: x :: y :: orientation :: rest =>
		        val staticGameObject = gameObjectFactory.createStaticObjectFromFile(toOrientation(orientation), factoryId)
		        gameGrid = gameGrid.set(x.toInt, y.toInt, staticGameObject)	        
		      
		      case _ =>
		    }
		  }
		  
		  
		  
		  
		  new GameField(playerList,
				  		List.fill[Projectile](0)(null),
				  		createTowerLines(towerList),
				  		0,
				  		gameGrid,
				  		gameObjectFactory)
	  
	  } catch {
	  	case e: Exception => null
	  }
	}
	
	def createTowerLines(towerList : List[Tower]): List[ List[ Tower ]] = {
	  var towerLineCount = -1
	  
	  // get the towerline count
	  towerList.foreach(tower => { 
	    if(tower.lineId > towerLineCount){
	      towerLineCount = tower.lineId
	    }
	  })
	  
	  // create the empty towerLines list
	  var towerLines = List.fill[List[Tower]](towerLineCount + 1)(List.fill[Tower](0)(null))
	  
	  // fill the list
	  towerList.foreach(tower => { 
	    val newTowerLinetowerLines = towerLines(tower.lineId).+:(tower)	    
	    towerLines = towerLines.updated(tower.lineId, newTowerLinetowerLines)	    
	  })
	  
	  towerLines
	}
	
	def toOrientation(orientation : String):Orientation = {
		orientation.toUpperCase() match{
		  case "NORTH" => North
		  case "SOUTH" => South
		  case "WEST" => West
		  case "EAST" => East		  
		}
	}
}