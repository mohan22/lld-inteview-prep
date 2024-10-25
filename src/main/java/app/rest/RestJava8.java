package app.rest;

import java.net.URL;
import java.net.HttpURLConnection;
public class RestJava8 {
    public static void main(String[] args){

        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/ditto");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int code = con.getResponseCode();
            String resp = new String(con.getInputStream().readAllBytes());
            System.out.println(resp);
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
