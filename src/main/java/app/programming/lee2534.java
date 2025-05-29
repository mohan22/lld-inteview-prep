package app.programming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class lee2534 {
    public static void main(String[] args){
        int[] arrival = {0,1,1,2,4};
        int[] state = {0,1,0,0,1};

        System.out.println(Arrays.toString(getAns(arrival,state)));

    }

    /**
     * list[0]->[[],[]]
     * list[1]->[[2],[1]]
     * list[2]->[[3],[]]
     * list[3]->[[],[]]
     * list[4]->[[],[4]]
     *
     *ans = [0,]
     *
     * @param arrival
     * @param state
     * @return
     */
    static int[] getAns(int[] arrival, int[] state){
        ArrayList<PriorityQueue<Integer>>[] list = new ArrayList[arrival.length];

        for(int i=0;i<arrival.length;i++){
            list[i] = new ArrayList<>();
            list[i].add(new PriorityQueue<>((a,b)->a-b));
            list[i].add(new PriorityQueue<>((a,b)->a-b));
        }

        for(int i=0;i<arrival.length;i++){
            list[arrival[i]].get(state[i]).add(i);
        }

        int[] ans = new int[arrival.length];

        int prev = 1;//0
        for(int i=0;i<ans.length;i++){//1
            if(!list[i].get(prev).isEmpty()){
                ans[list[i].get(prev).poll()]=i;
            }
            else{
                prev = prev^1;
                ans[list[i].get(prev).poll()]=i;
            }

            if(i<ans.length-1){
                list[i+1].get(0).addAll(list[i].get(0));
                list[i+1].get(1).addAll(list[i].get(1));
            }
        }

        return ans;

    }
}
