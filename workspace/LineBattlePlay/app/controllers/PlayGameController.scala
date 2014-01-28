package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.linebattle.controller.GameController
import com.linebattle.model.game._
import com.linebattle.model.objects._
import com.linebattle.controller.MapLoader

object PlayGameController extends Controller {
	
	val factory = new GameObjectFactory( "public/objects" )
	val mapLoader = new MapLoader( "public/maps/", factory )
	val lineBattle = new GameController( mapLoader, sleepTimeOnStep = 0 )
	
	def index = Action {
		buildWebsite
	}
	
	def newGame = Action {
		lineBattle.createNewGame( "play" )
		Redirect( routes.PlayGameController.index )
	}
	
	def endGame = Action {
		lineBattle.endGame
		Redirect( routes.PlayGameController.index )
	}
	
	def movePeon( orienation : Int ) = Action {
		
		orienation match {
			case 0 => lineBattle.movePeon( North )
			case 1 => lineBattle.movePeon( East )
			case 2 => lineBattle.movePeon( South )
			case 3 => lineBattle.movePeon( West )
		}
		
		Redirect( routes.PlayGameController.index )
	}
	
	def shoot = Action {
		lineBattle.shoot
		Redirect( routes.PlayGameController.index )
	}
	
	def selectNextPeon = Action {
		lineBattle.selectNextPeon
		Redirect( routes.PlayGameController.index )
	}
	
	def spawnPeon( id : String ) = Action {
		lineBattle.spawnPeon( id )
		Redirect( routes.PlayGameController.index )
	}
	
	def endRound = Action {
		lineBattle.endRound
		Redirect( routes.PlayGameController.index )
	}
	
	private[this] def buildWebsite = {
		Ok( views.html.index( "Line Battle", lineBattle ) )
	}
}