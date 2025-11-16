abstract class BinTree
case object Null extends BinTree
case class Node(value : Int, left:BinTree, right:BinTree) extends BinTree

def append(xs: List[Int], ys: List[Int]) : List[Int] = xs match {
  case Nil => ys
  case x::xs => x::append(xs,ys)
} // = xs.append(ys)
def append2lists(xs: List[Int], ys: List[Int], zs: List[Int]) : List[Int] = append(append(xs, ys), zs)

def präorder(X:BinTree) : List[Int] = X match {
  case Null => Nil
  case Node(value, left, right) => append2lists(value::Nil, präorder(left), präorder(right) )
}

präorder(Node(3,
  Node(1,
    Node(0,Null, Null),
    Node(2,Null, Null)),
  Node(5,
    Node(4,Null,Null),
    Node(6,Null, Null))))
