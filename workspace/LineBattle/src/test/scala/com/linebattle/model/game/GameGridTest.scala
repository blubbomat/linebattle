package com.linebattle.model.game

import org.scalatest._
import scala.collection.immutable.HashMap
import com.linebattle.model.objects._
import com.linebattle.util._

class GameGridTest extends FunSpec with Matchers {
	
	describe( "A GameGrid" ) {
		
		it( "should contain a 2-dimensional array and calculate the width and height." ) {
			val grid = List.fill[ GameObject ]( 80, 100 )( null )
			val gameGrid = new GameGrid( grid, new HashMap() )
			
			gameGrid.width should be ( 100 )
			gameGrid.height should be ( 80 )
		}
		
		it( "should be valid if the passed 2D array is rectangular." ) {
			val grid = List.fill[ GameObject ]( 80, 100 )( null )
			val gameGrid = new GameGrid( grid, new HashMap() )
			
			gameGrid.isValid should be ( true )
		}
		
		it( "should be invalid if the passed 2D array is not rectangular." ) {
			val grid = List.fill[ GameObject ]( 80, 100 )( null )
			val newRow = List.fill[ GameObject ]( 10 )( null )
			val invalidGrid = grid :+ newRow
			val gameGrid = new GameGrid( invalidGrid, new HashMap() )
			
			gameGrid.width should be ( 100 )
			gameGrid.height should be ( 81 )
			gameGrid.isValid should be ( false )
		}
		
		it( "should have an access to each grid element." ) {
			val grid = List.fill[ GameObject ]( 80, 100 )( null )
			val gameGrid = new GameGrid( grid, new HashMap() )
			
			for( x <- 0 to 99 ) {
				for( y <- 0 to 79 ) {
					gameGrid at( x, y ) should be ( null )
				}
			}
		}
		
		it( "should be immutable" ) {
			val grid = List.fill[ GameObject ]( 80, 100 )( null )
			val gameGrid = new GameGrid( grid, new HashMap() )
			val gameGrid2 = gameGrid set( 0, 0, new StaticGameObject() )
			gameGrid.at( 0, 0 ) should be ( null )
			gameGrid2.at( 0, 0 ) shouldNot be ( null )
		}
		
		it( "should throw an IllegalArgumentException, if the passed indices are out of bounds." ) {
			val grid = List.fill[ GameObject ]( 80, 100 )( null )
			val gameGrid = new GameGrid( grid, new HashMap() )
			
			a [IllegalArgumentException] should be thrownBy {
				gameGrid at( -1, 0 )
			}
			
			a [IllegalArgumentException] should be thrownBy {
				gameGrid at( 0, -1 )
			}
			
			a [IllegalArgumentException] should be thrownBy {
				gameGrid at( 100, 0 )
			}
			
			a [IllegalArgumentException] should be thrownBy {
				gameGrid at( 0, 80 )
			}
		}
	}
}