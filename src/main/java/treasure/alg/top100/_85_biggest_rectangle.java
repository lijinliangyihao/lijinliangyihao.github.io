package treasure.alg.top100;

import java.util.Arrays;
import java.util.Stack;

/**
 * 给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，
 * 找出只包含 1 的最大矩形，并返回其面积
 *
 * */
public class _85_biggest_rectangle {

    /**
     * 优化的柱状图啥的
     *
     * */
    public static class ZhuZhuangTu {
    
        public int maximalRectangle(char[][] matrix) {
            if(matrix.length == 0 || matrix[0].length == 0) return 0;
            int m = matrix.length, n = matrix[0].length;
    
            int[][] dp = new int[m][n];
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if(matrix[i][j] == '1')
                        dp[i][j] = j == 0 ? 1 : dp[i][j - 1] + 1;
                }
            }
    
            int res = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if(matrix[i][j] == '1') {
                        int width = dp[i][j];
                        for (int k = i; k >= 0; k--) {
                            if(dp[k][j] == 0) break;
                            width = Math.min(width, dp[k][j]);
                            res = Math.max(res, width * (i - k + 1));
                        }
                    }
                }
            }
            
            return res;
        }
    }
    
    /**
     * 这个解法和 84 关系比较紧密！
     *
     * 这个方法比较奇怪，而且也更慢
     * 看来还是第一种好点
     *
     * */
    public static class DanDiaoZhan {
    
        public int maximalRectangle(char[][] matrix) {
            int m = matrix.length;
            if (m == 0) {
                return 0;
            }
            int n = matrix[0].length;
            int[][] left = new int[m][n];
        
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j] == '1') {
                        left[i][j] = (j == 0 ? 0 : left[i][j - 1]) + 1;
                    }
                }
            }
        
            int ret = 0;
            for (int j = 0; j < n; j++) { // 对于每一列，使用基于柱状图的方法
                int[] up = new int[m];
                int[] down = new int[m];
                Arrays.fill(down, m);
            
                Stack<Integer> stack = new Stack<>();
                // 不知道为什么这两趟遍历合成一趟，慢了 30%
                // 必须遍历两次？奶奶滴
                for (int i = 0; i < m; i++) {
                    while (!stack.isEmpty() && left[stack.peek()][j] >= left[i][j]) {
                        down[stack.peek()] = i;
                        stack.pop();
                    }
                    up[i] = stack.isEmpty() ? -1 : stack.peek();
                    stack.push(i);
                }
            
                for (int i = 0; i < m; i++) {
                    ret = Math.max(ret, left[i][j] * (down[i] - up[i] - 1));
                }
            }
            return ret;
        }
    }

}
