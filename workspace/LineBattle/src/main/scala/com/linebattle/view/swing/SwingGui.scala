package com.linebattle.view.swing

import scala.swing.SimpleSwingApplication
import scala.swing.MainFrame
import scala.swing.Label
import scala.swing.BorderPanel
import scala.swing.FlowPanel
import com.linebattle.controller.GameController
import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import com.linebattle.util.Observer

object SwingGui extends SimpleSwingApplication with Observer[GameController]{
	val gameController = new GameController
	
	gameController.addObserver(this)
	
	val linebattlepanel = new LineBattlePanel(gameController)
	
	
	val mainFrame = new MainFrame {
		title = "LineBattle"
		menuBar = new LineBattleMenuBar(gameController)
		
	    contents = new FlowPanel{
	    	
	    	contents += linebattlepanel
	    } 
	} 
	
	def top = mainFrame
	
	def receiveUpdate(gameControllerObs: GameController) = {
		linebattlepanel.receiveUpdate(gameControllerObs)
		
		top.peer.paintComponents(top.peer.getGraphics())
		top.pack()
	}
}