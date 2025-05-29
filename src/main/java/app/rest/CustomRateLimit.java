package app.rest;

import java.util.*;

//https://leetcode.com/discuss/post/5576519/deliveroo-oa-by-anonymous_user-m6ef/
public class CustomRateLimit {
    static class Pair{
        int time;
        String msg;

        Pair(int t, String s){
            time = t;
            msg = s;
        }
    }
    public static void main(String[] args){
        //It is given that times list is sorted
        myVersion();
        System.out.println("=======================");
        simplerVersion();

    }

    static void myVersion(){
        List<Integer> times = Arrays.asList(1, 4, 5, 10, 11, 14);
        List<String> msgs = Arrays.asList("hello", "bye", "bye", "hello", "bye", "hello");
        int k = 6;

        Deque<Pair> dq = new ArrayDeque<>();
        Set<String> set = new HashSet<>();
        for(int i=0;i<times.size();i++){

            while(!dq.isEmpty() && times.get(i) - dq.peekFirst().time > k){
                String s = dq.peekFirst().msg;
                dq.pollFirst();
                set.remove(s);
            }

            if(set.contains(msgs.get(i))){
                System.out.println(msgs.get(i) + " " + false);
            }
            else{
                System.out.println(msgs.get(i) + " " + true);
                dq.addLast(new Pair(times.get(i),msgs.get(i)));
                set.add(msgs.get(i));
            }
        }
    }

    static void simplerVersion(){
        List<Integer> times = Arrays.asList(1, 4, 5, 10, 11, 14);
        List<String> msgs = Arrays.asList("hello", "bye", "bye", "hello", "bye", "hello");
        int k = 6;

        Map<String,Integer> map = new HashMap<>();

        for(int i=0;i< times.size();i++){
            String s = msgs.get(i);

            if(!map.containsKey(s) || times.get(i) - map.get(s) > k){
                map.put(s, times.get(i));
                System.out.println(s + " " + true);
            }
            else{
                System.out.println(s + " " + false);
            }
        }
    }
}
