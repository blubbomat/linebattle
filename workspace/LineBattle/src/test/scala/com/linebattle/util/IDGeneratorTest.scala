package com.linebattle.util

import org.scalatest._

class IDGeneratorTest extends FunSpec with Matchers {

	describe( "An IDGenerator" ) {
		
		it( "should be resetable." ) {
			IDGenerator.generateID
			val result1 = IDGenerator.generateID
			IDGenerator.reset
			val result2 = IDGenerator.generateID
			
			result1 shouldNot be ( 0 )
			result2 should be ( 0 )
		}
		
		it( "should start with the id 0." ) {
			IDGenerator.reset
			val result = IDGenerator.generateID
			result should be ( 0 )
		}
		
		it( "should return a copy of the current id by value." ) {
			
			val result1 = IDGenerator.generateID
			val result2 = IDGenerator.generateID
			
			result1 shouldNot be (result2)
		}
	}
}