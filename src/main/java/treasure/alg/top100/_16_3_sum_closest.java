package treasure.alg.top100;

import java.util.Arrays;

/**
 * 放弃了，不会
 * */
public class _16_3_sum_closest {
    
    /**
     * 很难吗？
     * */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int res = Integer.MAX_VALUE;
    
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1])
                continue;
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum == target) {
                    return target;
                }
                if (res == Integer.MAX_VALUE || Math.abs(sum - target) < Math.abs(res - target))
                    res = sum;
                // 这有个小优化，如果相邻两个数一样，可以持续的移动，没乃个必要
                if (sum > target) {
                    r--;
                } else {
                    l++;
                }
            }
        }
        return res;
    }
}
