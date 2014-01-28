package com.linebattle.controller
import org.scalatest._
import com.linebattle.model.objects.GameObjectFactory
import com.linebattle.model.objects._

class GameControllerTest extends FunSpec with Matchers {
	describe("Loaded Testmap"){
		val gameObjectFactory = new GameObjectFactory("testfiles/objects")
		val mapLoader = new MapLoader(	"testfiles/maps/",
	  		 							gameObjectFactory)
		
		
		
		it("should shoot an projectile without hitting anything"){
			val gameControllerTemp : GameController = new GameController(mapLoader = mapLoader, sleepTimeOnStep = 0)
			gameControllerTemp.createNewGame("testmap")
			
			val oldSize = gameControllerTemp.gameField.gameGrid.map.size
			gameControllerTemp.shoot
			
			gameControllerTemp.gameField.gameGrid.map.size should be (oldSize)
		}
		
		it("should be running"){
			val gameController : GameController = new GameController(mapLoader = mapLoader, sleepTimeOnStep = 0)
			gameController.createNewGame("testmap")
			
			gameController.isGameRunning should be (true)
		}
		
		it("should spawn an known peon"){
			val gameController : GameController = new GameController(mapLoader = mapLoader, sleepTimeOnStep = 0)
			gameController.createNewGame("testmap")
			
			gameController.spawnPeon("tank")
			
			val peon = gameController.gameField.gameGrid.at(3, 4).asInstanceOf[Peon]
			
			peon should not be (null)
		}
		
		it("should not spawn a unknown peon"){
			val gameController : GameController = new GameController(mapLoader = mapLoader, sleepTimeOnStep = 0)
			gameController.createNewGame("testmap")
			
			gameController.spawnPeon("unknownBla")
			
			val peon = gameController.gameField.gameGrid.at(3, 4).asInstanceOf[Peon]
			
			peon should be (null)
		}
		
		it("should switch to next player on end round"){
			val gameController : GameController = new GameController(mapLoader = mapLoader, sleepTimeOnStep = 0)
			gameController.createNewGame("testmap")
			
			gameController.endRound
			
			gameController.gameField.activePlayer should be (1)
		}
		
		it("should end the game"){
			val gameController : GameController = new GameController(mapLoader = mapLoader, sleepTimeOnStep = 0)
			gameController.createNewGame("testmap")
			
			gameController.endGame
			
			gameController.isGameRunning should be (false)
		}
		
		it("a peon without enough action points should not move anymore"){
			val gameController : GameController = new GameController(mapLoader = mapLoader, sleepTimeOnStep = 0)
			gameController.createNewGame("testmap")
			gameController.selectNextPeon
			gameController.shoot
			gameController.shoot
			gameController.shoot
			gameController.shoot
			
			gameController.movePeon(North)
			gameController.movePeon(East)
			
			gameController.gameField.selectedPeon.get.asInstanceOf[Peon].actionPoints.value should be (2)
		}
	}
}