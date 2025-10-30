def add(xs: List[Int]) :Int = xs match {
  case Nil => 0
  case y::ys => y+add(ys)
}
def map(xs: List[Int], f: Int=>Int): List[Int] = xs match{
  case Nil => Nil
  case x::xs=> f(x)::map(xs, f)
}
def addMap(f: List[Int] => List[Int]): (List[Int]) => Int = xs => add(f(xs))

def addMapSquares = addMap(xs=>map(xs, x=>x*x))
addMapSquares(1::2::3::Nil)