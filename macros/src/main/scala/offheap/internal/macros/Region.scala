package offheap
package internal
package macros

import scala.reflect.macros.whitebox

class Region(val c: whitebox.Context) extends Common {
  import c.universe._
  import c.universe.definitions._

  def open(pool: Tree) = q"???"

  def apply[T: WeakTypeTag](f: Tree)(pool: Tree) = {
    val r = freshVal("r", tpe = RegionClass.toType, value = open(pool))
    val res = fresh("res")
    val body = app(f, q"${r.symbol}")
    q"""
      $r
      val $res =
        try $body
        finally ${r.symbol}.close()
      $res
    """
  }
}
