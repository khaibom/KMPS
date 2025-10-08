package praktikum1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Funktional {

    static String file_path = ("Code/code/praktikum1/alben.xml");

    public static void main(String[] args) throws IOException {
        byte[] file_contents = Files.readAllBytes(Paths.get(file_path));
        ArrayList<String> tokens = createTokenList(file_contents, 0, new ArrayList<>());
        System.out.println(tokens);
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

}
