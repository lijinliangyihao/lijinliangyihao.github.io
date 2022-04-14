package treasure.alg.top100;

import java.util.*;

public class _312_poke_the_balloon {
    
    /**
     * 解法 1 自顶向下带记忆化搜索
     *
     * 由于死记硬背写错的地方用注释标出来了
     * */
    public static class TopDown {
    
        int[][] mem;
        int[] val;
        public int maxCoins(int[] nums) {
            int n = nums.length;
            val = new int[n + 2];
            val[0] = val[n + 1] = 1;
            System.arraycopy(nums, 0, val, 1, n);
            mem = new int[n + 2][n + 2];
            return search(0, n + 1);
        }
        int search(int left, int right) {
            if(left + 1 >= right) return 0;
            if(mem[left][right] != 0) return mem[left][right];
            int max = 0;
            for (int i = left + 1; i < right; i++) {
                // 这里得是 left 和 right，写死，不是 i - 1 和 i + 1
                int sum = val[left] * val[i] * val[right];
                // 这两个都是 i，不是 i 和 i + 1
                sum += search(left, i) + search(i, right);
                max = Math.max(max, sum);
            }
            mem[left][right] = max;
            return max;
        }
    }
    
    /**
     * 动态规划属于自底向上，思路和记忆化搜索类似
     * */
    public static class BottomUp {
    
        public int maxCoins(int[] nums) {
            int n = nums.length;
            int[] val = new int[n + 2];
            val[0] = val[n + 1] = 1;
            System.arraycopy(nums, 0, val, 1, n);
            // dp 含义是：开区间 (i, j) 最大硬币数，两个边界都取不到，i + 1 < j 才有数否则是 0
            // 观察到下面 i 最多到 n - 1，耍小聪明让第一个维度 = n，导致数组越界
            int[][] dp = new int[n + 2][n + 2];
            
            // 死记硬背的
            // 完全没搞懂
//            for (int left = 0; left < n + 1; left++) {
//                for (int right = n + 1; right > left + 1; right--) {
//                    int max = 0;
//                    for (int mid = left + 1; mid < right; mid++) {
//                        int sum = val[left] * val[mid] * val[right];
//                        sum += dp[left][mid] + dp[mid][right];
//                        max = Math.max(max, sum);
//                    }
//                    dp[left][right] = max;
//                }
//            }
    
            // 这个遍历顺序令人恐惧！
            /*
            * 为什么这样遍历？肯定是考虑到  sum += ... 那一行
            * 求大区间时，小区间必须已经被求出来了，所以可以看到 i 和 j 一开始几乎是挨着的
            *     通过倒着遍历 i 实现；而 j 还是正着遍历的
            * */
            for (int i = n - 1; i >= 0; i--) {
                for (int j = i + 2; j <= n + 1; j++) {
                    for (int k = i + 1; k < j; k++) {
                        int sum = val[i] * val[k] * val[j];
                        sum += dp[i][k] + dp[k][j];
                        dp[i][j] = Math.max(dp[i][j], sum);
                    }
                }
            }
            
            return dp[0][n + 1];
        }
    }
    
    /**
     * 我想着搞个 treemap 获取最小值，乘
     * 但是如果有重复的就完了，所以放弃了
     *
     * 看了答案感觉我的想法完全狗屁不通
     *
     * */
    public static class MyWrongAnswer {
        /*
         * 3 1 5 2 8
         *
         * 2 80 + 3158 = 80 + 15 + 358
         *                    40 + 318 = 40 + 24 + 38
         *                    15 + 358 = 15 + 120 + 38
         * 1 15 + 3528 = 15 + 80 + 358
         *
         * 3128 = 6 + 328 = 6 + 48 + 38
         *      = 8 + 318 = 8 + 24 + 38
         *
         * 238 = 6 + 24 +8
         *     = 48 + 16 + 8
         * 所以两两挨着的数，先找小的消；隔着的话没所谓了
         * 如果长度大于 3 不能找两变的最小数
         * */
        public int maxCoins(int[] nums) {
            TreeMap<Integer, Integer> map = new TreeMap<>();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < nums.length; i++) {
                list.add(nums[i]);
                map.put(nums[i], i);
            }
            int res = 0;
            while(list.size() > 0) {
                int value = map.firstKey();
                int ind = map.get(value);
                if(list.size() > 2) {
                }
            
            }
            return res;
        }
    }
}
