package app.programming;

public class IslandLakes {

    public static void main(String[] args){
        int[][] grid = new int[][]{
                {0,0,0,0,0},
                {0,1,1,1,0},
                {0,1,0,1,0},
                {0,1,1,1,0},
                {0,0,0,0,0}
        };

        int m = grid.length;
        int n = grid[0].length;
        for(int i=0;i<m;i++){
            if(grid[i][0]==0)
                dfs(grid,i,0);

            if(grid[i][n-1]==0)
                dfs(grid,i,n-1);
        }


        for(int j=0;j<n;j++){
            if(grid[0][j]==0)
                dfs(grid,0,j);
            if(grid[m-1][j]==0)
                dfs(grid,m-1,j);
        }

        int ans=0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(grid[i][j]==0){
                    dfs(grid,i,j);
                    ans++;
                }
            }
        }

        System.out.println("ans== "+ans);
    }



    static void dfs(int[][] grid, int row, int col){
        if(row<0 || row>=grid.length || col<0 || col>=grid[0].length || grid[row][col]==1)
                return;

        int[][] dir = new int[][]{{-1,0},{0,1},{1,0},{0,-1}};
        grid[row][col]=1;
        for(int[] d : dir){
            dfs(grid,row+d[0],col+d[1]);
        }
    }
}
