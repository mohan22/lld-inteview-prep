package app.example;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Encodings {
    public static void main(String[] args){
        //String str = "test";
//        Charset.defaultCharset()
        //System.setProperty("file.encoding", "UTF-8");
        System.out.println(Charset.defaultCharset() );
    }
}
