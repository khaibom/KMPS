abstract class BinTree
case object Null extends BinTree
case class Node(value : Int, left:BinTree, right:BinTree) extends BinTree

def filter(xs: List[Int], f: Int=>Boolean): List[Int] = xs match {
  case Nil => Nil
  case x::xs => if(f(x)) x::filter(xs, f) else filter(xs,f)
}



def filter(bintree: BinTree, f: Int=>Boolean): List[Int] = {
  def append(xs: List[Int], ys: List[Int]) : List[Int] = xs match {
    case Nil => ys
    case x::xs => x::append(xs,ys)
  }
  def append3lists(xs: List[Int], ys: List[Int], zs: List[Int]) : List[Int] = append(append(xs, ys), zs)
  def präorder(X:BinTree) : List[Int] = X match {
    case Null => Nil
    case Node(value, left, right) => append3lists(value::Nil, präorder(left), präorder(right) )
  }
  def tree_to_list(xs:BinTree) :List[Int] = präorder(xs)

  def filter_help(xs: List[Int], f: Int=>Boolean): List[Int] = xs match {
    case Nil => Nil
    case x::xs => if(f(x)) x::filter_help(xs, f) else filter_help(xs,f)
  }
  filter_help(tree_to_list(bintree), f)
}


filter(Node(3,
  Node(1,
    Node(0,Null, Null),
    Node(2,Null, Null)),
  Node(5,
    Node(4,Null,Null),
    Node(6,Null, Null))),
  x=>(x%2==0))