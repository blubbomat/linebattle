package com.linebattle.view.swing

import scala.swing.MenuBar
import com.linebattle.controller.GameController
import scala.swing.MenuItem
import scala.swing.Menu
import scala.swing.Action
import java.io.File
import com.linebattle.view.swing.util.UIUtil


class LineBattleMenuBar(val gameController : GameController) extends MenuBar{
	
	val quitAction = Action("Quit") {System.exit(0)}
	
	val mapPath = new File("gamefiles/maps/")
	val endGame = Action("End Game"){gameController.endGame}
	
	
	
	contents += new Menu("Game") {
		contents += new Menu("New Game"){
			for(file <- mapPath.listFiles if file.getName endsWith ".map"){
				val menuItem = new MenuItem(file.getName)
				val mapName = UIUtil.getFileNameWithoutEnd(file, ".map")
				menuItem.action = Action(mapName){gameController.createNewGame(mapName)}
				
				contents += menuItem	
			}
		}
		contents += new MenuItem(endGame)
		contents += new MenuItem(quitAction)
	}
	
	contents += new Menu("Build"){
		val peonPath = new File("gamefiles/objects/peons/")
		println(peonPath.getAbsolutePath())
		
		for(file <- peonPath.listFiles if file.getName endsWith ".peon"){
			val menuItem = new MenuItem(file.getName)
			val peonName = UIUtil.getFileNameWithoutEnd(file, ".peon")
			menuItem.action = Action(peonName){gameController.spawnPeon(peonName)}
			
			contents += menuItem	
			println("found " + peonName)
		}
	}
	
	
	
	
	
}