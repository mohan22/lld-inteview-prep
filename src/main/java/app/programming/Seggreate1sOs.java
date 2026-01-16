package app.programming;

import java.util.Arrays;

public class Seggreate1sOs {

    public static void main(String[] args) {
        int[] arr = new int[]{1,1,1,1,0,1};

        int l,r,c;

        l=0;
        r=arr.length-1;
        c=0;
        while(c<r){
            if(arr[c]==0){
                swap(arr,l,c);
                l++;
                c++;
            }
            else{
                swap(arr,r,c);
                r--;
            }
        }

        System.out.println(Arrays.toString(arr));
    }

    static void swap(int[] arr, int l, int r){
        int tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;
    }


}
