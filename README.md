linebattle
==========


This readme will explain the useage and will give some background informations of the project.

1.1 Project

	This project is splitted into two projects. The main project which uses sbt is at workspace/LineBattle.
	You can run the textual and graphical UI(swing) with sbt. The web UI is in another project (workspace/LineBattlePlay)
	which can be run by play.

1.2 Open Game Engine

	This game engine is build to easily add new units and maps. In the "gamefiles" folder are the files which will be read
	by the game engine. The engine will regnonize new files on the startup and will add the new units to the game. The graphics
	have so restrictions. The name of the picture has to be the same as the name of the peon, base, staticobject or projectile
	and it should be the size of 50x50.

2.1 Game

	Each player got a base where he can spawn units. These units are called peons. The standard peons are tank, jeep and a launcher.
	Each peon has it advantages and disadvantages. For example the jeep is fast and makes less damage. The launcher is slow and can
	shoot a projectile which does huge damage in an area. The tank is an allround unit. The goal of the game is to destroy the bases
	of the other players. This game is turn based, you will get on end of each round some ressources. Which depends on the number of
	towers you have conquered.

2.2 Controls:

	w,a,s,d: move peon
	q: select next peon
	f: fire with selected peon
	o: end round -> players next turn
