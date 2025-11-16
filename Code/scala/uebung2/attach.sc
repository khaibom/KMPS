def attach(x:Int, xs:List[Int]) : List[Int] = xs match {
  case Nil => x::Nil
  case y::ys => y::attach(x,ys)
}

attach(0,Nil)
attach(1,2::3::Nil)