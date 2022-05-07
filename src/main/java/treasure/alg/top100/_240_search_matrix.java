package treasure.alg.top100;

/**
 * 做过几亿次了。我就算脑淤血也能做出来这道题
 * */
public class _240_search_matrix {
    
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int i = 0, j = n - 1;
        while (i < m && j >= 0) {
            if (matrix[i][j] == target)
                return true;
            // 这两行的原理是，每次排除了一整行
            if (matrix[i][j] < target)
                i++;
            else
                j--;
        }
        return false;
    }
}
