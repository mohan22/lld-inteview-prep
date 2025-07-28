package app.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternExtract {
    public static void main(String[] args){
        String test = "hello 123 world 45 6";

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(test);


        while(matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
