def sumInts(a: Int, b: Int): Int = if (a > b) 0 else a + sumInts(a + 1, b)
sumInts(1,3)

def square(i: Int) : Int = i*i
def sumSquares(a: Int, b: Int): Int = if (a > b) 0 else square(a) + sumSquares(a + 1, b)
sumSquares(6,8)

def pot(a: Int) : Int = a match{
  case 0 => 1
  case _ => 2*pot(a-1)
}
def sumPOT(a: Int, b: Int): Int = if (a > b) 0 else pot(a) + sumPOT(a + 1, b)
sumPOT(2, 3)