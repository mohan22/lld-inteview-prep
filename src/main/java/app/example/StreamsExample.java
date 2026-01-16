package app.example;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Data{
    int size;
    String file;

    public Data(int s, String f){
        size = s;
        file = f;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(" Size = ").append(size);
        sb.append(" file = ").append(file);

        return sb.toString();
    }
}
public class StreamsExample {
    public static void main(String[] args){
        Data d1 = new Data(30,"xx");
        Data d2 = new Data(30,"bb");
        Data d3 = new Data(40,"cc");
        Data d4 = new Data(4,"dd");
        Data d5 = new Data(9,"ee");

        List<Data> l = new ArrayList<>();
        l.add(d1);
        l.add(d2);
        l.add(d3);
        l.add(d4);
        l.add(d5);

        List<Data> ll = l.stream()
                .sorted((a,b)-> {
                    if(a.size!=b.size)
                        return b.size-a.size;
                    return b.file.compareTo(a.file);
                }
                )
                .limit(3)
                .toList();

        System.out.println(ll);


        List<Integer> l2 = Arrays.asList(1,1,1,1,2,2,2,3,4,5,5,5);

        Map<Integer,Long> map = l2.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

        int freq = map.entrySet().stream().toList().stream().sorted((a,b) -> b.getValue().compareTo(a.getValue())).findFirst().get().getKey();

        System.out.println("max count :: " + freq);



    }
}

