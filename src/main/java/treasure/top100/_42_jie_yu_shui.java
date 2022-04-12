package treasure.top100;

import java.util.Stack;

public class _42_jie_yu_shui {
    
    
    /**
     * 思路是：每个位置都往左、往右找到最大值，然后求左右最大值的较小值，然后累计当前值
     * 简单而有效！
     * */
    public static class BruteForce {
        
        public int trap(int[] height) {
            int n = height.length;
            int res = 0;
            for (int i = 1; i < n; i++) {
                int leftMax = height[i], rightMax = leftMax;
                for (int j = i - 1; j >= 0; j--) {
                    if(height[j] > leftMax) leftMax = height[j];
                }
                for (int j = i + 1; j < n; j++) {
                    if(height[j] > rightMax) rightMax = height[j];
                }
                int min = Math.min(leftMax, rightMax);
                if(min > height[i]) {
                    res += min - height[i];
                }
            }
            return res;
        }
    }
    
    /**
     * 我勒个去！dp 思路和暴力非常的像，只是多了个存储，
     * 谁说暴力是垃圾！一定得会暴力。
     *
     * 而且这个 dp 已经很像双指针了，为后面的双指针奠定极了基础
     * */
    public static class Dp {
        
        public int trap(int[] height) {
            int n = height.length;
            int[] leftMax = new int[n];
            int[] rightMax = new int[n];
            
            leftMax[0] = height[0];
            for (int i = 1; i < n; i++) {
                leftMax[i] = Math.max(height[i], leftMax[i - 1]);
            }
            rightMax[n - 1] = height[n - 1];
            for (int i = n - 2; i >= 0; i--) {
                rightMax[i] = Math.max(height[i], rightMax[i + 1]);
            }
    
            int res = 0;
            for (int i = 1; i < n - 1; i++) {
                res += Math.min(leftMax[i], rightMax[i]) - height[i];
            }
            return res;
        }
    }
    
    /**
     * 双指针是对 dp 的一种抽象
     * 思路是一样的
     * */
    public static class DoublePointer {
    
        public int trap(int[] height) {
            int n = height.length;
            int left = 0, right = n - 1;
            int leftMax = 0, rightMax = 0;
            int res = 0;
            while(left < right) {
                // 谁小从哪一段开始累积结果
                if(height[left] < height[right]) {
                    if(height[left] > leftMax) leftMax = height[left];
                    else if(height[left] < leftMax) res += leftMax - height[left];
                    left++;
                } else {
                    if(height[right] > rightMax) rightMax = height[right];
                    else if(height[right] < rightMax) res += rightMax - height[right];
                    right--;
                }
            }
            return res;
        }
    }
    
    /**
     * 使用单调栈结构
     * 之前是一个竖条一个竖条的累计结果，现在是找坑，看每个坑能装多少；然后发现一个坑，累计值后把坑填上（相当于）
     *
     * 这个方法虽然是一趟，但是还没有 dp 快
     * */
    public static class MonotonousStack {
        
        public int trap(int[] height) {
            int n = height.length;
            Stack<Integer> stack = new Stack<>();
            int res = 0;
            for (int i = 0; i < n; i++) {
                while(!stack.isEmpty() && height[i] > height[stack.peek()]) {
                    int lowerIndex = stack.pop();
                    if(stack.isEmpty()) break;
                    int dist = i - stack.peek() - 1;
                    int h = Math.min(height[stack.peek()], height[i]) - height[lowerIndex];
                    // 我加上这个判断，没有任何性能提升
                    if(h > 0) {
                        res += h * dist;
                    }
                }
                stack.push(i);
            }
            return res;
        }
    }
}
