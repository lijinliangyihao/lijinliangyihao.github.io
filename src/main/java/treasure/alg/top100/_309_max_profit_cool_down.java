package treasure.alg.top100;

public class _309_max_profit_cool_down {
    
    /**
     * 懒得写了，俩字：不会
     *
     * 带冷冻期，现在有三个状态：
     *  0 持有股票
     *  1 没股票，但是在冷冻期
     *  2 没股票，但是不在冷冻期
     *
     * */
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n][3];
        dp[0][0] = -prices[0];
        for (int i = 1; i < n; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][2] - prices[i]);
            dp[i][1] = dp[i - 1][0] + prices[i]; // 今天冷冻期说明昨天必然交易过
            dp[i][2] = Math.max(dp[i - 1][1], dp[i - 1][2]); // 今天不在冷冻期，则昨天可能在也可能不在
        }
        return Math.max(dp[n - 1][1], dp[n - 1][2]);
    }
    
    /**
     * 发现变量之和前一个有关，所以用变量存就行了无需数组
     * */
    public int maxProfit2(int[] prices) {
        int n = prices.length;
        int pf1 = -prices[0], pf2 = 0, pf3 = 0;
        for (int i = 1; i < n; i++) {
            int newPf1 = Math.max(pf1, pf3 - prices[i]);
            int newPf2 = pf1 + prices[i];
            int newPf3 = Math.max(pf2, pf3);
            pf1 = newPf1;
            pf2 = newPf2;
            pf3 = newPf3;
        }
        return Math.max(pf2, pf3);
    }
}
