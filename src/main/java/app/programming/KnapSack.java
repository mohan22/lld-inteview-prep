package app.programming;

public class KnapSack {

    public static void main(String[] args) {



    }

    static int func(int[] wt, int[] cost, int n, int w){
        int[] dp = new int[n+1];
        int[] prev = new int[n+1];
        for(int i=1;i<=w;i++){
            for(int j=1;j<=n;j++){
                if(w>=wt[i-1]){
                    dp[j] = Math.min(prev[j],dp[j-1]);
                }
                else
                    dp[j] = dp[j-1];
            }
            prev = dp;
        }
        return dp[n];
    }
}
