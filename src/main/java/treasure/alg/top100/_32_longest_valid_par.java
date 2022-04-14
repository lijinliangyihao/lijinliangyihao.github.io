package treasure.alg.top100;

import java.util.Stack;

/**
 * 输入：s = ")()())"
 * 输出：4
 */
public class _32_longest_valid_par {
    
    public static class Dp {
        
        public int longestValidParentheses(String s) {
            int n = s.length();
            if (n == 0) return 0;
            int max = 0;
            int[] dp = new int[n];
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i) == ')') {
                    if (s.charAt(i - 1) == '(') {
                        dp[i] = 2 + (i - 1 > 0 ? dp[i - 2] : 0);
                    } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                        dp[i] = 2 + dp[i - 1] + (i - dp[i - 1] - 1 > 0 ? dp[i - dp[i - 1] - 2] : 0);
                    }
                    max = Math.max(max, dp[i]);
                }
            }
            return max;
        }
    }
    
    /**
     * 这个栈设计的就是，栈底元素是上一个未匹配右括号，其它元素是左括号，的下标
     * 你也可以维护上次右括号下标
     */
    public static class stack {
        
        // 我改了一下，维护上次右括号下标
        // 因为我感觉一个栈里放两种下标不是很好想到
        public int longestValidParentheses(String s) {
            int n = s.length();
            Stack<Integer> stack = new Stack<>();
            int max = 0;
            int lastRightParIndex = -1;
            for (int i = 0; i < n; i++) {
                if (s.charAt(i) == '(') {
                    stack.push(i);
                } else {
                    if (stack.isEmpty()) {
                        lastRightParIndex = i;
                    } else {
                        stack.pop();
                        max = Math.max(max, stack.isEmpty() ? i - lastRightParIndex : i - stack.peek());
                    }
                }
            }
            return max;
        }
        
        
        // 答案是把右括号放到栈底，栈底总是右括号
        public int longestValidParentheses2(String s) {
            int n = s.length();
            Stack<Integer> stack = new Stack<>();
            int max = 0;
            stack.push(-1);
            for (int i = 0; i < n; i++) {
                if (s.charAt(i) == '(') {
                    stack.push(i);
                } else {
                    stack.pop();
                    if (stack.isEmpty()) {
                        stack.push(i);
                    } else {
                        max = Math.max(max, i - stack.peek());
                    }
                }
            }
            return max;
        }
    }
    
    /**
     * 遍历两次，从左往右，从右往左
     * 同时数有几个括号，如果左右括号数量相等，计数；左多了，增加左；右多了，归零
     * <p>
     * 为何两次，因为考虑到左比右多时会少考虑很多个有效长度，所以得反着来一遍
     */
    public static class CountPar {
        
        public int longestValidParentheses(String s) {
            int max = 0, left = 0, right = 0, n = s.length();
            
            for (int i = 0; i < n; i++) {
                if (s.charAt(i) == '(') {
                    left++;
                } else {
                    right++;
                    if (right > left) left = right = 0;
                }
                if (left == right)
                    max = Math.max(max, left * 2);
            }
            
            left = right = 0;
            
            for (int i = n - 1; i >= 0; i--) {
                if (s.charAt(i) == ')') {
                    right++;
                } else {
                    left++;
                    if (right < left) left = right = 0;
                }
                if (left == right)
                    max = Math.max(max, left * 2);
            }
            
            return max;
        }
    }
    
    
}
