import scala.io.Source

case class Track(title: String, length: String, rating: Int, features: List[String], writers: List[String])
case class Album(title: String, date: String, artist: String, tracks: List[Track])

val source : List[Char] = Source.fromFile("D:\\FH Aachen\\Sem 5\\KMPS\\Code\\code\\praktikum2\\alben.xml").toList

// Aufgabe 2:
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
val tokenList : List[String] = createTokenList(source)

// Aufgabe 3:
def parseTrackHelp(xs: List[String], title: String, length: String, rating:Int, features: List[String], writers: List[String]) : Track = xs match {
  case Nil => Track(title, length, rating, features, writers)
  case "title"::y::"/title"::ys => parseTrackHelp(ys, y, length, rating, features, writers)
  case "length"::y::"/length"::ys => parseTrackHelp(ys, title, y, rating, features, writers)
  case "rating"::y::"/rating"::ys => parseTrackHelp(ys, title, length, y.toInt, features, writers)
  case "features"::y::"/features"::ys => parseTrackHelp(ys, title, length, rating, features:+y, writers)
  case "writers"::y::"/writers"::ys => parseTrackHelp(ys, title, length, rating, features, writers:+y)
  case "/track"::ys => Track(title, length, rating, features, writers)
  case y::ys => parseTrackHelp(ys, title, length, rating, features, writers)
}
def parseTrack(xs: List[String]) : Track = parseTrackHelp(xs, "", "", 0, Nil, Nil)

def parseAlbumHelp(xs: List[String], title:String, date:String, artist:String, tracks: List[Track]) : Album = xs match {
  case Nil => Album(title, date, artist, tracks)
  case "title"::y::"/title"::ys => parseAlbumHelp(ys, y, date, artist, tracks)
  case "date"::y::"/date"::ys => parseAlbumHelp(ys, title, y, artist, tracks)
  case "artist"::y::"/artist"::ys => parseAlbumHelp(ys, title, date, y, tracks)
  case "track"::ys => parseAlbumHelp(ys, title, date, artist, tracks:+parseTrack(ys))
  case "/album"::ys => Album(title, date, artist, tracks)
  case y::ys => parseAlbumHelp(ys, title, date, artist, tracks)
}
def parseAlbum(xs: List[String]) : Album = parseAlbumHelp(xs, "", "", "", Nil)

def createAlbumList(xs: List[String], albumList: List[Album]): List[Album] = xs match{
  case Nil => albumList
  case "album"::ys => createAlbumList(ys, albumList:+parseAlbum(ys))
  case y::ys => createAlbumList(ys, albumList)
}

val albumList : List[Album] = createAlbumList(tokenList, Nil)