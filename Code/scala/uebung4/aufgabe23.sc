def foldl(xs: List[Int], init: Int) (f: (Int , Int) => Int) :Int = xs match {
  case Nil => init
  case x::xs => foldl(xs, f(init,x))(f)
}

def add(x:Int, y:Int) = x+y
def mul(x:Int, y:Int) = x*y

def foldl_add(xs:List[Int]) = foldl(xs, 0)(add)
def foldl_mult(xs:List[Int]) = foldl(xs,1)(mul)

foldl_add(1::2::3::4::Nil)
foldl_mult(1::2::3::4::Nil)