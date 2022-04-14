package treasure.alg.top100;

public class _279_full_square_number {

    /**
     * 这个就直接看答案吧，我做不出来
     * */
    public static class Solution {
    
        public int numSquares(int n) {
            // dp[i] 是 i 这个数最小平方数的个数，
            // 转移方程是 dp[i] = dp[i - j * j] + 1
            int[] dp = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = 1; j * j <= i; j++) {
                    min = Math.min(min, dp[i - j * j]);
                }
                dp[i] = 1 + min;
            }
            return dp[n];
        }
    }
}
