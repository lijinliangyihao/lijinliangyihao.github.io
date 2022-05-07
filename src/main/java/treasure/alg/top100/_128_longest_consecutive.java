package treasure.alg.top100;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 这个题居然没做出来实在是太不应该了！
 * */
public class _128_longest_consecutive {
    
    // 我一直在想排序的问题，但是排序就成 nlogn 了
    // 要求 on，但没想到 hashtable
    /***
     * 暴力怎么搞？一个个数试，拿到一个数，然后从头遍历，找 x + 1
     * 我感觉得递归
     *
     * 注：这不叫递归，只是一个普通的循环
     *
     * 果然超时了
     * */
    public static class force {
    
        public int longestConsecutive(int[] nums) {
            int max = 0;
            int n = nums.length;
            for (int num : nums) {
                int cnt = 1;
                while (searchX(nums, num + cnt)) {
                    cnt++;
                }
                max = Math.max(max, cnt);
            }
            return max;
        }
    
        boolean searchX(int[] nums, int x) {
            for (int num : nums) {
                if (x == num)
                    return true;
            }
            return false;
        }
    }
    
    public static class use_a_set {
    
        /**
         * 每次从头遍历不太聪明，我们这次试用一个 set
         *
         * 但是又超时了！
         * */
        public int longestConsecutive(int[] nums) {
            Set<Integer> set = new HashSet<>();
            for (int num : nums) {
                set.add(num);
            }
            int max = 0;
            for (int num : nums) {
                int add = 1;
                while (set.contains(num + add)) {
                    add++;
                }
                max = Math.max(max, add);
            }
            return max;
        }
    }
    
    public static class use_a_set_and_condition {
    
        /**
         * 这次不得不，进行一些额外的判断了
         *
         * 才击败了 5%，我去你妈的！
         * */
        public int longestConsecutive(int[] nums) {
            Set<Integer> set = new HashSet<>();
            for (int num : nums) {
                set.add(num);
            }
            int max = 0;
            for (int num : nums) {
                if (set.contains(num - 1)) continue;
                int add = 1;
                while (set.contains(num + add)) {
                    add++;
                }
                max = Math.max(max, add);
            }
            return max;
        }
    }
    
    // 熟悉吧这个单词，我排序试试快不快
    public static class uncensored {
        
        /**
         * 我用不符合要求的 nlogn 解法，打败 99.89%
         * */
        public int longestConsecutive(int[] nums) {
            Arrays.sort(nums);
            int start = 0;
            int max = 0;
            int offset = 0;
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] != nums[i - 1] + 1) {
                    if (nums[i] == nums[i - 1]) {
                        offset--;
                        continue;
                    }
                    max = Math.max(max, i - start + offset);
                    offset = 0;
                    start = i;
                }
            }
            max = Math.max(max, nums.length - start + offset);
            return max;
        }
    }
}
