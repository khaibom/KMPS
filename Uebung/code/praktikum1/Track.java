package praktikum1;

import java.util.ArrayList;

public class Track{
	public String title;
	public String length;
	public int rating;
	public ArrayList<String> features = new ArrayList<String>();
	public ArrayList<String> writers = new ArrayList<String>();
	
	
	@Override
	public String toString() {
	  return "\n\tTrack:\n\t\tTitle:" + title + "\n\t\tLength:" + length + "\n\t\tRating:" + rating + "\n\t\tFeatures:" + features.toString() + "\n\t\tWriters:" + writers.toString();
	}
}