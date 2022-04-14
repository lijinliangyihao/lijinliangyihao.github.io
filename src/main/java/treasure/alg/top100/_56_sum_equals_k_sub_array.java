package treasure.alg.top100;

import java.util.HashMap;
import java.util.Map;

public class _56_sum_equals_k_sub_array {
    
    /**
     * 名字叫暴力，也可以通过测试
     * 可以看到暴力方法其实非常的简洁
     * */
    public static class BaoLi {
    
        public int subarraySum(int[] nums, int k) {
            int n = nums.length;
            int count = 0;
            for (int end = 0; end < n; end++) {
                int sum = 0;
                for (int start = end; start >= 0; start--) {
                    sum += nums[start];
                    if (sum == k) {
                        count++;
                    }
                }
            }
            return count;
        }
    }
    
    /**
     * 使用 map 对暴力解法进行了一些优化！
     * */
    public static class Mapped {
        /**
         * 看着不长，其实不是那么好理解的
         * 用到了一个等式：nums[i] - nums[j - 1] = k
         *
         * 本来有一个前缀和数组的，pre[]，结果只用到了前一个值，优化成一个变量 pre 了
         *
         * map 里存的是，前缀和出现的次数
         *      什么意思呢？比如一个数组长度 10，1-3 和 1-6 的和都是 5，那 map 里存 5 -> 2
         *      这样后面如果有一个前缀和 - k == 5 时，可以加上这个 2
         *
         *      然后每次加完数字，得把当前的前缀和存进去，更新或覆盖！
         * */
        public int subarraySum(int[] nums, int k) {
            Map<Integer, Integer> map = new HashMap<>();
            map.put(0, 1);
            int pre = 0, count = 0;
            for (int i = 0; i < nums.length; i++) {
                pre += nums[i];
                if(map.containsKey(pre - k)) {
                    count++;
                }
                map.put(pre, map.getOrDefault(pre, 0) + 1);
            }
            return count;
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
