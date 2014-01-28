package models

import scala.collection.mutable.HashMap

case class Task( id : Long, label : String )

object Task {
	
	var taskMap = new HashMap[ Long, Task ]
	var nextId : Long = 0
	
	def all() : List[ Task ] = taskMap.values.toList
	
	def create( label : String ) {
		val task = new Task( nextId, label )		
		taskMap += ( nextId -> task )
		nextId += 1
	}
	
	def delete( id : Long ) {
		taskMap -= id
	}
}