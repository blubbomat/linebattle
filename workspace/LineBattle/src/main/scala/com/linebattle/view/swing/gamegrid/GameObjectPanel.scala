package com.linebattle.view.swing.gamegrid

import scala.swing.BorderPanel
import com.linebattle.model.objects.GameObject
import scala.swing.Label
import java.awt.Color
import scala.swing.Swing
import com.linebattle.view.swing.util.UIUtil
import com.linebattle.model.objects._
import com.linebattle.model.game.GameField
import java.awt.Font

class GameObjectPanel(gameObject : GameObject, selectedObjectId : Option[Int], gameField : GameField) extends BorderPanel{
	
	border = Swing.LineBorder(java.awt.Color.BLACK)
	
	background = new Color(170,102,0)
	opaque = true

	if(gameObject != null){
		val healthLabel = new Label(" ")
		healthLabel.font = new Font(healthLabel.font.getName, Font.PLAIN, 7)
		
		val color = getColor(gameObject, gameField)
		
		if(gameObject.health.isValid){
			healthLabel.text = gameObject.health.toString
			
			if(selectedObjectId != None && selectedObjectId.get == gameObject.id){
				healthLabel.background = java.awt.Color.CYAN
				healthLabel.opaque = true
			}
		}	
		
		add(healthLabel, BorderPanel.Position.North)
		
		val objectIcon = new ObjectIcon(gameObject, color)
		add(objectIcon, BorderPanel.Position.Center)
	}
	
	
	private[this] def getColor(gameObject : GameObject, gameField : GameField) : Option[Color] = {
		if(gameObject != null){
			gameObject match {
				case peon : Peon => UIUtil.getPlayerColor(gameObject.playerId)
				case base : Base => UIUtil.getPlayerColor(gameObject.playerId)
				case tower : Tower => 
					var color : Option[Color] = None
					for(playerId <- 0 until gameField.playerList.length){
						if(gameField.isTowerConquered(tower, playerId)){
							color = UIUtil.getPlayerColor(playerId)
						}
					}
					color
				case _ => None
			}
			
		}
		else {
			None
		}
	}
	
}