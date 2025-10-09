def ggTRekursiv(a: Int, b: Int) : Int = if(b==0) a else ggTRekursiv(b, a%b)
ggTRekursiv(24,36)