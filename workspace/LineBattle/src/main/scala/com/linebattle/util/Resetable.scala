package com.linebattle.util

case class Resetable( maximum : Int, startValue : Int, valid : Boolean = true ) {
	
	def value = startValue
	def value_= ( newValue : Int ) : Resetable = copy( startValue = Math min( newValue, maximum ) )
	
	def isValid = valid
	def isValid_= ( newValid : Boolean ) : Resetable = copy( valid = newValid )
	
	def reset : Resetable = copy( startValue = maximum )
	
	def >=( other : Int ) : Boolean = value >= other
	def <=( other : Int ) : Boolean = value <= other
	
	def -( modifier : Int ) : Resetable = {
		if( valid ) {
			copy( startValue = value - modifier )
		}
		else this
	}
	
	def +( modifier : Int ) : Resetable = {
		if( valid ) {
			copy( startValue = Math min( value + modifier, maximum ) )
		}
		else this
	}
	
	override def toString:String = {
		if(valid){
			"[" + value.toString + "/" + maximum.toString + "]"
		}
		else {
			"[invalid]"
		}
	}
}

object Resetable {
	implicit def applyInteger( value : Int ) : Resetable = new Resetable( value, value )
	implicit def applyBoolean( isValid : Boolean ) : Resetable = new Resetable( 0, 0, isValid )
}