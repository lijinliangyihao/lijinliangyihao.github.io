package treasure.alg.top100;

public class _79_search_word {
    
    /**
     * 就一个解法，dfs
     * 我感觉我的，比答案优雅
     * */
    public boolean exist(char[][] board, String word) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (doSearch(board, visited, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    boolean doSearch(char[][] board, boolean[][] visited, String word, int i, int j, int len) {
        if (len == word.length()) return true;
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j] || board[i][j] != word.charAt(len)) return false;
        visited[i][j] = true;
        boolean exist = doSearch(board, visited, word, i + 1, j, len + 1) ||
            doSearch(board, visited, word, i, j + 1, len + 1) ||
            doSearch(board, visited, word, i - 1, j, len + 1) ||
            doSearch(board, visited, word, i, j - 1, len + 1);
        visited[i][j] = false;
        return exist;
    }
}
