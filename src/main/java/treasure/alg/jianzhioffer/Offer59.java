package treasure.alg.jianzhioffer;

import java.util.LinkedList;

/**
 * 给定一个数组 nums 和滑动窗口的大小 k，请找出所有滑动窗口里的最大值。
 *
 * 很经典的题，挑战一把！
 * */
public class Offer59 {
    
    /**
     * 我死记硬背搞出来的解法，比较冗长，看看别人的
     * */
    public static class SiJiYingBei {
        
        public int[] maxSlidingWindow(int[] nums, int k) {
            if(nums.length == 0) return new int[0];
            int[] res = new int[nums.length - k + 1];
            // 做了很多遍了，这里面存下标
            LinkedList<Integer> q = new LinkedList<>();
        
            for (int i = 0; i < k; i++) {
                while(!q.isEmpty() && nums[q.peekLast()] <= nums[i]) {
                    q.pollLast();
                }
                q.offer(i);
            }
            int j = 0;
            res[j++] = nums[q.peek()];
        
            for (int i = k; i < nums.length; i++, j++) {
                while(!q.isEmpty() && nums[q.peekLast()] <= nums[i]) {
                    q.pollLast();
                }
                q.offer(i);
                // 越界的移除
                if(q.peek() == i - k) q.poll();
                res[j] = nums[q.peek()];
            }
            return res;
        }
    
        /**
         * 参考别人的改的，没比我的好到哪去
         * */
        public int[] shorter(int[] nums, int k) {
            if(nums.length == 0) return new int[0];
            int[] res = new int[nums.length - k + 1];
            LinkedList<Integer> q = new LinkedList<>();
            for (int i = 0; i < nums.length; i++) {
                while(!q.isEmpty() && nums[q.peekLast()] <= nums[i]) {
                    q.pollLast();
                }
                q.offer(i);
                if(q.peek() == i - k) q.poll();
                // i == k - 1 时产生第一个，故
                if(i - k + 1 > -1) {
                    res[i - k + 1] = nums[q.peek()];
                }
            }
            return res;
        }
    }
    
}
