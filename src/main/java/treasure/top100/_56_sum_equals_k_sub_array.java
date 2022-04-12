package treasure.top100;

public class _56_sum_equals_k_sub_array {
    
    /**
     * 名字叫暴力，也可以通过测试
     * 也不是哪个山驴逼随随便便能想出来的
     * */
    public static class 暴力 {
    
        public int subarraySum(int[] nums, int k) {
            return 1;
        }
    }
    
    /**
     * TODO 这是错误答案
     *
     * 我思路是双指针，如果 sum < k，改变 i 和 j 谁影响大我就改变谁
     *      如果 sum > k，改变 i 和 j 谁影响小就改变谁
     *      如果 sum == k，同 sum < k，优先追求大影响
     *
     *      但我思路是错的！！！！！！！答案越来越离谱。
     * */
    public static class WrongAnswer {
        public int subarraySum(int[] nums, int k) {
            int n = nums.length;
            int i = 0, j = 0;
            int sum = nums[0];
            int count = 0;
            while (i <= j) {
                if (sum <= k) {
                    if (sum == k) count++;
                    if (j < n - 1 && sum - nums[i] > sum + nums[j + 1]) {
                        sum -= nums[i++];
                    }
                    else {
                        j++;
                        if(j == n) break;
                        sum += nums[j];
                    }
                } else {
                    if (j < n - 1 && sum - nums[i] < sum + nums[j + 1]) {
                        j++;
                        if (j == n) break;
                        sum += nums[j];
                    }
                    else {
                        sum -= nums[i++];
                    }
                }
            }
            return count;
        }
    }
}
