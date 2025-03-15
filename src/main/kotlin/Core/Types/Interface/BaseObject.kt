package Core.Types.Interface

import java.time.LocalDateTime

abstract class BaseObject : Searchable{
   val created: LocalDateTime = LocalDateTime.now();
   var modified: LocalDateTime = created
       private set

    protected fun updateModified(){
        modified = LocalDateTime.now();
    }

    abstract override fun search(query: String): Boolean
}