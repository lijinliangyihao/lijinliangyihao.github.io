package treasure.jianzhioffer;

/**
 * 输入一个递增排序的数组和一个数字s，在数组中查找两个数，使得它们的和正好是s。
 * 如果有多对数字的和等于s，则输出任意一对即可
 *
 * 过于简单了！
 * */
public class Offer57 {
    
    public int[] twoSum(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while(l < r) {
            int sum = nums[l] + nums[r];
            if(sum == target) return new int[] {nums[l], nums[r]};
            if(sum > target) r--;
            else l++;
        }
        return null;
    }
    
}
