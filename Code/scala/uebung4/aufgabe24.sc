import scala.annotation.tailrec

def range(a: Int, b: Int) : List[Int] =
{
  @tailrec
  def rangeHelp(a:Int, b:Int, result: List[Int]) : List[Int] = {
    if(a<=b) rangeHelp(a+1, b, result:+a)
    else result
  }
  rangeHelp(a, b, Nil)
}

range(1, 3)



