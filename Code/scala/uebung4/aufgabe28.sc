//xs<ys
def infix(xs: List[Int], ys: List[Int]) : Boolean = ys match {
  case Nil => false /////////////
  case y::ys => xs match{
    case Nil => ////////////
    case x::xs => if (x==y) infix(xs, ys) else false
  }
}