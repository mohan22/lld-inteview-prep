package app.objectorient;

import java.util.HashMap;
import java.util.Map;

class Val{
    long ts;
    long ttl;
    Object value;
    public Val(Object v){
        value = v;
    }

    public Val(Object v, long curTime, long exp){
        value = v;
        ts = curTime;
        ttl = exp;
    }

    public String toString(){
        return "Val [ ts= "+ ts + " ttl= "+ ttl+ " value= "+ value + " ]";
    }
}

public class KvStore {
        Map<String, Map<String,Val>> store = new HashMap<>();

        public void set(String key, String field, Object value){
            store.putIfAbsent(key,new HashMap<>());
            Val val = new Val(value);
            store.get(key).put(field,val);
        }

        public void set(String key, String field, Object value, long curTime, long exp){
            store.putIfAbsent(key,new HashMap<>());
            Val val = new Val(value,curTime,exp);
            store.get(key).put(field,val);
        }

        Val get(String key, String field){
            if(!store.containsKey((key)))
                return null;

            return store.get(key).get(field);
        }

        Val get(String key, String field,long ts){
            if(!store.containsKey((key)) || store.get(key).get(field)==null)
                return null;

            if(store.get(key).get(field).ttl>=ts)
                return store.get(key).get(field);

            return null;

        }



        public static void main(String[] args){
            KvStore kvStore = new KvStore();

            kvStore.set("key1","field1","value1");
            kvStore.set("key1","field2","value2");

            System.out.println(kvStore.get("key1","field1").value);

            kvStore.set("key2","field1","value1", System.currentTimeMillis(),System.currentTimeMillis()+1000);

            System.out.println(kvStore.get("key2","field1",System.currentTimeMillis()));
            System.out.println(kvStore.get("key2","field1",System.currentTimeMillis()+5000));
        }

}
