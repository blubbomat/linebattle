package com.linebattle.model.game

import com.linebattle.model.objects._

case class PlayerData( base : Base, 
				  	   peons : List[Peon],
				  	   playerID : Int,
				  	   selectedPeonIndex : Int,
				  	   ressources : Int ) {
	
	def updateBase(newBase : Base) : PlayerData = {
		copy(base = newBase)
	}
	
	def addPeon( peon : Peon ) : PlayerData = {
		val newPeons = peons :+ peon
		copy( peons = newPeons )
	}
	
	def spawnPeon( peon : Peon ) : PlayerData = {
		
		if( ressources >= peon.buildCosts ) {
			val newRessources = ressources - peon.buildCosts
			val newPeons = peons :+ peon
			copy( ressources = newRessources, peons = newPeons )
		}
		else this
	}
	
	def isSpawnPossible( peon : Peon ) : Boolean = ressources >= peon.buildCosts
	
	def removePeon( peon : Peon ) : PlayerData = {
		val newPeons = peons diff List( peon )
		copy( peons = newPeons, selectedPeonIndex = 0 )
	}
	
	def updatePeon( peon : Peon ) : PlayerData = {
		
		val result = peons find ( p => p.id == peon.id )
		
		if( result != None ) {
			val index = peons indexOf ( result get )
			val newPeons = peons updated ( index, peon )
			copy( peons = newPeons )
		}
		
		else addPeon( peon )
	}
	
	
	def selectNextPeon : PlayerData = {
		if( peons.length > 0 ) {
			val nextSelectedPeonIndex = ( selectedPeonIndex + 1 ) % peons.length
			copy( selectedPeonIndex = nextSelectedPeonIndex )
		}
		else this
	}
	
	def isPeonSelected : Boolean = peons.length > 0
	
	def selectedPeon : Peon = {
		if( isPeonSelected ) {
			peons( selectedPeonIndex )
		}
		else throw new Exception( "There is no peon for player " + playerID + "!" )
	}
	
	def modifyRessources( modifier : Int ) : PlayerData = {
		val newRessources = Math max ( ressources - modifier, 0 )
		copy( ressources = newRessources ) 
	}
	
	override def toString:String = {
		val stringBuilder = new StringBuilder
		stringBuilder ++= "Player " ++= playerID.toString
		if(base != null){
			stringBuilder ++= " Base(" ++= base.health.toString ++= ")"
		}
		
		stringBuilder ++= " | Ressources: " ++= ressources.toString
		
		stringBuilder.toString
	}
}