def insert(x:Int, xs:List[Int]) : List[Int] = xs match {
  case Nil => x::Nil
  case y::ys => if(x<=y) x::xs else y::insert(x,ys)
}

insert(4, 1::2::6::Nil)

def insSort(xs:List[Int]): List[Int] = xs match {
  case Nil => Nil
  case x::xs => insert(x,insSort(xs))
}

insSort(2::7::1::3::0::6::4::5::Nil)