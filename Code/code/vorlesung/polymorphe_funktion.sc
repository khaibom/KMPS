def map[A,B](xs:List[A], f: A => B) : List[B] = xs match {
  case Nil => Nil
  case y::ys => f(y)::map(ys, f)
}

map[Int, String] (1::2::Nil, _ => "Hi")
