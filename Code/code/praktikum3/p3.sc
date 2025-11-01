import com.sun.tools.javac.tree.DCTree.isBlank

import java.lang.Character.{isUpperCase, toUpperCase}
import scala.io.Source

case class Track(title: String, length: String, rating: Int, features: List[String], writers: List[String])
case class Album(title: String, date: String, artist: String, tracks: List[Track])

val source : List[Char] = Source.fromFile("D:\\FH Aachen\\Sem 5\\KMPS\\Code\\code\\praktikum2\\alben.xml").toList
//P2:
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

def parseTrackHelp(xs: List[String], title: String, length: String, rating:Int, features: List[String], writers: List[String]) : Track = xs match {
  case Nil => Track(title, length, rating, features, writers)
  case "/track"::ys => Track(title, length, rating, features, writers)
  case "title"::y::"/title"::ys => parseTrackHelp(ys, y, length, rating, features, writers)
  case "length"::y::"/length"::ys => parseTrackHelp(ys, title, y, rating, features, writers)
  case "rating"::y::"/rating"::ys => parseTrackHelp(ys, title, length, y.toInt, features, writers)
  case "feature"::y::"/feature"::ys => parseTrackHelp(ys, title, length, rating, features:+y, writers)
  case "writing"::y::"/writing"::ys => parseTrackHelp(ys, title, length, rating, features, writers:+y)
  case y::ys => parseTrackHelp(ys, title, length, rating, features, writers)
}
def parseTrack(xs: List[String]) : Track = parseTrackHelp(xs, "", "", 0, Nil, Nil)
def parseAlbumHelp(xs: List[String], title:String, date:String, artist:String, tracks: List[Track], isInTrack: Boolean) : Album = xs match {
  case Nil => Album(title, date, artist, tracks)
  case "/album"::ys => Album(title, date, artist, tracks)
  case "title"::y::"/title"::ys => isInTrack match{
    case true => parseAlbumHelp(ys, title, date, artist, tracks, isInTrack)
    case false=> parseAlbumHelp(ys, y, date, artist, tracks, isInTrack)
  }
  case "date"::y::"/date"::ys => parseAlbumHelp(ys, title, y, artist, tracks, isInTrack)
  case "artist"::y::"/artist"::ys => parseAlbumHelp(ys, title, date, y, tracks, isInTrack)
  case "track"::ys => parseAlbumHelp(ys, title, date, artist, tracks:+parseTrack(ys), true)
  case "/track"::ys => parseAlbumHelp(ys, title, date, artist, tracks, false)
  case y::ys => parseAlbumHelp(ys, title, date, artist, tracks, isInTrack)
}
def parseAlbum(xs: List[String]) : Album = parseAlbumHelp(xs, "", "", "", Nil, false)

def createAlbumList(xs: List[String], albumList: List[Album]): List[Album] = xs match{
  case Nil => albumList
  case "album"::ys => createAlbumList(ys, albumList:+parseAlbum(ys))
  case y::ys => createAlbumList(ys, albumList)
}
val albumList : List[Album] = createAlbumList(tokenList, Nil)


//P3
//1
def poly_map[A,B](xs:List[A], f: A => B) : List[B] = xs match {
  case Nil => Nil
  case y::ys => f(y)::poly_map(ys, f)
}
def titleAlbumToUpper(x: Album) : Album = x.copy(title = x.title.toUpperCase())

def titleTrackToUpperHelp(x: Track) : Track = x.copy(title = x.title.toUpperCase())
def titleTrackToUpper(x: Album) : Album = x.copy(tracks = poly_map[Track, Track](x.tracks, titleTrackToUpperHelp))

def getTrackLength(x:Album) : List[String] = poly_map[Track, String](x.tracks, y=>y.length)

albumList
val b1 = poly_map[Album, Album](albumList, titleAlbumToUpper)
val c1 = poly_map[Album, Album](b1, titleTrackToUpper)
val e1 = poly_map[Album, List[String]](c1, getTrackLength)

//2

def filter[A](xs:List[A], f: A => Boolean) : List[A] = xs match {
  case Nil => Nil
  case y::ys => f(y) match{
    case true => y::filter(ys, f)
    case false => filter(ys, f)
  }
}
val get_second_tracklist = c1 match{
  case Nil => Nil
  case _::x::xs => x.tracks
}
val getTracksRatingBiggerThan4 = filter[Track](get_second_tracklist, x => x.rating >= 4)
val b2 = poly_map[Track, String](getTracksRatingBiggerThan4, x => x.title)

