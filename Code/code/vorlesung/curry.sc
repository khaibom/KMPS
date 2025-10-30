def sum(f: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0 else f(a) + sum(f,a + 1, b)
def id(x: Int): Int = x
def square(x: Int): Int = x * x
def cube(x: Int): Int = x * x * x


def sumCurry1(f: Int => Int) (a: Int, b: Int) : Int = sum(f,a,b)

def sumCurry2(f: Int => Int): (Int,Int) => Int = (a,b) => sum(f,a,b)
def sumCurry3(f: Int => Int): (Int,Int) => Int = sum(f,_,_)
def sumPA1(f: Int => Int): (Int,Int) => Int = sumCurry1(f)


def sumInts = sumCurry1(id)_
def sumSquares = sumCurry2(square)
def sumCubes =  sumCurry3(cube)
def sumCubesPA = sumPA1(cube)

sumInts(1,3)
sumSquares(1,3)
sumCubes(1,3)
sumCubesPA(1,3)