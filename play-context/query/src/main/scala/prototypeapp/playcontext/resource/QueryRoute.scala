package prototypeapp.playcontext.resource

import infrastructure.Resource

trait QueryRoute { self: Resource =>

  val queryRoute =
    pathPrefix("events") {
      pathPrefix("poll") {
        get {
          complete {
            List( ("", "") )
          }
        }
      }
    }

}
