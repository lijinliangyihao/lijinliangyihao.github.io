package treasure.alg.top100;

import java.util.ArrayList;
import java.util.List;

public class _54_print_spiral {
    
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int m = matrix.length, n = matrix[0].length;
        
        int l = 0, r = n - 1, top = 0, bottom = m - 1;
        while (l <= r && top <= bottom) {
            for (int i = l; i <= r; i++) {
                res.add(matrix[top][i]);
            }
            for (int i = top + 1; i <= bottom ; i++) {
                res.add(matrix[i][r]);
            }
            if (l < r && top < bottom) {
                for (int i = r - 1; i >= l ; i--) {
                    res.add(matrix[bottom][i]);
                }
                for (int i = bottom - 1; i > top ; i--) {
                    res.add(matrix[i][l]);
                }
            }
            l++; r--; top++; bottom--;
        }
        
        return res;
    }
    
    public List<Integer> spiralOrder2(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int m = matrix.length, n = matrix[0].length;
        boolean[][] v = new boolean[m][n];
        int idx = 0;
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int row = 0, col = 0;
        int i = 0, total = m * n;
        while (i++ < total) {
            res.add(matrix[row][col]);
            v[row][col] = true;
            int rr = row + dirs[idx][0], cc = col + dirs[idx][1];
            if (rr < 0 || rr >= m || cc < 0 || cc >= n || v[rr][cc])
                idx = (idx + 1) % 4;
            row += dirs[idx][0];
            col += dirs[idx][1];
        }
        
        return res;
    }
    
}
