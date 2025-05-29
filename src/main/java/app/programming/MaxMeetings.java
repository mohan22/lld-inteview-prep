package app.programming;

import java.util.Arrays;

public class MaxMeetings {

    public static void main(String[] args){
        int[][] arr = new int[][]{{1,2},{4,3},{9,1},{5,7}};

        Arrays.sort(arr,(a,b)->a[1]-b[1]);

        for(int[] a : arr){
            System.out.println(a[0] + " " + a[1]);
        }

    }
}
