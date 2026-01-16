package app.objectorient;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

interface KvStore2{
    void set(String key, String field, String val);
    String get(String key,String field);
}

class ValueWrapper{
    String val;
    Long ttl;
    Long ts;

    public ValueWrapper(String v, Long tl, Long t){
        val  = v;
        ttl = tl;
        ts = t;
    }
}

interface KvStoreWithTtl extends KvStore2{
    void set(String key, String field, String val,Long ts, Long ttl);
    String get(String key,String field,Long ts);

    int snapShotAt(long ts);
    void rollback(long ts);


}

class InMemoryKvStore implements KvStoreWithTtl{
    Map<String, Map<String,ValueWrapper>> store = new HashMap<>();
    TreeMap<Long,Map<String, Map<String,ValueWrapper>>> snapShotMap = new TreeMap<>();
    @Override
    public void set(String key, String field, String val) {
        set(key,field,val,System.currentTimeMillis(),null);
        //store.computeIfAbsent(key,k->new HashMap<>()).put(field,val);
    }

    @Override
    public String get(String key,String field) {
        return get(key,field,System.currentTimeMillis());
//       if(store.get(key)==null)
//           return null;
//       return store.get(key).get(field).get;
    }

    @Override
    public void set(String key, String field, String val, Long ts, Long ttl) {
        store.computeIfAbsent(key,k->new TreeMap<>()).put(field,new ValueWrapper(val,ttl,ts));
    }

    @Override
    public String get(String key, String field, Long ts) {
        if (store.get(key) == null)
            return null;
        ValueWrapper valueWrapper =  store.get(key).get(field);
        if(valueWrapper==null || isExpired(valueWrapper, ts))
            return null;
        return valueWrapper.val;
    }

    @Override
    public int snapShotAt(long ts) {
        Map<String, Map<String,ValueWrapper>> snapShot = new HashMap<>();
        for(var keyEntry : store.entrySet()){
            for(var fieldEntry : keyEntry.getValue().entrySet()){
                ValueWrapper vw = fieldEntry.getValue();
                if(!isExpired(vw,ts)){
                    snapShot.computeIfAbsent(keyEntry.getKey(), k-> new TreeMap<>())
                            .put(fieldEntry.getKey(),new ValueWrapper(vw.val, vw.ttl, vw.ts));
                }
            }
        }

        snapShotMap.put(ts,snapShot);
        return snapShot.size();


    }

    @Override
    public void rollback(long ts) {
        store = snapShotMap.floorEntry(ts).getValue();
    }

    boolean isExpired(ValueWrapper v, Long curTs){
        if(v.ttl==null)
            return false;

        return curTs > v.ts + v.ttl;
    }
}
public class KeyValueStoreV2 {
    public static void main(String[] args){
        KvStoreWithTtl store = new InMemoryKvStore();
        store.set("aa","bb","cc");
        store.set("xx","yy","zz");
        store.set("pp","qq","rr",100L,5L);
        store.snapShotAt(104);

        store.set("ii","jj","kk",110L,5L);
        System.out.println("Before rollback");
        System.out.println(store.get("ii","jj",112L));

        System.out.println(store.get("aa","bb"));
        System.out.println(store.get("aa","cc"));
        System.out.println(store.get("pp","qq"));
        System.out.println(store.get("ii","jj",103L));
        System.out.println(store.get("pp","11",103L));

        store.rollback(106);
        System.out.println("After rollback");
        System.out.println(store.get("ii","jj",112L));

    }


}
