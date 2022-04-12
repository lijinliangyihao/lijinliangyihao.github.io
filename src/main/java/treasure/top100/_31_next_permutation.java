package treasure.top100;

public class _31_next_permutation {
    /**
     * 就是找规律，比当前数字大的最小的数有什么特点？
     * 541234567 -> 541234576
     * 如果后面升序，交换最后两个就行了
     * 5412375422 -> 5412275423
     * 如果隔了几个降序的元素，找到一个升序的，得把降序的两个元素搞成升序，然后交换两个升序元素
     *
     * 倒着找，如果一直都是升序，那完整的反过来就行了
     * 如果找到一个降序的，记住它，然后再倒着找，找到第一个比它大的元素，和它交换位置
     * 然后把这个元素后面的元素排序
     *  35421 -> 41235
     *
     *  连蒙带猜居然搞出来了，可怕！
     * */
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int i = n - 1;
        while(i > 0 && nums[i - 1] >= nums[i]) i--;
        if (i > 0) {
            int t = nums[i - 1];
            int j = n - 1;
            while(nums[j] <= t) j--;
            nums[i - 1] = nums[j];
            nums[j] = t;
        }
        reverse(nums, i, n - 1);
    }
    
    private void reverse(int[] nums, int l, int r) {
        while(l < r) {
            int t = nums[l];
            nums[l] = nums[r];
            nums[r] = t;
            l++;
            r--;
        }
    }
}
