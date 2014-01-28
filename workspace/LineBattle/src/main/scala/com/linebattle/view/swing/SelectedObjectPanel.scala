package com.linebattle.view.swing


import scala.swing.BoxPanel
import scala.swing.Orientation
import com.linebattle.model.objects.GameObject
import scala.swing.Label
import com.linebattle.model.objects.Peon
import scala.swing.Swing
import com.linebattle.model.game.GameField
import java.awt.Dimension



class SelectedObjectPanel(gameField : GameField) extends BoxPanel(Orientation.Vertical){
	
	updateData(gameField)
	border = Swing.TitledBorder(border, "Selected Peon")
	
	def updateData(gameField : GameField){
		contents.clear
		
		if(gameField != null){
			val gameObject = gameField.selectedPeon
			if(gameObject != None){
				contents += new Label("Name: " + gameObject.get.name)
				contents += new Label("Health: " + gameObject.get.health.toString)
				gameObject.get match{
					case peon : Peon => 
						contents += new Label("Action Points: " + peon.actionPoints.toString)
						contents += new Label("Moving costs: " + peon.movingCosts)
						contents += new Label("Shooting costs: " + peon.shootingCosts)
						contents += new Label("Projectile: " + peon.projectile)
				}
			}
		}
		preferredSize = new Dimension(200, 200)
	}
}