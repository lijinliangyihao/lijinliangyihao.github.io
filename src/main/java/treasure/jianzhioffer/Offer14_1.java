package treasure.jianzhioffer;

/**
 * 这道题是真滴难！
 * 割绳子
 * */
public class Offer14_1 {
    
    
    /*
    设数组dp记录0 ~ n 剪绳子的最大乘积
    两层遍历：第一层表示绳子的长度 第二层用来表示第一段减去的长度。要想求最大值，有两种情况：
        剪绳子：剪绳子的话乘积就是 j * dp[i - j] 减去第一段的长度 * 剩下长度的最大值
        剪第一段，第二段不剪，直接 j * (i - j ) 当前的长度 * 剩下的长度
        不剪 dp[i]
    * */
    public int cuttingRope(int n) {
        int[] dp = new int[n + 1];
        dp[2] = 1;
        for(int i = 3; i < n + 1; i++){
            for(int j = 2; j < i; j++){
                // 我的疑问是这个 dp[i - j] 不已经是最优了吗？
                // 它在那个长度是最优的，因为他必须剪一刀；在当前长度可就不一定是最优了，所以得 max 两次
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), j * dp[i - j]));
            }
        }
        return dp[n];
    }
    
}
