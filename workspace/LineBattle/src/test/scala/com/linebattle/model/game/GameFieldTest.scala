package com.linebattle.model.game
import org.scalatest._
import com.linebattle.controller.MapLoader
import com.linebattle.model.objects.GameObjectFactory
import com.linebattle.model.objects._

class GameFieldTest extends FunSpec with Matchers {
	val gameObjectFactory = new GameObjectFactory("testfiles/objects")
	val mapLoader = new MapLoader(	"testfiles/maps/",
	  		 							gameObjectFactory)
	
	describe("loaded map(testmap)"){
		
		val gameField = mapLoader.loadMap("testmap")
		val gameFieldWithoutBase = gameField.removeGameObject(gameField.gameGrid.at(2,4))
		
		it("should remove peon on explotion with high damage projectile"){
			val peon = gameField.gameGrid.at(9,3).asInstanceOf[Peon]
			
			val highDmgProjectile = gameObjectFactory.createProjectileFromFile(North, "highDamage")
			val newGameField = gameField.explodeProjectile(highDmgProjectile, new Position(9,3))
			
			val removedPeon = newGameField.gameGrid.at(9,3)
			removedPeon should be (null)
			
			val oldPeonCount = gameField.playerList(peon.playerId).peons.length
			val newPeonCount = newGameField.playerList(peon.playerId).peons.length
			
			newPeonCount should be (oldPeonCount -1)
		}
		
		it("should damage the peon from explotion with normal damage projectile"){
			val peon = gameField.gameGrid.at(9,3).asInstanceOf[Peon]
			
			val projectile = gameObjectFactory.createProjectileFromFile(North, "projectileTank")
			
			val newGameField = gameField.explodeProjectile(projectile, new Position(9,3))
			
			val damagedPeon = newGameField.gameGrid.at(9,3).asInstanceOf[Peon]
			
			damagedPeon.health.value should be (peon.health.value - projectile.damage)			
		}
		
		it("should damage two peons with damage radius on projectile"){
			val radiusProjectile = gameObjectFactory.createProjectileFromFile(North, "damageRadius")	
			val newGameField = gameField.explodeProjectile(radiusProjectile, new Position(13,2))
			
			gameField.gameGrid.at(14,2) shouldNot be (null)
			newGameField.gameGrid.at(14,2) should be (null)
			
			gameField.gameGrid.at(13,3) shouldNot be (null)
			newGameField.gameGrid.at(13,3) should be (null)
		}
		
		it("should remove an peon"){
			val peon = gameField.gameGrid.at(13,3).asInstanceOf[Peon]
			val newGameField = gameField.removeGameObject(peon)
			
			val oldPeonCount = gameField.playerList(peon.playerId).peons.length
			
			val removedPeon = newGameField.gameGrid.at(13,3)
			
			removedPeon should be (null)
			
			val newPeonCount = newGameField.playerList(peon.playerId).peons.length
			
			newPeonCount should be (oldPeonCount -1)
		}
		
		it("should throw an exception on removing a tower"){
			val tower = gameField.gameGrid.at(9,2).asInstanceOf[Tower]
			
			a [Exception] should be thrownBy gameField.removeGameObject(tower)
		}
		
		it("should remove a base"){
			val base = gameField.gameGrid.at(27,4).asInstanceOf[Base]
			val newGameField = gameField.removeGameObject(base)
			
			val removedBase = newGameField.gameGrid.at(27,4)
			
			removedBase should be (null)
			
			val baseFromPlayer = newGameField.playerList(base.playerId).base
			
			baseFromPlayer should be (null)
		}
		
		it("should remove an static object"){
			val tree = gameField.gameGrid.at(3, 8).asInstanceOf[StaticGameObject]
			val newGameField = gameField.removeGameObject(tree)
			
			newGameField.gameGrid.at(3, 8) should be (null)
		}
		
		it("should throw exception on removing an unsupported object"){
			a [Exception] should be thrownBy gameField.removeGameObject(new UnsupportedGameObject)
		}
		
		
		it("should update an peon"){
			var newPeon = gameField.gameGrid.at(13,3).asInstanceOf[Peon]
			newPeon = newPeon.hasShooted
			
			val newGameField = gameField.updateGameObject(newPeon, None)
			
			val peonFromUpdatedGameField = newGameField.gameGrid.at(13,3).asInstanceOf[Peon]
			
			peonFromUpdatedGameField.actionPoints.value should be (6)
		}
		
		it("should update an static gameobject"){
			val tree = gameField.gameGrid.at(3, 8).asInstanceOf[StaticGameObject]
			val editedTree = tree.takeDamage(10)
			val newGameField = gameField.updateGameObject(editedTree, None)
			
			val updatedTree = newGameField.gameGrid.at(3, 8).asInstanceOf[StaticGameObject]
			updatedTree.health.value should be (editedTree.health.value)
		}
		
		it("should update an base"){
			val base = gameField.gameGrid.at(27,4).asInstanceOf[Base]
			val editedBase = base.takeDamage(10)
			val newGameField = gameField.updateGameObject(editedBase, None)
			
			val updatedBase = newGameField.gameGrid.at(27,4).asInstanceOf[Base]
			updatedBase.health.value should be (editedBase.health.value)
		}
		
		it("should throw exception on unsupported gameobject"){
			a [Exception] should be thrownBy gameField.updateGameObject(new UnsupportedGameObject, None)
		}
		
		it("should not get a winner"){
			gameField.getWinner should be (None)
		}
		
		it("should get a winner if only one base occurs"){
			val base = gameField.gameGrid.at(27,4).asInstanceOf[Base]
			val newGameField = gameField.removeGameObject(base)
			
			newGameField.getWinner should be (Some(0))
		}

		
		it("selected peon should rotate"){
			val newGameField = gameField.movePeon(North)
			
			val rotatedPeon = newGameField.gameGrid.at(13,3).asInstanceOf[Peon]
			
			rotatedPeon.orientation should be (North)
			rotatedPeon.actionPoints.value should be (7)
		}
		
		it("peon should move east"){
			val newGameField = gameField.movePeon(East)
			
			val movedPeon = newGameField.gameGrid.at(14,3).asInstanceOf[Peon]
			
			movedPeon shouldNot be (null)
			movedPeon.actionPoints.value should be (7)
			
			val oldPositionObject = newGameField.gameGrid.at(13,3)
			oldPositionObject should be (null)
		}
		
		it("should be able to use toString on playerData"){
			val playerString = gameField.playerList(0).toString
			playerString should not be (null)
		}
		
		it("the tower should be conquered by player 0"){
			val tower = gameField.gameGrid.at(9,2).asInstanceOf[Tower]
			
			gameField.isTowerConquered(tower, 1) should be (true)
			gameField.isTowerConquered(tower, 0) should be (false)
		}
		
		it("should calculate the player income"){
			gameField.nextRound.getActivePlayerIncome should be (30)
		}
		
		it("second tower should not be conquered"){
			val tower = gameField.gameGrid.at(13,2).asInstanceOf[Tower]
			
			gameField.isTowerConquered(tower, 1) should be (false)
			gameField.isTowerConquered(tower, 0) should be (false)
		}
		
		it("peon should shoot in East direction"){
			val newGameField = gameField.shoot
			
			val projectile = newGameField.gameGrid.at(14,3).asInstanceOf[Projectile]
			val peon = newGameField.gameGrid.at(13,3).asInstanceOf[Peon]
			
			projectile shouldNot be (null)
			
			newGameField.projectileList.length should be (1)
			
			peon.actionPoints.value should be (6)
		}
		
		
		
		it("should update the projectiles on next step"){
			val newGameField = gameField.shoot
			
			val projectile = newGameField.gameGrid.at(14,3).asInstanceOf[Projectile]
			val peon = newGameField.gameGrid.at(13,3).asInstanceOf[Peon]
			
			val nextStepGameField = newGameField.nextStep
			
			val newProjectile = nextStepGameField.gameGrid.at(15, 3).asInstanceOf[Projectile]			
			newProjectile shouldNot be (null)
			
			val oldProjectile = nextStepGameField.gameGrid.at(14,3)			
			oldProjectile should be (null)
			
			
		}
		
		it("should change values for nextRound"){
			var newGameField = gameField.movePeon(North) // rotate to north -> will decrease action points
			
			newGameField = newGameField.nextRound
			
			val peon = newGameField.gameGrid.at(13,3).asInstanceOf[Peon]
			
			newGameField.activePlayer should be (1)
			peon.actionPoints.value should be (10)
		}
		
		it("should not spawn an peon without base"){
			val newGameField = gameFieldWithoutBase.spawnPeon("tank")
			newGameField.gameGrid.at(3, 4) should be (null)
		}
		
		it("should have an selected object"){
			gameField.selectedPeon.get.name should be ("tank")
		}
	}
	
