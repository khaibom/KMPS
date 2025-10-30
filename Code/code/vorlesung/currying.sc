def sum(f: Int => Int)(a: Int, b: Int): Int = {
  def iter(a: Int, result: Int): Int = {
    if (a>b) result
    else iter(a+1, result+f(a))
  }
  iter(a,0)
}

def pot(a:Int) : Int = {
  def potHelp(a:Int, b: Int) : Int = if (a==0) b else potHelp(a-1, b*2)
  potHelp(a, 1)
}
def sumInts : (Int, Int) => Int = sum(x => x)
def sumSquare = sum(x => x*x)_
def sumPOT = sum(pot)_

sumInts(1,3)
sumSquare(1,3)
sumPOT(1,3)
sum(x=>x*x)(1,3)


