package treasure.jianzhioffer;

import java.util.*;

/**
 * 我们把只包含质因子 2、3 和 5 的数称作丑数（Ugly Number）。
 * 求按从小到大的顺序的第 n 个丑数
 *
 * 看了好几遍，动态规划的解释，感觉没有太明白
 *
 *
 * */
public class Offer49 {
    
    public static class ByPriorityQueue {
    
        public int nthUglyNumber(int n) {
            Set<Long> set = new HashSet<>();
            int[] factor = {2, 3, 5};
            PriorityQueue<Long> queue = new PriorityQueue<>();
            set.add(1L);
            queue.offer(1L);
            // 这个得从 0 开始
            for (int i = 0; i < n; i++) {
                long val = queue.poll();
                if(i == n - 1) {
                    return (int)val;
                }
                for (long each : factor) {
                    long mul = val * each;
                    if(set.add(mul)) {
                        queue.offer(mul);
                    }
                }
            }
            return 1;
        }
    }
    
    public static class Dp {
        public int nthUglyNumber(int n) {
            int[] dp = new int[n];
            dp[0] = 1;
            int a = 0, b = 0, c = 0;
            for (int i = 1; i < n; i++) {
                int n1 = dp[a] * 2, n2 = dp[b] * 3, n3 = dp[c] * 5;
                dp[i] = Math.min(n1, Math.min(n2, n3));
                if(dp[i] == n1) a++;
                if(dp[i] == n2) b++;
                if(dp[i] == n3) c++;
            }
            return dp[n - 1];
        }
    }
    
}
