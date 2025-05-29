package app.programming;

public class SuperPrimeCut {
    public static void main(String[] args){
        System.out.println(isSpc(23));
        System.out.println(isSpc(113));
        System.out.println(isSpc(239));
        System.out.println(isSpc(2396));


    }

    private static boolean isSpc(int n) {
        if(n<10){
            return isPrime(n);
        }

        return isPrime(n)&&isSpc(n/10);
    }

    private static boolean isPrime(int n){
        if(n<=1)
            return false;
        int cnt=0;
        for(int i=1;i*i <= n;i++){
            if(n%i == 0)cnt++;
        }

        return cnt==1;
    }

}
