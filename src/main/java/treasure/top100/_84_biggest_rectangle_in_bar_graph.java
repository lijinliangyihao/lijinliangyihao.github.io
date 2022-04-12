package treasure.top100;

import java.util.Arrays;
import java.util.Stack;

/**
 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 *
 * 思考了一会，还是不会做！
 *
 * 草他妈，暴力方法都没想出来，没有脑子！
 * */
public class _84_biggest_rectangle_in_bar_graph {
    
    /**
     * 任何情况下不要忘了暴力方法！
     *
     * 这两个解法都是对的，就是会超时
     * */
    public static class BruteForce {
        
        // 枚举和自己一样高的所有的柱子
        public int largestRectangleArea(int[] heights) {
            int n = heights.length;
            int max = 0;
            for (int i = 0; i < n; i++) {
                int left = i - 1, right = i + 1;
                while(left >= 0 && heights[left] >= heights[i]) left--;
                while(right < n && heights[right] >= heights[i]) right++;
                max = Math.max(max, (right - left + 1) * heights[i]);
            }
            return max;
        }
    
        // 枚举到当前位置为止的宽度的最大面积，先枚举右边界，再枚往左挪举左边界
        public int largestRectangleArea2(int[] heights) {
            int n = heights.length;
            int max = 0;
            for (int i = 0; i < n; i++) {
                int h = heights[i];
                for (int j = i; j >= 0; j--) {
                    h = Math.min(h, heights[j]);
                    max = Math.max(max, h * (i - j + 1));
                }
            }
            return max;
        }
    }
    
    /**
     * 单调栈用到了：固定高度，往两侧扩展的暴力解法思路
     * 因为两侧遇到比自己低的就停止，遇到比自己高的继续往前走
     *
     * 方法：遍历两次，得到两个数组，每个组数下标是往左或往右比自己低的那个位置
     * 再遍历一次，获取每个位置的面积，同时求最大值
     *
     * 获取这两个数组的过程就是单调栈
     * */
    public static class DanDiaoZhan1 {
    
        
        public int largestRectangleArea(int[] heights) {
            int n = heights.length;
            int[] left = new int[n];
            int[] right = new int[n];
            Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < n; i++) {
                while(!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }
                left[i] = stack.isEmpty() ? -1 : stack.peek();
                stack.push(i);
            }
            stack.clear();
            for (int i = n - 1; i >= 0; i--) {
                while(!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }
                // 忘了改这了，这应该是 n
                right[i] = stack.isEmpty() ? n : stack.peek();
                stack.push(i);
            }
            int res = 0;
            for (int i = 0; i < n; i++) {
                res = Math.max(res, (right[i] - left[i] - 1) * heights[i]);
            }
            return res;
        }
    }
    
    /**
     * 也是单调栈，但是只需要一趟遍历
     * 思路和上面是一样的，也是需要两个数组记录左和右的下标位置，乘以高度
     * 区别是：在同一趟遍历里，当值被 pop 时，更新右侧值
     * 其它的都一样
     *
     * 说是遍历了一趟，这个更慢了！
     * */
    public static class DanDiaoZhan2 {
    
        public int largestRectangleArea(int[] heights) {
            int n = heights.length;
            int[] left = new int[n];
            int[] right = new int[n];
            Arrays.fill(right, n);
            
            Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < n; i++) {
                while(!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    // 这里非常的巧妙，但是他妈的，反而降低了运行效率，我透
                    right[stack.peek()] = i;
                    stack.pop();
                }
                left[i] = stack.isEmpty() ? -1 : stack.peek();
                stack.push(i);
            }
            int res = 0;
            for (int i = 0; i < n; i++) {
                res = Math.max(res, heights[i] * (right[i] - left[i] - 1));
            }
            return res;
        }
    }
}
