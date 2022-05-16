package treasure.alg.top100;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 本题是经典的「NP 完全问题」，
 * 也就是说，如果你发现了该问题的一个多项式算法，那么恭喜你证明出了 P=NP，可以期待一下图灵奖了。
 *
 * 正因如此，我们不应期望该问题有多项式时间复杂度的解法。
 * 我们能想到的，例如基于贪心算法的「将数组降序排序后，依次将每个元素添加至当前元素和较小的子集中」
 * 之类的方法都是错误的，可以轻松地举出反例。
 *
 * 因此，我们必须尝试非多项式时间复杂度的算法，
 * 例如时间复杂度与元素大小相关的动态规划
 *
 * */
public class _416_partition_two_subset {
    
    /**
     * 错误：我以为排序然后从大到小减就行了
     *
     * 但是错了！
     *
     * */
    public static class my_naive_solution {
        
        public boolean canPartition(int[] nums) {
            int sum = IntStream.of(nums).sum();
            if ((sum & 1) == 1) return false;
            int half = sum / 2;
            Arrays.sort(nums);
            int l = 0, r = nums.length - 1;
            while (l < r) {
                if (half >= nums[r])
                    half -= nums[r--];
                else if (half >= nums[l])
                    half -= nums[l++];
                else
                    break;
                if (half == 0)
                    return true;
            }
            return false;
        }
    }
    
    public static class solution {
    
        /**
         * dp[i][j] 表示，0,i 这个区间，如果能选出来一些数字，
         *      注意居然是一些，我草他娘，
         * 和为 j 则为 true
         *
         * 这是咋想到的？？？？？？？
         * 为什么是这个状态？？？？
         *
         * */
        public boolean canPartition(int[] nums) {
            int n = nums.length;
            
            int sum = 0, max = 0;
            for (int num : nums) {
                sum += num;
                if (num > max)
                    max = num;
            }
            
            if (sum % 2 != 0)
                return false;
            
            int half = sum / 2;
            boolean[][] dp = new boolean[n][half + 1];
            
            if (max > half)
                return false;
    
            for (int i = 0; i < n; i++) {
                dp[i][0] = true;
            }
            dp[0][nums[0]] = true;
    
            for (int i = 1; i < n; i++) {
                for (int j = 1; j <= half; j++) {
                    if (j >= nums[i])
                        dp[i][j] = dp[i - 1][j] | dp[i - 1][j - nums[i]];
                    else
                        dp[i][j] = dp[i - 1][j];
                }
            }
            return dp[n - 1][half];
        }
    
        /**
         * 观察到状态只和前两个 i j 相关，一维数组就行
         * 而且，后面的 j 依赖前面的 j 所以得倒着来
         * */
        public boolean canPartition2(int[] nums) {
            int n = nums.length;
        
            int sum = 0, max = 0;
            for (int num : nums) {
                sum += num;
                if (num > max)
                    max = num;
            }
        
            if (sum % 2 != 0)
                return false;
        
            int half = sum / 2;
        
            if (max > half)
                return false;
        
            boolean[] dp = new boolean[half + 1];
            dp[0] = true;
        
            for (int i = 1; i < n; i++) {
                int num = nums[i]; // 要不要 nums[i] ?
                for (int j = half; j >= num; j--) {
                    dp[j] |= dp[j - num];
                }
            }
            return dp[half];
        }
    
    }
    
}
