package Core.Types

import java.time.LocalDateTime

open class BaseObject {
   val created: LocalDateTime = LocalDateTime.now();
   var modified: LocalDateTime = created
       private set

    protected fun updateModified(){
        modified = LocalDateTime.now();
    }
}