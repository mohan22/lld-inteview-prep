package app.example;

import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class SkipListTest {

    public static void main(String[] args){
        ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>((a,b)-> a-b);//4 3 2 1

        set.add(1);
        set.add(2);
        set.add(5);
        set.add(7);

        Set<Integer> s = set.headSet(4);
        System.out.println(s);
        s.clear();

        System.out.println(set.size());

        TreeMap<Integer,Integer> tm = new TreeMap<>();

        tm.headMap(10);
        tm.tailMap(10);
    }
}
