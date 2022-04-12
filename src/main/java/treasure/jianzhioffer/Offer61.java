package treasure.jianzhioffer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 从若干副扑克牌中随机抽 5 张牌，判断是不是一个顺子，即这5张牌是不是连续的。
 * 2～10为数字本身，A为1，J为11，Q为12，K为13，而大、小王为 0 ，可以看成任意数字。
 * A 不能视为 14
 *
 * [0,0,1,2,5]
 * 输出: True
 * */
public class Offer61 {
    
    public static class Luffy {
    
        // 一种是无需排序，用两个变量判断间隔，一个 set 判重
        // 这个很离谱，不需要管有几个 0
        public boolean isStraight1(int[] nums) {
            Set<Integer> set = new HashSet<>();
            int min = 14;
            int max = 0;
            for (int num : nums) {
                if(num == 0) continue;
                if(!set.add(num)) return false;
                min = Math.min(min, num);
                max = Math.max(max, num);
            }
            return max - min < 5;
        }
        
        // 第二种和我背的思路类似
        public boolean isStraight(int[] nums) {
            Arrays.sort(nums);
            int joker = 0;
            int last = -1;
            for (int num : nums) {
                if(num == 0) joker++;
                else if(num == last) return false;
                last = num;
            }
            return nums[4] - nums[joker] < 5;
        }
    }
    
    public static class Mine {
        // 我背过了答案：先排序，再记录 gap 长度，<= 0 则 true
        public boolean isStraight(int[] nums) {
            Arrays.sort(nums);
            int zeros = 0;
            while(nums[zeros] == 0) zeros++;
        
            int start = zeros;
            int gap = 0;
            for (int i = start + 1; i < 5; i++) {
                // 对子不能忘了考虑不然就 gg 了
                if(nums[i] == nums[i - 1]) return false;
                gap += nums[i] - nums[i - 1] - 1;
            }
            return gap <= zeros;
        }
    }
}
