package treasure.alg.top100;

/**
 * 我知道是个二维 dp 但是不想浪费时间想了因为我应该做不出来
 * */
public class _152_max_product {

    /**
     * 呵呵，不是二维数组，是两个一维数组
     * 也没必要两个一位数组，两个变量就够了
     * */
    public static class s1 {
        
        public int maxProduct(int[] nums) {
            int max = nums[0], min = max, res = max;
            for (int i = 1; i < nums.length; i++) {
                // 这个 max 被修改了所以得保存临时变量
                int tempMax = max;
                max = Math.max(max * nums[i], Math.max(min * nums[i], nums[i]));
                min = Math.min(tempMax * nums[i], Math.min(min * nums[i], nums[i]));
                // max 肯定是最大的因为求了 max，所以不用和 min 比了
                res = Math.max(max, res);
            }
            return res;
        }
    }
    
}
