package treasure.alg.top100;

public class _53_max_sub_array {
    
    /*
    * 从头到尾扫：对于每个数，看看是当前数大，还是之前的数加当前数大
    * 同时记录一个全局最大值
    *
    * 居然做错了！
    *
    * */
    public int maxSubArray_wrong(int[] nums) {
        // 这个 max 初始化错了
        int max = -10001, cur = nums[0];
        for (int i = 1; i < nums.length; i++) {
            cur = Math.max(nums[i], nums[i] + cur);
            max = Math.max(cur, max);
        }
        return max;
    }
    
    
    public int maxSubArray(int[] nums) {
        int max = nums[0], cur = max;
        for (int i = 1; i < nums.length; i++) {
            cur = Math.max(nums[i], nums[i] + cur);
            max = Math.max(cur, max);
        }
        return max;
    }
}
