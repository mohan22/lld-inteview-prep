package app.rest;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.HttpResource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ApITesting {

    static class Info{
        int count;
        int pages;

    }
    static class Result{
        int id;

        String species;
        String name;

        String gender;


        List<String> episode;
    }
    static class Response{
        Info info;
        List<Result> results;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        String base = "https://rickandmortyapi.com/api/character";
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        int page=1;
        List<String> ans = new ArrayList<>();
        Response resp;
        do{
            String url = buildUrl(base,page);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            resp = gson.fromJson(response.body(), Response.class);
            addToAns(ans,resp);
            page++;

        }while(page<=resp.info.pages);

        for(String s : ans){
            System.out.println(s);
        }

    }

    static String buildUrl(String base, int page){
        if(page==1)
            return base;
        return base + "?page="+page;
    }

    static void addToAns(List<String> ans , Response resp){
        //name + type + episode numbers
        List<Result> res = resp.results.stream().filter(r->r.species.equals("Alien")).collect(Collectors.toList());
        for(Result r : res){
            String s = r.name + ", " + r.gender +", "+r.species+", " + getEpisodeNums(r.episode);
            ans.add(s);
        }

        //return s;
    }

    static String getEpisodeNums(List<String> episodes){
        List<Integer> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String e : episodes){
           // System.out.println(e);
            Matcher matcher = Pattern.compile("[0-9]+").matcher(e);
            if(matcher.find()) {
                sb.append(matcher.group());
                sb.append(",");
            }
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }


}
