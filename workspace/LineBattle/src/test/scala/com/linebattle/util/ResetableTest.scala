package com.linebattle.util

import org.scalatest._

class ResetableTest extends FunSpec with Matchers {

	describe( "Resetable" ) {
		
		it( "should have a maximum value, a current value and a flag for validation." ) {
			val testObject = new Resetable( 100, 10, true )
			testObject.maximum should be ( 100 )
			testObject.value should be ( 10 )
			testObject.valid should be ( true )
		}
		
		it( "should be able to reset the current value to the maximum value" ) {
			val testObject = new Resetable( 23, 1, true )
			val testObject1 = testObject.reset
			testObject.value should be ( 1 )
			testObject1.value should be ( 23 )
		}
		
		it( "should be immutable" ) {
			val testObject = new Resetable( 100, 10, false )
			val testObject1 = testObject value = ( 14 )
			val testObject2 = testObject value = ( 20 )
			testObject.value should be ( 10 )
			testObject1.value should be ( 14 )
			testObject2.value should be ( 20 )
		}
		
		it( "should change the new value to the maximum value, if the new value is greater than the maximum value" ) {
			val testObject = new Resetable( 100, 10, false )
			val testObject1 = testObject value = ( 110 )
			testObject1.value should be ( 100 )
		}
		
		it( "should be comarable with >= operator. Therefore it should use the current value." ) {
			val testObject = new Resetable( 100, 10, true )
			( testObject >= 10 ) should be ( true )
			( testObject >= 11 ) should be ( false )
		}
		
		it( "should be comarable with <= operator. Therefore it should use the current value." ) {
			val testObject = new Resetable( 100, 10, true )
			( testObject <= 10 ) should be ( true )
			( testObject <= 9 ) should be ( false )
		}
		
		it( "should be modifiable if it is valid." ) {
			val testObject = new Resetable( 100, 10, true )
			val testObject2 = testObject - 10
			val testObject3 = testObject + 20
			testObject2.value should be ( 0 )
			testObject3.value should be ( 30 )
		}
		
		it( "should be not modifiable if it is not valid." ) {
			val testObject = new Resetable( 100, 10, false )
			val testObject2 = testObject - 10
			val testObject3 = testObject + 20
			testObject2.value should be ( 10 )
			testObject3.value should be ( 10 )
		}
		
		it( "should be immutable." ) {
			val resetable1 = new Resetable( 10, 10, true )
			val resetable2 = resetable1.isValid_=( false )
			
			resetable1.isValid should be ( true )
			resetable2.isValid should be ( false )
			resetable1 shouldNot be ( resetable2 )
		}
	}
}