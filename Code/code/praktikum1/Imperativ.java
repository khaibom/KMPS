package praktikum1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Imperativ {
	static Boolean is_in_album = false;
	static Boolean is_in_track = false;
	static int current_album = 0;
	static int current_track = 0;
	static int current_character = 0;
	
	static String file_path = ("Code/code/praktikum1/alben.xml");
	static ArrayList<Album> albums = new ArrayList<Album>();
	
	public static void main(String[] args) throws IOException {
		byte[] file_contents = Files.readAllBytes(Paths.get(file_path));
		
		while(current_character < file_contents.length){
			if(file_contents[current_character] == '\n' || file_contents[current_character] == '\r' || file_contents[current_character] == '\t'){
				current_character++;
			}
			else if(new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<album>"))){
				current_album = albums.size();
				albums.add(new Album());
				is_in_album = true;
				current_character += 7;
			}
			else if(new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("</album>"))){
				is_in_album = false;
				current_character += 8;
			}
			else if(new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<title>"))){
				current_character += 7;
				int title_length = 0;
				while(file_contents[current_character + title_length] != '<')
					title_length++;
				String title = new String(file_contents, current_character, title_length, StandardCharsets.UTF_8);
				current_character += title_length;
				current_character += 8;
				if(is_in_track)
					albums.get(current_album).tracks.get(current_track).title = title;
				else if(is_in_album)
					albums.get(current_album).title = title;

			}
			else if(new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<artist>"))){
				current_character += 8;
				int artist_length = 0;
				while(file_contents[current_character + artist_length] != '<')
					artist_length++;
				String artist = new String(file_contents, current_character, artist_length, StandardCharsets.UTF_8);
				current_character += artist_length + 9;
				albums.get(current_album).artist = artist;
			}
			else if(new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<rating>"))){
				current_character += 8;
				int rating_length = 0;
				while(file_contents[current_character + rating_length] != '<')
					rating_length++;
				String rating = new String(file_contents, current_character, rating_length, StandardCharsets.UTF_8);
				albums.get(current_album).tracks.get(current_track).rating = Integer.parseInt(rating);
				current_character += rating_length + 9;
			}
			else if(new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<track>"))){
				is_in_track = true;
				current_character += 8;
				current_track = albums.get(current_album).tracks.size();
				albums.get(current_album).tracks.add(new Track());
			}
			else if(new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("</track>"))){
				is_in_track = false;
				current_character += 9;
			}
			else if(new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("<feature>"))){
				current_character += 9;
				int feature_length = 0;
				while(file_contents[current_character + feature_length] != '<')
					feature_length++;
				String feature = new String(file_contents, current_character, feature_length, StandardCharsets.UTF_8);
				albums.get(current_album).tracks.get(current_track).features.add(feature);
				current_character += feature_length + 10;
			}
			else if(new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<length>"))){
				current_character += 8;
				int length_length = 0;
				while(file_contents[current_character + length_length] != '<')
					length_length++;
				String length = new String(file_contents, current_character, length_length, StandardCharsets.UTF_8);
				albums.get(current_album).tracks.get(current_track).length = length;
				current_character += length_length + 9;
			}
			else if(new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("<writing>"))){
				current_character += 9;
				int writing_length =  0;
				while(file_contents[current_character + writing_length] != '<')
					writing_length++;
				String writing = new String(file_contents, current_character, writing_length, StandardCharsets.UTF_8);
				albums.get(current_album).tracks.get(current_track).writers.add(writing);
				current_character += writing_length + 10;
			}
			else if(new String(file_contents, current_character, 6, StandardCharsets.UTF_8).equals(new String("<date>"))){
				current_character += 6;
				int date_length =  0;
				while(file_contents[current_character + date_length] != '<')
					date_length++;
				String date = new String(file_contents, current_character, date_length, StandardCharsets.UTF_8);
				albums.get(current_album).date = date;
				current_character += date_length + 7;
			}
		}
		
		for(int i = 0; i < albums.size(); i++){
			System.out.println(albums.get(i));
		}	
	}

}
