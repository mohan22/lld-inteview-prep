package app.languageexamples;

import com.sun.source.tree.Tree;

import java.util.*;

public class VehicleUtil {
    TreeMap<Integer, List<String>> map = new TreeMap<>();

    void recordValue(int timestamp, String intersection){
        map.computeIfAbsent(timestamp,k-> new ArrayList<>()).add(intersection);
    }



    public static void main(String[] args){
        VehicleUtil vehicleUtil = new VehicleUtil();
        vehicleUtil.recordValue(1,"a");
        vehicleUtil.recordValue(1,"b");
        vehicleUtil.recordValue(2,"a");
        vehicleUtil.recordValue(3,"a");
        vehicleUtil.recordValue(2,"b");

        System.out.println(vehicleUtil.getCount(1,2,"a"));
        System.out.println(vehicleUtil.getCount(1,2,"b"));



    }

    private int getCount(int startTime, int endTime, String intersection) {
        int ans=0;
        for(Map.Entry<Integer,List<String>> entry : map.subMap(startTime,true,endTime,true).entrySet()){
            ans += Collections.frequency(entry.getValue(),intersection);
        }

        return ans;
    }
}