	describe("loaded empty Map"){
		val gameFieldEmpty = mapLoader.loadMap("empty_testmap")
		
		it("should give no selected peon"){
			gameFieldEmpty.selectedPeon should be (None)
		}
		
		it("should handle commands without selected peon"){	
			
			a [Exception] should be thrownBy gameFieldEmpty.movePeon(East)
			a [Exception] should be thrownBy gameFieldEmpty.shoot
			gameFieldEmpty.selectNextPeon
			gameFieldEmpty.switchActivePlayer
			gameFieldEmpty.nextRound
			gameFieldEmpty.nextStep
		}
		
		it("should return None on selectedObject"){
			val selectedObjectId = gameFieldEmpty.selectedObjectID
			selectedObjectId should be (None) 
		}
	
	  
		it("should spawn an peon"){
			 val newGameField = gameFieldEmpty.spawnPeon("tank")	
			 val secondGameField = newGameField.spawnPeon("tank")
			 val spawnedPeon = newGameField.gameGrid.at(3, 4).asInstanceOf[Peon]
			 
			 spawnedPeon.name should be("tank")
			 spawnedPeon.playerId should be (newGameField.activePlayer)
			 spawnedPeon.orientation should be (East)
			 spawnedPeon.actionPoints.value should be (10)
			 newGameField.selectedObjectID.get should be (spawnedPeon.id)			 
			 
			 secondGameField.gameGrid.at(3, 4).id should be (spawnedPeon.id)
		}
		
		
		
		it("should add an projectile"){
			val projectile = new Projectile()
			
			val newGameField = gameFieldEmpty.addGameObject(projectile, new Position(10, 4))
			
			val projectileListLength = newGameField.projectileList.length
			
			projectileListLength should be (1)
		}
		
		it("should add an peon"){
			val peon = new Peon(playerId = 0)
			val newGameField = gameFieldEmpty.addGameObject(peon, new Position(1, 1))
			
			newGameField.gameGrid.at(1, 1).id should be (peon.id)
		}
		
		it("should add a static object"){
			val tree = new StaticGameObject
			val newGameField = gameFieldEmpty.addGameObject(tree, new Position(1, 1))
			
			newGameField.gameGrid.at(1, 1).id should be (tree.id)
		}
		
		it("should throw an exception on adding unsupported object"){
			a [Exception] should be thrownBy gameFieldEmpty.addGameObject(new UnsupportedGameObject, new Position(1,1))
		}
	}
	
	describe("loaded map(shootingtest)"){
		val gameField = mapLoader.loadMap("shooting_test")
		
		it("should kill the low health peon"){
			val newGameField = gameField.shoot
			
			val shootingPeon = newGameField.gameGrid.at(13,3).asInstanceOf[Peon]
			
			val oldPeonWhoGetsShoot = gameField.gameGrid.at(14,3)
			oldPeonWhoGetsShoot shouldNot be (null)
			
			val nonExistendsPeon = newGameField.gameGrid.at(14,3)
			nonExistendsPeon should be (null)		
			
			
			shootingPeon.actionPoints.value should be (6)
		}
	}
	
	describe("loaded map(shootingtest_range)"){
		val gameField = mapLoader.loadMap("shooting_test_range")
		
		it("should kill low health peon on second step"){
			var newGameField = gameField.shoot
			
			// first step
			newGameField = newGameField.nextStep			
			
			newGameField.gameGrid.at(14, 3) should be (null)
			newGameField.gameGrid.at(15, 3) shouldNot be (null)
			
			// second step 
			newGameField = newGameField.nextStep
			
			newGameField.gameGrid.at(15, 3) should be (null)
			newGameField.gameGrid.at(16, 3) should be (null)
		}
	}
	
	
}