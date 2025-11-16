def ggTRekursiv(a: Int, b: Int) : Int = if(b==0) a else ggTRekursiv(b, a%b)

def blöckAnz(a: Int, b: Int, c: Int): Int = b match{
  case 0 => c
  case _ => blöckAnz(b, a%b, c+1)
}

blöckAnz(24,36,1)
blöckAnz(48,18,1)
