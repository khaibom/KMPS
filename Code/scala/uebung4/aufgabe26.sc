abstract class BinTree
case object Null extends BinTree
case class Node(value : Int, left:BinTree, right:BinTree) extends BinTree


def map_bt(X: BinTree)(f: Int=>Int) :BinTree = X match {
  case Null => Null
  case Node(value, left, right) => Node(f(value), map_bt(left)(f), map_bt(right)(f))
}

def mapt_mul2(X: BinTree) = map_bt(X)(x=>x*2)


mapt_mul2(Node(3,
  Node(1,
    Node(0,Null, Null),
    Node(2,Null, Null)),
  Node(5,
    Node(4,Null,Null),
    Node(6,Null, Null))))