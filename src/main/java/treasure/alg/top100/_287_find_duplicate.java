package treasure.alg.top100;

import java.util.HashMap;

/**
 * 不能修改数组，还得用常数空间
 * 我一看数字都是正的，所以考虑把数字搞成负的再恢复，他们发现不了
 *
 * 烦死了
 * 看官方题解看了好几个小时也没看太懂，操他妈的
 * */
public class _287_find_duplicate {
    
    public static class fast_slow {
    
        /**
         * 没想到，这个能相当于快慢指针？？？
         * 怎么理解 “环” 这个概念？环就是某一个点，它有两个入口
         * 如果每个元素，都视为一个指针，指向它要去的下标
         * 重的这个元素，相当于两个元素指向同一个下标
         * 也就是有了环
         *
         * 无耻！
         * */
        public int findDuplicate(int[] nums) {
            int slow = nums[0], fast = nums[slow];
            while (slow != fast) {
                slow = nums[slow];
                fast = nums[nums[fast]];
            }
            // 这应该是 0 而不是 nums[0]
            // 不然就死循环了！
            slow = 0;
            while (slow != fast) {
                slow = nums[slow];
                fast = nums[fast];
            }
            return slow;
        }
    }
    
    public static class bit_wise {
    
        /**
         * 这个是位运算，也不太好想
         * 你得知道一个简单的证明，就不多提了
         *
         * 这个解法也是，效率非常的低；因为位运算比较消耗 cpu？
         * */
        public int findDuplicate(int[] nums) {
            int bits = 32 - Integer.numberOfLeadingZeros(nums.length - 1);
            int res = 0;
            for (int i = 0; i < bits; i++) {
                int x = 0, y = 0;
                for (int j = 0; j < nums.length; j++) {
                    // 这个写成 == 1 了
                    // 这块错过几亿次了吧？
                    if ((nums[j] & (1 << i)) != 0)
                        x++;
                    if (j > 0 && (j & (1 << i)) != 0)
                        y++;
                }
                if (x > y)
                    res |= 1 << i;
            }
            return res;
        }
    }
    
    public static class binary_search {
    
        /**
         * 这个效率不高不说，还不是那么容易理解
         * cnt(i) 表示，对于数字 i，<= i 的数字的个数
         * 1 target 有两个，那 [1, target - 1] cnt(i) = i, [target, n] cnt(i) = i + 1
         * 2 target 至少三个，那就可以视为，有些数被 target 替换了，
         *     假如替换的是 i < target 那 [i, target - 1] 里的数，cnt(i) = i - 1, [1, i - 1] 还是 i
         *     如果替换的 i > target，那 右边的数应该是没变的还是 i - 1
         *
         * 从 1 到 n 开始二分查找，时间复杂度 nlogn
         *
         * 看着答案抄，我写出了五处错误，可真强啊
         * */
        public int findDuplicate(int[] nums) {
            // 错误0 这个 l 写成 0
            // 错误1 这个 r 写成 nums.length
            int l = 1, r = nums.length - 1;
            int ans = -1;
            while (l <= r) {
                int mid = (l + r) >>> 1;
                int cnt = 0;
                for (int num : nums) {
                    // 错误 2 这个符号写反了
                    if (num <= mid)
                        cnt++;
                }
                if (cnt <= mid)
                    // 错误3 写成 l++
                    l = mid + 1;
                else {
                    // 错误4 写成 r--
                    r = mid - 1;
                    ans = mid;
                }
            }
            return ans;
        }
    }
    
    public static class mine {
        /**
         * 我的解法是对的，但是中间忽略了正负号，导致越界了
         * */
        public int findDuplicate(int[] nums) {
            try {
                for (int num : nums) {
                    int abs = Math.abs(num);
                    if (nums[abs - 1] < 0)
                        return abs;
                    nums[abs - 1] = -nums[abs - 1];
                }
            }
            finally {
                for (int i = 0; i < nums.length; i++) {
                    nums[i] = Math.abs(nums[i]);
                }
            }
            return -1;
        }
    }
    
}
