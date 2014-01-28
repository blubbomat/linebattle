package com.linebattle.view.swing.util

import java.awt.Color
import java.io.File

object UIUtil {
	
	def getPlayerColor(playerId : Int) : Option[Color] = {
		playerId match {
			case 0 => Some(java.awt.Color.RED)
			case 1 => Some(java.awt.Color.BLUE)
			case 2 => Some(java.awt.Color.GREEN)
			case 3 => Some(java.awt.Color.YELLOW)
			
			case _ => None
		}
	}
	
	def getFileNameWithoutEnd(file: File, ending:String):String = {
		val fileName = file.getName
		fileName.substring(0, (fileName.length - ending.length))
	}
}