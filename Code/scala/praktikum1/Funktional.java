package praktikum1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Funktional {

    static String file_path = ("Code/code/praktikum1/alben.xml");

    public static void main(String[] args) throws IOException {
        byte[] file_contents = Files.readAllBytes(Paths.get(file_path));
        ArrayList<String> tokens = createTokenList(file_contents, 0, new ArrayList<>());
        System.out.println(tokens);
        ArrayList<Album> albums = parseFile(new ArrayList<Album>(), tokens, false, false, 0, 0, 0);
        System.out.println(albums);
    }

    public static ArrayList<String> createTokenList(byte[] file_contents, int current_character, ArrayList<String> tokens) {
        if (current_character < file_contents.length) {
            if (file_contents[current_character] == '\n' || file_contents[current_character] == '\r' || file_contents[current_character] == '\t' || file_contents[current_character] == ' ') {
                return createTokenList(file_contents, current_character + 1, tokens);
            } else if (new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<album>"))) {
                tokens.add(new String("album"));
                return createTokenList(file_contents, current_character + 7, tokens);
            } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("</album>"))) {
                tokens.add(new String("/album"));
                return createTokenList(file_contents, current_character + 8, tokens);
            } else if (new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<track>"))) {
                tokens.add(new String("track"));
                return createTokenList(file_contents, current_character + 7, tokens);
            } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("</track>"))) {
                tokens.add(new String("/track"));
                return createTokenList(file_contents, current_character + 8, tokens);
            } else if (new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<title>"))) {
                tokens.add(new String("title"));
                tokens.add(new String(file_contents, current_character + 7, findContentLength(file_contents, current_character+7, 0), StandardCharsets.UTF_8));
                return createTokenList(file_contents, current_character + 7 + findContentLength(file_contents, current_character+7, 0), tokens);
            } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("</title>"))) {
                tokens.add(new String("/title"));
                return createTokenList(file_contents, current_character + 8, tokens);
            } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<length>"))) {
                tokens.add(new String("length"));
                tokens.add(new String(file_contents, current_character + 8, findContentLength(file_contents, current_character+8, 0), StandardCharsets.UTF_8));
                return createTokenList(file_contents, current_character + 8 + findContentLength(file_contents, current_character+8, 0), tokens);
            } else if (new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("</length>"))) {
                tokens.add(new String("/length"));
                return createTokenList(file_contents, current_character + 9, tokens);
            } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<rating>"))) {
                tokens.add(new String("rating"));
                tokens.add(new String(file_contents, current_character + 8, findContentLength(file_contents, current_character+8, 0), StandardCharsets.UTF_8));
                return createTokenList(file_contents, current_character + 8+ findContentLength(file_contents, current_character+8, 0), tokens);
            } else if (new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("</rating>"))) {
                tokens.add(new String("/rating"));
                return createTokenList(file_contents, current_character + 9, tokens);
            } else if (new String(file_contents, current_character, 6, StandardCharsets.UTF_8).equals(new String("<date>"))) {
                tokens.add(new String("date"));
                tokens.add(new String(file_contents, current_character + 6, findContentLength(file_contents, current_character+6, 0), StandardCharsets.UTF_8));
                return createTokenList(file_contents, current_character + 6 + findContentLength(file_contents, current_character+6, 0), tokens);
            } else if (new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("</date>"))) {
                tokens.add(new String("/date"));
                return createTokenList(file_contents, current_character + 7, tokens);
            } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<artist>"))) {
                tokens.add(new String("artist"));
                tokens.add(new String(file_contents, current_character + 8, findContentLength(file_contents, current_character+8, 0), StandardCharsets.UTF_8));
                return createTokenList(file_contents, current_character + 8 + findContentLength(file_contents, current_character+8, 0), tokens);
            } else if (new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("</artist>"))) {
                tokens.add(new String("/artist"));
                return createTokenList(file_contents, current_character + 9, tokens);
            } else if (new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("<writing>"))) {
                tokens.add(new String("writing"));
                tokens.add(new String(file_contents, current_character + 9, findContentLength(file_contents, current_character+9, 0), StandardCharsets.UTF_8));
                return createTokenList(file_contents, current_character + 9 +findContentLength(file_contents, current_character+9, 0), tokens);
            } else if (new String(file_contents, current_character, 10, StandardCharsets.UTF_8).equals(new String("</writing>"))) {
                tokens.add(new String("/writing"));
                return createTokenList(file_contents, current_character + 10, tokens);
            } else if (new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("<feature>"))) {
                tokens.add(new String("feature"));
                tokens.add(new String(file_contents, current_character + 9, findContentLength(file_contents, current_character+9, 0), StandardCharsets.UTF_8));
                return createTokenList(file_contents, current_character + 9 + findContentLength(file_contents, current_character+9, 0), tokens);
            } else if (new String(file_contents, current_character, 10, StandardCharsets.UTF_8).equals(new String("</feature>"))) {
                tokens.add(new String("/feature"));
                return createTokenList(file_contents, current_character + 10, tokens);
            }
        }
        return tokens;
    }
    public static int findContentLength(byte[] file_contents, int current_character, int length) {
        if(file_contents[current_character] != '<'){
            return findContentLength(file_contents, current_character + 1, length + 1);
        }
        else{
            return length;
        }
    }

    public static ArrayList<Album> parseFile(ArrayList<Album> albums, ArrayList<String> tokens, Boolean is_in_album, Boolean is_in_track, int current_album, int current_track, int i) {
        if(i<tokens.size()){
            if(tokens.get(i).equals("album")){
                albums.add(new Album());
                return parseFile(albums, tokens, true, is_in_track, albums.size()-1, current_track, i+1);
            }
            else if(tokens.get(i).equals("/album")){
                return parseFile(albums, tokens, false, is_in_track, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("track")){
                albums.get(current_album).tracks.add(new Track());
                return parseFile(albums, tokens, is_in_album, true, current_album, albums.get(current_album).tracks.size()-1, i+1);
            }
            else if(tokens.get(i).equals("/track")){
                return parseFile(albums, tokens, is_in_album, false, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("title")){
                if(!tokens.get(i+1).equals("/title")){
                    if(is_in_track)
                        albums.get(current_album).tracks.get(current_track).title = tokens.get(i+1);
                    else if(is_in_album)
                        albums.get(current_album).title = tokens.get(i+1);
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+2);
                }
                else{
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
                }
            }
            else if(tokens.get(i).equals("/title")){
                return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("length")){
                if (!tokens.get(i+1).equals("/length")){
                    albums.get(current_album).tracks.get(current_track).length = tokens.get(i+1);
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+2);
                }
                else{
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
                }
            }
            else if(tokens.get(i).equals("/length")){
                return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("rating")){
                if(!tokens.get(i+1).equals("/rating")){
                    albums.get(current_album).tracks.get(current_track).rating = Integer.parseInt(tokens.get(i+1));
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+2);
                }
                else {
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
                }
            }
            else if(tokens.get(i).equals("/rating")){
                return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("date")){
                if(!tokens.get(i+1).equals("/date")){
                    albums.get(current_album).date = tokens.get(i+1);
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+2);
                }
                else{
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
                }
            }
            else if(tokens.get(i).equals("/date")){
                return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("artist")){
                if(!tokens.get(i+1).equals("/artist")){
                    albums.get(current_album).artist = tokens.get(i+1);
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+2);
                }
                else{
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
                }
            }
            else if(tokens.get(i).equals("/artist")){
                return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("writing")){
                if (!tokens.get(i+1).equals("/writing")){
                    albums.get(current_album).tracks.get(current_track).writers.add(tokens.get(i+1));
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+2);
                }
                else{
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);

                }
            }
            else if(tokens.get(i).equals("/writing")){
                return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
            }
            else if(tokens.get(i).equals("feature")){
                if(!tokens.get(i+1).equals("/feature")){
                    albums.get(current_album).tracks.get(current_track).features.add(tokens.get(i+1));
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+2);
                }
                else{
                    return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
                }
            }
            else if(tokens.get(i).equals("/feature")){
                return parseFile(albums, tokens, is_in_album, is_in_track, current_album, current_track, i+1);
            }
        }
        return albums;
    }
}
