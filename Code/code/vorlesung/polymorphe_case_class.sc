abstract class MyList[+A]
case object Nil extends MyList[Nothing]
case class List[A] (head:A, tail:MyList[A]) extends MyList[A]


def append[A](xs:MyList[A],ys:MyList[A]) : MyList[A] =
  xs match {
    case Nil => ys
    case List(y,zs) => List(y,append(zs,ys))
  }

append[Int](List(1,Nil),List(2,Nil))