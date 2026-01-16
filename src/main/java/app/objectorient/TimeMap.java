package app.objectorient;

import com.sun.source.tree.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TimeMap {
    public static void main(String[] args){
        Map<Integer,Map<Integer, Map<Integer,String>>> map = new HashMap<>();

        map.put(1,new HashMap<>());
        map.get(1).put(11,new TreeMap<>());
        map.get(1).get(11).put(1000,"s1");
        map.get(1).get(11).put(1001,"s2");
        map.get(1).get(11).put(99,"s3");
        Map<Integer,Map<Integer,String>> map2 = map.get(1);
        TreeMap<Integer,String> tm = (TreeMap<Integer, String>) map2.get(11);
        for(String s : tm.values()){
            System.out.println(s);
        }

        System.out.println(tm.lastEntry().getValue());

        int givenTs = 150;

        System.out.print(tm.floorEntry(150).getValue());

    }
}
