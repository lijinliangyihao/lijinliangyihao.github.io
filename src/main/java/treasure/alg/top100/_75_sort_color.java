package treasure.alg.top100;

public class _75_sort_color {
    
    public void sortColors(int[] nums) {
        int end = sort0(nums, 0, nums.length - 1);
        sort2(nums, end, nums.length - 1);
    }
    
    int sort0(int[] nums, int l, int r) {
        while (l < r) {
            while (l < r && nums[r] > 0) r--;
            while (l < r && nums[l] == 0) l++;
            if(nums[l] != nums[r]) {
                int t = nums[l]; nums[l] = nums[r]; nums[r] = t;
            }
        }
        return l;
    }
    
    int sort2(int[] nums, int l, int r) {
        while (l < r) {
            while (l < r && nums[r] == 2) r--;
            while (l < r && nums[l] < 2) l++;
            if(nums[l] != nums[r]) {
                int t = nums[l]; nums[l] = nums[r]; nums[r] = t;
            }
        }
        return l;
    }
}
