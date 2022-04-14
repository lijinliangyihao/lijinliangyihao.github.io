package treasure.alg.jianzhioffer;

/**
 * 假设把某股票的价格按照时间先后顺序存储在数组中，请问买卖该股票一次可能获得的最大利润是多少
 * */
public class Offer63 {
    
    /*
    * 这个是最简单的版本，买卖一次
    * 记录之前的最低点和当前比较，然后求个最大值，就是这么简单
    *
    * 虽然很简单但是还是得知道状态转移方程
    * dp[i] = max(dp[i-1], prices[i] - min(cost, prices[i]))
    * 每日最大利润为：前一天的利润，或当前利润减之前的最小值；两者之间的更大值
    *
    * 由于状态值和 dp[i - 1] prices[i] cost 三个相关，用变量 max 代替 dp 列表了
    * */
    public int maxProfit(int[] prices) {
        int max = 0, min = Integer.MAX_VALUE;
        for (int price : prices) {
            if(price < min) min = price;
            max = Math.max(max, price - min);
        }
        return max;
    }
    
}
