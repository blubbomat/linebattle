package com.linebattle.view.swing.gamegrid

import scala.swing.GridPanel
import com.linebattle.model.game.GameField
import scala.swing.Swing
import javax.swing.Icon
import scala.swing.Label
import javax.swing.ImageIcon
import scala.swing.Dialog


class GameFieldGridPanel(val gameField : GameField) extends GridPanel(1, 1){
	val imageFolder = "gamefiles/graphics/"
		
	updateGameGrid(gameField)
	
	border = Swing.TitledBorder(border, "Battlefield")
	
	def updateGameGrid(gameField : GameField){
		contents.clear
		columns = 1
		rows = 1
		
		if(gameField != null){
			columns = gameField.gameGrid.width
			rows = gameField.gameGrid.height
			
			gameField.gameGrid.grid.foreach(rowList =>{
	
				rowList.foreach(gameObject => {									
					contents += new GameObjectPanel(gameObject, gameField.selectedObjectID, gameField)			    
				})
				
			})
			
			
		}
		else {
			val label = new Label
			label.icon = new ImageIcon("gamefiles/graphics/title.png")
			contents += label
		}
	}
	
}