package treasure.top100;

public class _34_sorted_array_find_number_first_and_last_pos {
    
    /**
     *  不要想着背过写法、出什么奇技淫巧
     *  最原始的写法不一定慢，最准确不容易遗忘
     * */
    public static class Naive {
    
        public int[] searchRange(int[] nums, int target) {
            if(nums.length == 0) return new int[]{-1, -1};
            return new int[]{findLeft(nums, target), findRight(nums, target)};
        }
    
        private int findLeft(int[] nums, int target) {
            int l = 0, r = nums.length - 1;
            int res = -1;
            while(l <= r) {
                int mid = (l + r) >>> 1;
                if(nums[mid] == target) {
                    res = mid;
                    r = mid - 1;
                } else if(nums[mid] < target) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
            return res;
        }
        private int findRight(int[] nums, int target) {
            int l = 0, r = nums.length - 1;
            int res = -1;
            while(l <= r) {
                int mid = (l + r) >>> 1;
                if(nums[mid] == target) {
                    res = mid;
                    l = mid + 1;
                } else if(nums[mid] < target) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
            return res;
        }
    }
    
}
