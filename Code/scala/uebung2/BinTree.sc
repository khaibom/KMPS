abstract class BinTree
  case object Null extends BinTree
  case class Node(value : Int, left:BinTree, right:BinTree) extends BinTree


def anzKnoten(X:BinTree) : Int = X match {
  case Null => 0
  case Node(value, left, right) => 1 + anzKnoten(left) + anzKnoten(right)
}

anzKnoten(Null)
anzKnoten(Node(1,Node(0,Null, Null),Node(3,Node(2,Null,Null),Null)))