def findTitleFromRTTracks(albumList: List[Album]) : List[String] = {
  def containsRT(writerList: List[String]) : Boolean = writerList match{
    case Nil => false
    case y::ys => y=="Rod Temperton" match{
      case true => true
      case false => containsRT(ys)
    }
  }
  def filterRT(trackList: List[Track]) : List[Track] = filter[Track](trackList, x => containsRT(x.writers))
  def getTitels(trackList: List[Track]) : List[String] = poly_map[Track, String](trackList, x => x.title)
  def append(xs: List[String], ys: List[String]) : List[String] = xs match {
    case Nil => ys
    case x::xs => x::append(xs,ys)
  }
  albumList match{
    case Nil => Nil
    case x::xs => append(getTitels(filterRT(x.tracks)),findTitleFromRTTracks(xs))
  }
}
val c2 = findTitleFromRTTracks(c1)

//3
def partition[A](xs:List[A], f: A => Boolean) : List[List[A]] = {
  def partitionHelp[A](xs:List[A], f: A => Boolean, part: List[A], results: List[List[A]]) : List[List[A]] = xs match{
    case Nil => results:+part
    case y::ys => f(y) match{
      case true => partitionHelp(ys, f, Nil, results:+part)
      case false => partitionHelp(ys, f, part:+y, results)
    }
  }
  partitionHelp(xs, f, Nil, Nil)
}

val a3 = partition[Int](List(1,2,3, 0, 4,5, 0,0, 6, 0), x => x == 0)
val get_second_tracklist = albumList match{
  case Nil => Nil
  case _::x::xs => x.tracks
}
val b3= partition[Track](get_second_tracklist, x => x.title == "Thriller")


def isBlank(s: String): Boolean = s.trim.isEmpty

def createTokenListNew(source: List[Char]) : List[String] = {
  def extractChar(source: List[Char]): List[List[Char]] = partition[Char](source, x => x == '\n' || x == '\r' || x == '\t' || x == '<' || x == '>')
  def filterBlank(xs: List[List[Char]]): List[List[Char]] = filter[List[Char]](xs, x => x!= Nil)
  def makeStringList(xs: List[List[Char]]) : List[String] = xs match{
    case Nil => Nil
    case x::xs => x.mkString("")::makeStringList(xs)
  }
  makeStringList(filterBlank(extractChar(source)))
}
val newTokenList = createTokenListNew(source)

//4
def sum(a: Int, b: Int): Int = a+b
def prod(a: Int, b: Int): Int = a*b

def a4(f1: Int => Int)(f2: (Int, Int) => Int, init: Int)(a: Int, b: Int): Int = {
  def iter(a: Int, result: Int): Int = (a>b) match {
    case true => result
    case false => iter(a+1, f2(result, f1(a)))
  }
  iter(a, init)
}
def sum_4a(a:Int, b:Int) = a4(x=>x)(sum, 0)(a,b)
def prod_4a(a:Int, b:Int) = a4(x=>x)(prod, 1)(a,b)

sum_4a(1,4)
prod_4a(1,4)

a4(x=>x)(_+_, 0)(1,4)
a4(x=>x)(_*_, 1)(1,4)

//4.b.
/*
* iter(1, result)
* iter(2, f2(result, f1(1)))
* iter(3, f2(f2(result, f1(1)), f1(2)))
* iter(4, f2(f2(f2(result, f1(1)), f1(2)), f1(3))
* left-folding
*/
//c.

//d.
def d4(a: Int, b: Int): Int = {
  def map(f1: Int => Int, a: Int, b: Int, result: Int): Int = (a>b) match{
    case true => result
    case false => map(f1, a+1, b, result+a)
  }
}/*
Aufgabe 4:

b. Verwendet Ihre Implementierung aus a. right- oder left-folding? Unter
welchen Umständen würde das jeweils Andere die Funktionalität Ihres
Programms ändern?
c. Wie verhält sich Ihre Implementierung aus a. für einen leeren
Wertebereich? Welches Verhalten wäre in so einem Fall sinvoll?
d. Implementieren Sie die Funktion aus a. ohne Rekursion jedoch unter
Verwendung der Higher-Order-Funktionen map, fold und range auf
Integer-Listen aus Vorlesung und Übung.
*/

