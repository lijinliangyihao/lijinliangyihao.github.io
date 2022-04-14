package treasure.alg.jianzhioffer;

import java.util.Arrays;

/**
 * 把n个骰子扔在地上，所有骰子朝上一面的点数之和为s。
 * 输入n，打印出s的所有可能的值出现的概率。
 *
 * 你需要用一个浮点数数组返回答案，
 * 其中第 i 个元素代表这 n 个骰子所能掷出的点数集合中第 i 小的那个的概率
 *
 * 1
 * 输出: [0.16667,0.16667,0.16667,0.16667,0.16667,0.16667]
 *
 * 2
 * 输出: [0.02778,0.05556,0.08333,0.11111,0.13889,0.16667,0.13889,0.11111,0.08333,0.05556,0.02778]
 *
 * 直接看答案了，看懂答案都费劲
 *
 * */
public class Offer60 {
    
    /**
     *
     * 经过路飞的分析
     * 一个骰子，点数 1-6
     * 两个骰子，点数 2-12，共 11 个，公式为 5n + 1
     *
     * 然后假设 f(1, x) 已知
     * f(1, x) 对 f(2) 每个值的贡献：
     *      (f(1, x) + 1) / 6
     *      (f(1, x) + 2) / 6
     *      ...
     *      (f(1, x) + (6 - x)) / 6
     *
     * 这样把 f(2) 求出来了，如此往复，f(n) 就求出来了
     *
     * 更直观的理解是：fn 每个值，都是从它前面 fn-1 fn-2 ... fn-5 过来的，但是这种需要做判断防止越界
     * 所以反过来从 fn-1 往 fn 累加，这种更好判断一些
     *
     * */
    public static class Luffy {
        
        public double[] dicesProbability(int n) {
            double[] dp = new double[6];
            Arrays.fill(dp, 1.0 / 6);
            for (int i = 2; i <= n; i++) {
                double[] tmp = new double[5 * i + 1];
                for (int j = 0; j < dp.length; j++) {
                    for (int k = 0; k < 6; k++) {
                        tmp[j + k] += dp[j] / 6;
                    }
                }
                dp = tmp;
            }
            return dp;
        }
    }
    
}
