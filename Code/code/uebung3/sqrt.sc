def abs(x: Double): Double = if (x < 0) -x else x
def isTrue(x: Double, sqrtx: Double): Boolean = if (abs(x-sqrtx*sqrtx)<0.00001) true else false

def sqrtHelp(x:Double, y:Double) : Double = if(isTrue(x,y)) y else sqrtHelp(x, (y+x/y)/2)
def sqrt(x: Double) : Double = sqrtHelp(x, 2)

sqrt(2)