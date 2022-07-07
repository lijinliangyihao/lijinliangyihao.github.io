package treasure.alg.top100;

import java.util.ArrayList;
import java.util.List;

public class _448_findDisappearedNumbers {
    
    /*
    * 挨个交换，直到被交换的位置的数和自己一样
    * 第二轮，挨个数，发现和自己位置不一样的就记录
    * */
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            while (num != i + 1 && num != nums[num - 1]) {
                nums[i] = nums[num - 1];
                nums[num - 1] = num;
                num = nums[i];
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
//              这有点瑕疵，之前写成了 res.add(nums[i]);
                res.add(i + 1);
            }
        }
        return res;
    }
    
    /*
    * 官方题解比较妙，通过增加 n 来判断
    * */
    public List<Integer> findDisappearedNumbers_v2(int[] nums) {
        List<Integer> res = new ArrayList<>();
        int n = nums.length;
        for (int num : nums) {
//          有个小瑕疵，写成 nums[num % n] += n; 了，少 - 了一个 1
            nums[(num - 1) % n] += n;
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] <= n) {
                res.add(i + 1);
            }
        }
        return res;
    }
}
