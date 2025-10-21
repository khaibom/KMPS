import scala.io.Source

case class Track(title: String, length: String, rating: Int, features: List[String], writers: List[String])
case class Album(title: String, date: String, artist: String, tracks: List[Track])

val source : List[Char] = Source.fromFile("D:\\FH Aachen\\Sem 5\\KMPS\\Code\\code\\praktikum2\\alben.xml").toList
val source_test : List[Char] = Source.fromFile("D:\\FH Aachen\\Sem 5\\KMPS\\Code\\code\\praktikum2\\albentest.xml").toList

def findToken(xs: List[Char], isInATag: Boolean, isContent: Boolean, token: String, tokenList: List[String]) : List[String] = xs match{
  case Nil => tokenList
  case y::ys => y match{
    case '\n' | '\r' | '\t' => isContent match{
                                      case true => findToken(ys, isInATag, false, "", tokenList:+token)
                                      case false => findToken(ys, isInATag, false, "", tokenList)
    }
    case '<' => isContent match{
      case false =>findToken(ys, true, false, "", tokenList)
      case true => findToken(ys, true, false, "", tokenList:+token)
    }
    case '>' => findToken(ys, false, false, "", tokenList:+token)
    case '/' => isInATag match{
                case true  => findToken(ys, isInATag, isContent, "/", tokenList)
                case false => findToken(ys, isInATag, isContent, token, tokenList)
    }
    case ' ' => isContent match{
                case true => findToken(ys, isInATag, isContent, token+y, tokenList)
                case false => findToken(ys, isInATag, isContent, token, tokenList)
    }
    case _ =>  findToken(ys, isInATag, true, token+y, tokenList)
  }
}

def createTokenList(source: List[Char]) : List[String] = source match{
  case Nil => Nil
  case _ => findToken(source, isInATag = false, isContent = false, "", Nil)
}

//var tokenList_test : List[String] = createTokenList(source_test)
var tokenList : List[String] = createTokenList(source)
