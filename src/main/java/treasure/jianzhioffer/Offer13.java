package treasure.jianzhioffer;

import java.util.LinkedList;
import java.util.Queue;

public class Offer13 {
    
    /**
     * 基于 bfs 的，这两个都效率不高，不知道为啥
     * */
    public static class SolutionBfs {
        
        public int movingCount(int m, int n, int k) {
            boolean[][] visited = new boolean[m][n];
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[] {0, 0});
            int sum = 1;
            visited[0][0] = true;
            int[][] dirs = {{0, 1}, {1, 0}};
            while(!queue.isEmpty()) {
                int[] coor = queue.poll();
                int x = coor[0];
                int y = coor[1];
                
                /*
                * 在这犯了个大错误，写了 x += ... 导致第二次循环，x 的值变了
                * */
                
                /*
                * stupid！
                * */
                for (int i = 0; i < 2; i++) {
                    int tx = dirs[i][0] + x;
                    int ty = dirs[i][1] + y;
                    if(tx < 0 || tx >= m || ty < 0 || ty >= n || visited[tx][ty] || !valid(tx, ty, k)) continue;
                    queue.offer(new int[] {tx, ty});
                    sum++;
                    visited[tx][ty] = true;
                }
            }
            return sum;
        }
        
        boolean valid(int i, int j, int k) {
            return sumBit(i) + sumBit(j) <= k;
        }
        int sumBit(int i) {
            int res = 0;
            while(i > 0) {
                res += i % 10;
                i /= 10;
            }
            return res;
        }
    }
    
    public static class SolutionDp {
        /**
         * 虽然花了一些时间但是搞出来了，也算
         * */
        public int movingCount(int m, int n, int k) {
            boolean[][] dp = new boolean[m][n];
            dp[0][0] = true;
            int sum = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if(valid(i, j, k)) {
                        if(i > 0) {
                            dp[i][j] |= dp[i - 1][j];
                        }
                        if(j > 0) dp[i][j] |= dp[i][j - 1];
                        if(dp[i][j]) {
                            sum++;
                        }
                    }
                }
            }
            return sum;
        }
    
        boolean valid(int i, int j, int k) {
            return sumBit(i) + sumBit(j) <= k;
        }
        int sumBit(int i) {
            int res = 0;
            while(i > 0) {
                res += i % 10;
                i /= 10;
            }
            return res;
        }
    }

}
