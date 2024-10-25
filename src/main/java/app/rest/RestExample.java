package app.rest;
import java.util.*;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

class Ability{
    Map<String,String> ability;
    boolean is_hidden;
    int slot;
}

class Resp{
    List<Ability> abilities;
}
public class RestExample {
    public static void main(String[] args){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://pokeapi.co/api/v2/pokemon/ditto"))
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            String resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            Gson gson = new Gson();
            Resp r = gson.fromJson(resp,Resp.class);
           // System.out.println(resp);
            System.out.println(r.abilities.get(1).is_hidden);
        }
        catch(Exception e){
            System.out.print(e);
        }
    }

}
