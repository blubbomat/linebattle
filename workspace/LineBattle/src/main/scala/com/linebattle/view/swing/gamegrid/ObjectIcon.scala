package com.linebattle.view.swing.gamegrid

import scala.swing.Label
import java.awt.Graphics2D
import com.linebattle.model.objects.GameObject
import javax.swing.ImageIcon
import scala.swing.Color
import java.awt.Image

class ObjectIcon(gameObject : GameObject, color : Option[Color]) extends Label{
	val imageFolder = "gamefiles/graphics/"
		
	var rotation = 0
	
	if(gameObject != null){
		val imageIcon = new ImageIcon(imageFolder + gameObject.name.toLowerCase + ".png")
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT))
		
		rotation = gameObject.orientation.toDegree
		icon = imageIcon
		if(color != None){
			background = color.get
			opaque = true
		}
		
	}
	
	
	override def paint(graphics2D : Graphics2D){
	    graphics2D.rotate(Math.toRadians(rotation), this.size.width/2.0, this.size.height/2)
		
		super.paint(graphics2D)
	}
}