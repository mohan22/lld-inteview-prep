package org.example;


/**
 *
 * Implement key-value store with
 * 1)SET key field value
 * 2)GET key field
 * 3)SET key field value ts ttl --> sets key at ts time with TTL
 * 4)GET key field ts --> Get field at ts time
 * Solution - https://chat.openai.com/share/c28ba346-b784-4fc0-b22a-7e7d7764d31e
 *
 *
 */


import java.util.*;
public class KeyValueStore {
    static Map<String, Map<String, List<String>>> db = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        insert("key1","f1", "v1");
        insert("key1","f2", "v2",String.valueOf(new Date().getTime()));
        insert("key1","f3", "v3",String.valueOf(new Date().getTime()),String.valueOf(5000));

        System.out.println(get("key1","f1",String.valueOf(new Date().getTime())));
        System.out.println(get("key1","f2",String.valueOf(new Date().getTime())));
        System.out.println(get("key1","f3",String.valueOf(new Date().getTime())));
        Thread.sleep(4000);
        System.out.println(get("key1","f3",String.valueOf(new Date().getTime())));
    }


    static void insert(String key, String field, List<String> value){
        db.putIfAbsent(key,new TreeMap<>());
        db.get(key).put(field, value);
    }

    static void insert(String key, String field, String value){
        insert(key,field,Arrays.asList(value));
    }

    static void insert(String key,String field, String value, String ts){
        insert(key,field,Arrays.asList(value,ts));
    }

    static void insert(String key,String field, String value, String ts, String ttl){
        long exp = Long.parseLong(ts) + Long.parseLong(ttl);
        insert(key,field,Arrays.asList(value,ts,String.valueOf(exp)));
    }

    static String get(String key, String field, String ts){
        if(!db.containsKey(key) || !db.get(key).containsKey(field) || isExpired(key,field,ts))
            return "oops";

        return db.get(key).get(field).get(0);
    }

    static boolean isExpired(String key,String field,String ts){
        List<String> list = db.get(key).get(field);
        if(list.size()<3)
            return false;
        long curTime = Long.parseLong(ts);
        long exp = Long.parseLong(list.get(2));

        return curTime >= exp;
    }
}
