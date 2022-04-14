package treasure.alg.jianzhioffer;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 输入一个非负整数数组，把数组里所有数字拼接起来排成一个数，
 * 打印能拼接出的所有数字中最小的一个
 *
 * 这个题首先你得获取数字的全排列
 * 如果知道比较是比较他们的字符串了，那难度只在你会不会全排列了。。
 *
 * 完了搞错了，不能全排列，会超时
 * 直接排序出来个字符串就行了
 * */
public class Offer45 {
    
    /**
     * 只干死了 27.89%，不过比我那个超时的强
     * */
    public static class TheSolution {
        
        public String minNumber(int[] nums) {
            if(nums.length == 0) return null;
            PriorityQueue<Integer> q = new PriorityQueue<>((a, b) -> {
                String s1 = String.valueOf(a);
                String s2 = String.valueOf(b);
                return s1.concat(s2).compareTo(s2.concat(s1));
            });
            for(int i: nums) q.offer(i);
            StringBuilder sb = new StringBuilder();
            while(!q.isEmpty()) {
                sb.append(q.poll());
            }
            return sb.toString();
        }
    }
    
    public static class MyTimeout {
        public String minNumber(int[] nums) {
            if(nums.length == 0) return null;
            PriorityQueue<String> q = new PriorityQueue<>(permute(nums));
            return q.poll();
        }
    
        LinkedList<String> res = new LinkedList<>();
        List<String> permute(int[] nums) {
            doPermute(nums, 0);
            return res;
        }
    
        void doPermute(int[] nums, int index) {
            if(index == nums.length) {
                StringBuilder sb = new StringBuilder();
                for (Integer i : nums) {
                    sb.append(i);
                }
                res.add(sb.toString());
                return;
            }
            for (int i = index; i < nums.length; i++) {
                // 这个 swap 意思是让每个数字都来 index 试一圈
                swap(nums, index, i);
                doPermute(nums, index + 1);
                swap(nums, index, i);
            }
        }
    
        void swap(int[] nums, int i, int j) {
            if(i != j) {
                int t = nums[i];
                nums[i] = nums[j];
                nums[j] = t;
            }
        }
    }
    
}
