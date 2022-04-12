package treasure.jianzhioffer;

/**
 * 在一个 m*n 的棋盘的每一格都放有一个礼物，每个礼物都有一定的价值（价值大于 0）。
 * 你可以从棋盘的左上角开始拿格子里的礼物，并每次向右或者向下移动一格、直到到达棋盘的右下角。
 * 给定一个棋盘及其上面的礼物的价值，请计算你最多能拿到多少价值的礼物
 *
 * */
public class Offer47 {
    
    /**
     * 倒是蒙出来了。。。能否状态压缩？
     * */
    public static class MyAnswer {
        
        public int maxValue(int[][] grid) {
            if(grid.length == 0 || grid[0].length == 0) return 0;
            int m = grid.length, n = grid[0].length;
            int[][] dp = new int[m][n];
            dp[0][0] = grid[0][0];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int left = 0, up = 0;
                    if(i > 0) left = dp[i - 1][j];
                    if(j > 0) up = dp[i][j - 1];
                    dp[i][j] = Math.max(left, up) + grid[i][j];
                }
            }
            return dp[m - 1][n - 1];
        }
    }
    
    /**
     * 牛逼，第一次自主实现了状态压缩！
     * */
    public static class CompressState {
        
        public int maxValue(int[][] grid) {
            if(grid.length == 0 || grid[0].length == 0) return 0;
            int m = grid.length, n = grid[0].length;
            int[] dp = new int[n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int up = dp[j];
                    int left = j > 0 ? dp[j - 1] : 0;
                    dp[j] = Math.max(up,left) + grid[i][j];
                }
            }
            return dp[n - 1];
        }
    }

}
