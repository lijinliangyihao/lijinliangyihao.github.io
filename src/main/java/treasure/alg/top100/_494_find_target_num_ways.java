package treasure.alg.top100;

import java.util.Arrays;

public class _494_find_target_num_ways {
    
    /**
     * 这个题没想，直接看答案了
     * 理由是。。心情不太好
     * */
    public static class backtrack {
        int count;
        public int findTargetSumWays(int[] nums, int target) {
            helper(nums, target, 0, 0);
            return count;
        }
        void helper(int[] nums, int target, int index, int sum){
            // 这能写成这样也是脑血栓晚期
//            if (index == nums.length && sum == target) {
            if (index == nums.length) {
                if (sum == target)
                    count++;
            } else {
                helper(nums, target, index + 1, sum + nums[index]);
                helper(nums, target, index + 1, sum - nums[index]);
            }
        }
    }
    
    public static class dp {
        /**
         * 这个和 416 非常的像，几乎完全类似，但是有一定差别，只是代码像
         *
         * 假设所有需要加负号，的正数，的和为 neg
         * 所有的 -neg + 正数 = target，正数 = sum - neg
         * 故 sum - 2neg = target, neg = sum - target) / 2
         * 我们就看这个 neg 了，它表示所有的应该加负号的数字的和
         *
         * dp[i][j] i 表示区间， [0,i]，j 表示和为 j，也就是 0 - i 选几个数，和为 j 的个数
         * 初始值，dp[0][0] 表示，0 这个区间，和为 0，有一种情况，啥也不选，所以是 1，这是初始值
         *
         * 再也不装逼了，十来行的代码错了七八处
         *
         * */
        public int findTargetSumWays(int[] nums, int target) {
            int n = nums.length;
    
            int sum = Arrays.stream(nums).sum();
            int neg2 = sum - target;
            if (neg2 % 2 == 1 || neg2 < 0)
                return 0;
            int neg = neg2 / 2;
    
            int[][] dp = new int[n + 1][neg + 1];
            dp[0][0] = 1;
    
            for (int i = 1; i <= n; i++) {
                int num = nums[i - 1];
                for (int j = 0; j <= neg; j++) {
                    dp[i][j] = dp[i - 1][j];
                    if (j >= num)
                        dp[i][j] += dp[i - 1][j - num];
            
                }
            }
            return dp[n][neg];
        }
    
        public int findTargetSumWays2(int[] nums, int target) {
            int n = nums.length;
    
            int sum = Arrays.stream(nums).sum();
            int neg2 = sum - target;
            if (neg2 % 2 == 1 || neg2 < 0)
                return 0;
            int neg = neg2 / 2;
    
            int[] dp = new int[neg + 1];
            dp[0] = 1;
    
            // 小心这个 nums 的下标不要越界！
            for (int i = 0; i < n; i++) {
                for (int j = neg; j >= nums[i] ; j--) {
                    dp[j] += dp[j - nums[i]];
                }
            }
            return dp[neg];
        }
    }
}
