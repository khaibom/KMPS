import scala.annotation.tailrec

def fakIterative(n: Int): Int = {
  var res = 1
  for (i <- 1 to n) { //inklusiv
    res *= i
  }
  res
}
fakIterative(5)

@tailrec
def fakHelp(i: Int, j: Int): Int = i match{
  case 0 => 1
  case 1 => i*j
  case _ => fakHelp(i-1, i*j)
}
def fak(n : Int) :Int = fakHelp(n , 1)

fak(5)