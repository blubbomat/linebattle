package com.linebattle.view.swing

import scala.swing.Frame
import scala.swing.BorderPanel
import scala.swing.Button
import com.linebattle.controller.GameController
import scala.swing.event.KeyPressed
import scala.swing.event.Key
import com.linebattle.model.objects._
import com.linebattle.util.Observer
import com.linebattle.controller.GameController
import com.linebattle.view.swing.gamegrid.GameFieldGridPanel
import scala.swing.Dialog

class LineBattlePanel(val gameController : GameController) extends BorderPanel with Observer[GameController]{
	val gameFieldGridPanel = new GameFieldGridPanel(gameController.gameField)
	val selectedObjectPanel = new SelectedObjectPanel(gameController.gameField)
	val playerInfoPanel = new PlayerInfoPanel(gameController.gameField)

	add(playerInfoPanel, BorderPanel.Position.North)
	add(gameFieldGridPanel, BorderPanel.Position.Center)
	add(selectedObjectPanel, BorderPanel.Position.East)
	
	listenTo(keys)

	reactions += {
            case KeyPressed(_, Key.W, _, _) => gameController.movePeon(North)
            case KeyPressed(_, Key.A, _, _) => gameController.movePeon(West)
            case KeyPressed(_, Key.S, _, _) => gameController.movePeon(South)
            case KeyPressed(_, Key.D, _, _) => gameController.movePeon(East)
            
            case KeyPressed(_, Key.F, _, _) => gameController.shoot
            case KeyPressed(_, Key.Q, _, _) => gameController.selectNextPeon
            case KeyPressed(_, Key.O, _, _) => gameController.endRound
    }
    focusable = true
    this.repaint

    def receiveUpdate(gameControllerObs: GameController) = {
		gameFieldGridPanel.updateGameGrid(gameController.gameField)
		selectedObjectPanel.updateData(gameController.gameField)
		playerInfoPanel.updatePlayerInfo(gameController.gameField)
		
		if(gameController.gameField != null && gameController.gameField.getWinner != None){
			Dialog.showMessage(this, "The winner is player " + gameController.gameField.getWinner.get)
		}
	}
}