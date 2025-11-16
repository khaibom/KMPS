def präfix(xs: List[Int], ys: List[Int]) : Boolean = xs match {
  case Nil => if(ys==Nil) true else false
  case x::x1s => ys match {
    case Nil => false
    case y::y1s => if(x==y) präfix(x1s, y1s) else false
  }
}

präfix(1::2::3::Nil, 1::2::3::Nil)
präfix(1::Nil, Nil)
präfix(Nil,Nil)
präfix(1::2::3::Nil, 1::3::Nil)
