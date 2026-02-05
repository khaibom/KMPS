// Aufgabe 3
def map(xs: List[Int], f: Int => Int) : List[Int] = xs match {
  case Nil => Nil
  case x::xs => f(x)::map(xs, f)
}

def filter(xs:List[Int], f: Int=>Boolean) : List[Int] = xs match{
  case Nil => Nil
  case x::xs => if(f(x)) x::filter(xs, f) else filter(xs, f)
}

val liste0 = 1::2::3::4::7::8::Nil
map(filter(liste0, x=>(x%2==0)), x=>x/2)


// Aufgabe 2

// a. cbv -  Argument werden vor dem Funktionsaufruf ausgewertet udn als
//    Werte übergeben. Jedes Argument wird genau 1 mal berechnet.
//    cbn -  Argument werden nicht sofort, sondern erst bei ihrer Verwendung
//    im Funktionskörper ausgewertet. Ein Argument kann mehrfach oder gar
//    nicht ausgewertet werden.

// b. c.


// Aufgabe 1
// a. wenn der Ausdruck nur einen rekursiven Aufruf enthält, der am Ende
//    ausgeführt wird.
// b. Man braucht const Platzaufwand, da durch die Tail Rekursion immer
//    der gleiche Stack Frame verwendet werden kann.

def sum(n: Int, result: Int) : Int = n match {
  case 0 => result
  case _ => sum(n-1, result+n)
}

// d. weil beim Ausdruck mit rekursivem Aufruf der Ausdruck nur aus
//    einem Funktionsaufruf besteht