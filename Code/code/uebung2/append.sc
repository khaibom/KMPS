def append(xs: List[Int], ys: List[Int]) : List[Int] = xs match {
  case Nil => ys
  case x::xs => x::append(xs,ys)
}

append(1::2::3::Nil, 4::5::6::Nil)