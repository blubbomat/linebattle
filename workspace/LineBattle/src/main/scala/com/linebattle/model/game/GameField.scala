package com.linebattle.model.game

import scala.collection.immutable.List
import com.linebattle.model.objects._
import com.linebattle.util.IDGenerator

case class GameField( playerList : List[PlayerData],
				 	  projectileList : List[ Projectile ],
				 	  towerLines : List[ List[ Tower ] ],
				 	  activePlayer : Int,
				 	  gameGrid : GameGrid,
				 	  objectFactory : GameObjectFactory = new GameObjectFactory() ) {
	
	def selectNextPeon : GameField = {
		val newPlayerData = playerList( activePlayer ).selectNextPeon
		val newPlayerList = playerList.updated( activePlayer, newPlayerData )
		copy( playerList = newPlayerList )
	}
	
	def switchActivePlayer : GameField = {
		val newPlayerID = ( activePlayer + 1 ) % playerList.length
		copy( activePlayer = newPlayerID )
	}
	
	def movePeon( moveOrientation : Orientation ) : GameField = {
		
		val activePlayerData = playerList( activePlayer )
		val activePeon = activePlayerData selectedPeon
		
		//Hat der Peon die richtige Orientierung?
		//1.) wenn nein bewege ihn nicht.
		if( activePeon.orientation != moveOrientation ) {
			changePeonOrientation( activePeon, moveOrientation )
		}
		
		//2.) wenn ja bewege ihn.
		else {
			movePeon( activePeon )
		}
	}
	
	private[this] def changePeonOrientation( peon : Peon, newOrientation : Orientation ) : GameField = {
		
		if( peon canMove ) {
			val changedPeon = peon changeOrientation ( newOrientation )
			val peonPosition = gameGrid getPosition ( changedPeon.id )
			val newGameGrid = gameGrid set( peonPosition, changedPeon )
			updatePeon( changedPeon, peonPosition, newGameGrid )
		}
		else this
	}
	
	private[this] def movePeon( peon : Peon ) : GameField = {
		
		val oldPosition = gameGrid getPosition ( peon.id )
		val newPosition = gameGrid neighborPosition( oldPosition, peon.orientation )
		
		if( peon.canMove && gameGrid.isInside( newPosition ) && !gameGrid.hasObjectAt( newPosition ) ) {
			val cleanGameGrid = gameGrid.remove( oldPosition )
			val newPeon = peon.hasMoved
			updatePeon( newPeon, newPosition, cleanGameGrid)
		}
		
		else this
	}
	
	
	
	private[this] def updatePeon( newPeon : Peon, position:Position, currentGameGrid : GameGrid) : GameField = {
		val activePlayerData = playerList( activePlayer )
		val newPlayerData = activePlayerData.updatePeon( newPeon )
		val newPlayerList = playerList.updated( activePlayer, newPlayerData )
		val newGameGrid = currentGameGrid.set(position, newPeon);
		
		copy( playerList = newPlayerList, gameGrid = newGameGrid )
	}
	
	
	def addGameObject(gameObject:GameObject, position : Position) : GameField = {
	  var newGameField = this
	  val newGameGrid = newGameField.gameGrid.set(position, gameObject)
	  
	  gameObject match{
			case peon : Peon =>
			  val newPlayerData = newGameField.playerList(peon.playerId).addPeon(peon)
			  val newPlayerList = newGameField.playerList.updated(peon.playerId, newPlayerData)
			  newGameField.copy(gameGrid = newGameGrid, playerList = newPlayerList)
			  
			case projectile : Projectile =>
			  val newProjectileList = projectileList :+ projectile
			  newGameField.copy(gameGrid = newGameGrid, projectileList = newProjectileList)

			case staticObject : StaticGameObject =>	
			  newGameField.copy(gameGrid = newGameGrid)
			  
			case _ => throw new Exception("unsupported gameobject(" + gameObject +") for addGameObject")
		}
	}
	
	def updateGameObject(gameObject : GameObject, position : Option[Position]) : GameField = {
		  var newGameField = this
		  var objectPosition = newGameField.gameGrid.getPosition(gameObject.id)
		  var newGameGrid = newGameField.gameGrid
		  if(position != None){
		  	newGameGrid = newGameGrid.remove(objectPosition)
		    objectPosition = position.get
		  }
		  
		  newGameGrid = newGameGrid.set(objectPosition, gameObject)	  
		  newGameField = newGameField.copy(gameGrid = newGameGrid)
		  
		  
		  gameObject match {
		  	
				case peon : Peon => 
					val newPlayerData = playerList(peon.playerId).updatePeon(peon)
					val newPlayerList = playerList.updated(peon.playerId, newPlayerData)
					newGameField.copy(playerList = newPlayerList)
				  
				case projectile : Projectile =>
					val projectileIndex = getProjectileIndex(projectile.id)
					val newProjectileList = projectileList.updated(projectileIndex, projectile)
					newGameField.copy(projectileList = newProjectileList)					
					
				case staticObject : StaticGameObject =>	
					newGameField
					
				case base : Base =>
					val newPlayerData = playerList(base.playerId).updateBase(base)
					val newPlayerList = playerList.updated(base.playerId, newPlayerData)
					newGameField.copy(playerList = newPlayerList)
					
				case tower : Tower =>
					newGameField					
					
				case _ => throw new Exception("unsupported gameobject(" + gameObject +") for updateGameObject")
		  }
	
	}
	
	private[this] def getProjectileIndex(projectileId : Int) : Int = {
		var index = -1
		
		for(i <- 0 until projectileList.length){
			val projectile = projectileList(i)
			
			if(projectileId == projectile.id){
				index = i
			}
		}
		
		index
	}
	
	def removeGameObject(gameObject : GameObject) : GameField = {
		gameObject match {
			case peon : Peon => 
				val newPlayerData = playerList(peon.playerId).removePeon(peon)
				val newPlayerList = playerList.updated(peon.playerId, newPlayerData)
				val newGameGrid = gameGrid.remove(peon)
				copy(playerList = newPlayerList, gameGrid = newGameGrid)
			case projectile : Projectile => 
				val newProjectileList = projectileList diff List(projectile)
				val newGameGrid = gameGrid.remove(projectile)
				copy(projectileList = newProjectileList, gameGrid = newGameGrid)
			case tower : Tower =>
				throw new Exception("Tower should not be removed")
			case staticObject : StaticGameObject =>
				val newGameGrid = gameGrid.remove(staticObject)
				copy(gameGrid = newGameGrid)
			case base : Base => 
				val newGameGrid = gameGrid.remove(base)
				val newPlayerData = playerList( base.playerId ).copy( base = null )
				val newPlayerList = playerList.updated( base.playerId, newPlayerData )
				copy( gameGrid = newGameGrid, playerList = newPlayerList )
			case _ => throw new Exception("unsupported gameobject(" + gameObject +") for removeGameObject")
				
		}
	}
	
	def explodeProjectile(projectile:Projectile, position:Position):GameField = {
		val radius = projectile.damageRadius - 1
		var newGameField = this

		for( x <- position.x-radius to position.x+radius ) {
			for( y <- position.y-radius to position.y+radius ) {
				try{
					if( newGameField.gameGrid hasObjectAt( x,  y ) ) {
						val gameObject = newGameField.gameGrid at( x, y )
						val damagedObject = gameObject takeDamage( projectile.damage )
						if( damagedObject isDead ) {
							newGameField = newGameField.removeGameObject(gameObject)
						}
						else {
							newGameField = newGameField.updateGameObject(damagedObject, None)
						}
					}
				} catch{
					case e : Exception => // explosion out of gamefield -> do nothing
				}
				// Else Do nothing
			}
		}
		
		newGameField
	}
	
	def shoot : GameField = {
		val activePlayerData = playerList( activePlayer )
		val activePeon = activePlayerData selectedPeon
		
		if( activePeon canShoot ) {
			val peonPosition = gameGrid getPosition( activePeon.id )
			val projectileSpawnPosition = gameGrid.neighborPosition( peonPosition, activePeon.orientation )
			
			if( gameGrid.hasObjectAt( projectileSpawnPosition ) ) {
			    var newGameField = this
				val newPeon = activePeon hasShooted
				val projectile = objectFactory.createProjectileFromFile( newPeon.orientation, newPeon.projectile )
				
				newGameField = newGameField.updateGameObject(newPeon, None)
				
				newGameField.explodeProjectile(projectile, projectileSpawnPosition)
			}
			
			else {
				val projectileInstance : Projectile = objectFactory.createProjectileFromFile( activePeon.orientation, activePeon.projectile )
				var newGameField = addGameObject(projectileInstance, projectileSpawnPosition)
				val newPeon = activePeon.hasShooted
				newGameField.updateGameObject(newPeon, None)
			}
		}
		
		else { 
			this
		}
	}
	
	def canAnProjectileMove():Boolean = {
		var anProjectileCanMove = false
		
		projectileList.foreach(projectile => {
			if(projectile.canMove){
				anProjectileCanMove = true
			}
		})
		
		
		anProjectileCanMove
	}
	
	def nextStep : GameField = {		
		var newGameField = this
		
		var projectileExplodeList = List.fill[Projectile](0)(null)
		var projectileExplodePositionList = List.fill[Position](0)(null)
		
		// move the projectiles
		projectileList.foreach(projectile => {
			val projectilePosition = newGameField.gameGrid.getPosition(projectile.id)
			
			if(projectile.canMove){				
				val nextPosition = newGameField.gameGrid.neighborPosition( projectilePosition, projectile.orientation )
				
				if(newGameField.gameGrid.isInside(nextPosition)){
					if( newGameField.gameGrid.isPositionFree(nextPosition) ){
						// move the projectile
						val newProjectile = projectile.hasMoved
						if(newProjectile.canMove){
							// projectile is still moving
							newGameField = newGameField.updateGameObject(newProjectile, Some(nextPosition))
						}
						else {
							// projectile reaches end -> explode
							projectileExplodeList = projectileExplodeList :+ projectile
							projectileExplodePositionList = projectileExplodePositionList :+ nextPosition
							newGameField = newGameField.removeGameObject(projectile)
						}
					}
					else {
						// projectile hits something -> explode
						projectileExplodeList = projectileExplodeList :+ projectile
						projectileExplodePositionList = projectileExplodePositionList :+ nextPosition
						newGameField = newGameField.removeGameObject(projectile)
					}
				}
				else {
					// projectile gets out of gamefield -> remove
					newGameField = newGameField.removeGameObject(projectile)
				}
			}
			else {
				
			}
		})
		
		
		// do the explosions
		for(i <- 0 until projectileExplodeList.length){
			newGameField = newGameField.explodeProjectile(projectileExplodeList(i), projectileExplodePositionList(i))
		}
		
		
		newGameField
	}	
	
	
	def updatePeon(peon:Peon):GameField = {
		val peonPosition = gameGrid.getPosition(peon.id)
		val newPlayerData =  playerList(peon.playerId).updatePeon(peon)
		val newPlayerList = playerList.updated(peon.playerId, newPlayerData)
		val newGrid = gameGrid.set(peonPosition, peon)
		
		copy(gameGrid = newGrid, playerList = newPlayerList)		
	}
	
	def isTowerConquered(tower:GameObject, playerId:Int):Boolean = {
		var enemyCount = 0
		var playerCount = 0
		val towerPosition = gameGrid.getPosition(tower.id)
		
		for(x <- towerPosition.x-1 to towerPosition.x+1){
			for(y <- towerPosition.y-1 to towerPosition.y+1){
				if(gameGrid.isInside(x, y)){
					val gameObject = gameGrid.at(x, y)
					if(gameObject != null){
						if(gameObject.playerId == playerId){
							playerCount = playerCount + 1
						}
						else {
							enemyCount = enemyCount + 1
						}
					}
				}
			}
		}
		
		(playerCount > 0 && enemyCount <= 1)
	}
	
	def getActivePlayerIncome : Int = {
		
		var income = 0
		
		if(playerList(activePlayer).base != null)
			income += playerList(activePlayer).base.income
		
		towerLines.foreach(towerList => {
			towerList.foreach( tower => {
				if( isTowerConquered(tower, activePlayer) ){
					income = income + tower.income
				}
			})
		})
		
		income
	}
	
	def addResourcesToActivePlayer() : GameField = {
		val ressourcesToAdd = getActivePlayerIncome
		
		val newPlayerData = playerList(activePlayer).modifyRessources(-ressourcesToAdd)
		val newPlayerList = playerList.updated(activePlayer, newPlayerData)
		
		copy(playerList = newPlayerList)
	}
	
	def nextRound : GameField = {
		
		var gameField = this
		
		var newProjectileList : List[ Projectile ] = List()
		
//		projectileList.foreach( projectile => {
//			val newProjectile = projectile.resetShootingRange
//			newProjectileList :+= newProjectile
//		})
		
		
		this.playerList(activePlayer).peons.foreach(peon => {
			val newPeon = peon.resetActionPoints
			gameField = gameField.updatePeon(newPeon)
		})
		
		
		gameField = gameField.addResourcesToActivePlayer
		
		
		gameField.switchActivePlayer.copy( projectileList = newProjectileList )
	}
	
	def spawnPeon( name : String ) : GameField = {
		
		val activePlayerData = playerList( activePlayer )
		val activeBase = activePlayerData.base
		if(activeBase != null){
			val basePosition = gameGrid getPosition( activeBase.id )
			val spawnPosition = gameGrid.neighborPosition( basePosition, activeBase.orientation )
			val isSpawnPositionInside = gameGrid isInside( spawnPosition )
			val isSpawnPositionFree = gameGrid isPositionFree( spawnPosition )
			
			val newPeon = objectFactory.createPeonFromFile( activePlayer, activeBase.orientation, name )
			
			if( activePlayerData.isSpawnPossible( newPeon ) && isSpawnPositionInside && isSpawnPositionFree ) {
				val newPlayerData = activePlayerData.spawnPeon( newPeon )
				val newGameGrid = gameGrid.set( spawnPosition, newPeon )
				val newPlayerList = playerList.updated( activePlayer, newPlayerData )
				copy( playerList = newPlayerList, gameGrid = newGameGrid )
			}		
			else this
		}
		else this
	}
	
	def selectedPeon : Option[Peon] = {
		if(isSelectedObject){
			Some(playerList(activePlayer).selectedPeon)
		} 
		else {
			None
		}
	}
	
	def selectedObjectID : Option[Int] = {
		if(isSelectedObject)
			Some(playerList( activePlayer ).selectedPeon.id)
		else 
			None		
	}
	
	def isSelectedObject : Boolean = {
		playerList(activePlayer).isPeonSelected
	}
	
	def getWinner : Option[Int] = {
		
		var numberOfBases = 0
		var winner : Option[Int] = None
		
		playerList.foreach( player => {
			
			if( player.base != null && player.base.health >= 1 ) {
				numberOfBases += 1
				winner = Some( player.playerID )
			}
		})
		
		if( numberOfBases == 1 ) {
			winner
		}
		else {
			None
		}
	}
}