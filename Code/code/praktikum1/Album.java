package praktikum1;

import java.util.ArrayList;

public class Album {
	public String title;
	public String date;
	public String artist;
	public ArrayList<Track> tracks = new ArrayList<Track>();
	
	@Override
	public String toString() {
	  return "Album:\n\tTitle:" + title + "\n\tDate:" + date + "\n\tArtist:" + artist + "\n\tTracks:" + tracks.toString();
	}
}
