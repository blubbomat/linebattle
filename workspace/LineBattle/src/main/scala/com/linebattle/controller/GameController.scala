package com.linebattle.controller

import com.linebattle.model.objects.Orientation
import com.linebattle.model.game.GameField
import com.linebattle.util._
import com.linebattle.util.Subject

class GameController(	val mapLoader : MapLoader = new MapLoader,
						val sleepTimeOnStep: Int = 300
						) extends Subject[GameController]{
	var gameField : GameField = null
  
	def createNewGame(mapName:String) = {
	  gameField = mapLoader loadMap(mapName)
	  this.notifyObservers
	}
	
	def endGame = {
		gameField = null
		this.notifyObservers
	}
	
	def isGameRunning = gameField != null
	
	def movePeon(orientation: Orientation) = {
	  try{
		  if(gameField != null && gameField.getWinner == None){
			  gameField = gameField movePeon(orientation)
			  this.notifyObservers
		  }
	  } catch {
	  	case e : Exception => gameField
	  }
	}
	
	def shoot = {
		try {
			if(gameField != null && gameField.getWinner == None){
			  gameField = gameField.shoot
			  
			  this.notifyObservers
			  
			  while(gameField.canAnProjectileMove){
			  	Thread.sleep(sleepTimeOnStep)
			  	gameField = gameField.nextStep
			  	this.notifyObservers		  	
			  }
		   }
		} catch {
	  	case e : Exception => gameField
	  }
	}
	
	def selectNextPeon = {
	   try{
		  if(gameField != null && gameField.getWinner == None){
			  gameField = gameField.selectNextPeon
			  this.notifyObservers
		  }
	  } catch {
	  	case e : Exception => gameField
	  }
	}
	
	def spawnPeon(factoryID: String) = {
	  if(gameField != null && gameField.getWinner == None){
	  	  try{
	  		  gameField = gameField spawnPeon(factoryID)
	  		  this.notifyObservers
	  	  } catch {
	  	  	case e:Exception =>
	  	  }		  
	  }
	}
	
	def endRound = {
	  if(gameField != null && gameField.getWinner == None){
		  gameField = gameField.nextRound
		  this.notifyObservers
	  }
	}
}