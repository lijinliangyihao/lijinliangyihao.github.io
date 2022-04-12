package treasure.jianzhioffer;

/**
 * 输入一个整型数组，数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。
 * 要求时间复杂度为O(n)
 *
 * 这个很简单，就是 max 初始值要注意，需要很小
 * */
public class Offer42 {
    
    public int maxSubArray(int[] nums) {
        int max = Integer.MIN_VALUE, last = 0;
        for (int i = 0; i < nums.length; i++) {
            if(last < 0) {
                last = nums[i];
            }
            else {
                last += nums[i];
            }
            max = Math.max(max, last);
        }
        return max;
    }

}
