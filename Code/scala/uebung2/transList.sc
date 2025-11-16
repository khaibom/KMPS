abstract class Mylist
case object Null extends Mylist
case class MeinList(head: Int, tail: Mylist) extends Mylist

def transList(xs: List[Int]) : Mylist = xs match {
  case Nil => Null
  case y::ys => MeinList(y, transList(ys))
}

transList(1::2::3::Nil)