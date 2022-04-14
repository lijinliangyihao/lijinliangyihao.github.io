package treasure.alg.jianzhioffer;

/**
 * 这波我用的是快排里那个交换
 * 我都没分配内存，居然只击败了 38% 的人？
 * */
public class Offer21 {
    
    public int[] exchange(int[] nums) {
        int n = nums.length;
        if(n == 0) return nums;
        int l = 0, r = n - 1;
        while (l < r) {
            while(l < r && (nums[l] & 1) == 1) l++;
            while(l < r && (nums[r] & 1) == 0) r--;
            if(l != r) {
                int t = nums[l];
                nums[l] = nums[r];
                nums[r] = t;
            }
        }
        return nums;
    }

}
