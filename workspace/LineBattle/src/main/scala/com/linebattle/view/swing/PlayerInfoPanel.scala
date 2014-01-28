package com.linebattle.view.swing

import scala.swing.BoxPanel
import scala.swing.Orientation
import com.linebattle.model.game.GameField
import scala.swing.Label
import scala.swing.Swing
import com.linebattle.model.game.PlayerData
import java.awt.Color
import com.linebattle.view.swing.util.UIUtil

class PlayerInfoPanel(gameField : GameField) extends BoxPanel(Orientation.Horizontal){
	
	updatePlayerInfo(gameField)

	def updatePlayerInfo(gameField : GameField){
		contents.clear
		
		if(gameField != null){
			gameField.playerList.foreach(playerData =>{
				
				val boxPanel = new BoxPanel(Orientation.Horizontal){	
					val label = new Label(playerData.toString)
					border = Swing.TitledBorder(border, "Player " + playerData.playerID)
					
					if(gameField.activePlayer == playerData.playerID){
						background = java.awt.Color.CYAN
						opaque = true
					}
					
					val color = UIUtil.getPlayerColor(playerData.playerID)				
					if(color != None){
						label.background = color.get
						label.opaque = true
					}
					
					contents += label
				}
				
				
				
				
				contents += boxPanel
			})
		}
	}
}