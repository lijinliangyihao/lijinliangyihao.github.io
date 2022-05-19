package treasure.alg.top100;

/**
 * 真鸡巴烦。一想就不会！
 * */
public class _59_spiral_matrix_2 {
    
    /**
     * 淦
     * 流下了悔恨的泪水
     * */
    public int[][] generateMatrix(int n) {
        int max = n * n;
        int cur = 1;
        int[][] res = new int[n][n];
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int dirIdx = 0;
        int row = 0, col = 0;
        while (cur <= max) {
            res[row][col] = cur++;
            int nextRow = row + dirs[dirIdx][0], nextCol = col + dirs[dirIdx][1];
            if (nextRow < 0 || nextCol < 0 || nextRow >= n || nextCol >= n || res[nextRow][nextCol] != 0)
                dirIdx = (dirIdx + 1) % 4;
            row += dirs[dirIdx][0];
            col += dirs[dirIdx][1];
        }
        return res;
    }
    
    // 错了得有一千处吧，也就
    public int[][] generateMatrix1(int n) {
        int num = 1;
        int[][] res = new int[n][n];
        int left = 0, right = n - 1, top = 0, bottom = n - 1;
        while (left <= right && top <= bottom) {
            for (int i = left; i <= right ; i++) {
                res[top][i] = num++;
            }
            for (int i = top + 1; i <= bottom ; i++) {
                res[i][right] = num++;
            }
        
            // 这里判断能否往左或往上走
            if (left < right && top < bottom) {
                for (int i = right - 1; i >= left ; i--) {
                    res[bottom][i] = num++;
                }
                for (int i = bottom - 1; i > top ; i--) {
                    res[i][left] = num++;
                }
            }
        
            left++; right--; top++; bottom--;
        }
        return res;
    }
    
}
