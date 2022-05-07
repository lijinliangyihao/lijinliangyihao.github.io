package treasure.alg.top100;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 这个我肯定能做出来，但我更倾向于用并查集来做，但我可能写不出来一个好用的并查集操他妈
 *
 * 没搞出来。。。想 bfs 写成了 dfs，还写错了
 * 并查集也不会写
 * 我操！
 *
 * */
public class _200_num_islands {
    
    public static class my_union_find {
    
        UnionFind uf;
        public int numIslands(char[][] grid) {
            int m = grid.length, n = grid[0].length;
            uf = new UnionFind(grid);
            boolean[][] visited = new boolean[m][n];
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    helper(grid, visited, i, j);
                }
            }
            return uf.count;
        }
        
        void helper(char[][] grid, boolean[][] visited, int i, int j) {
            if (visited[i][j] || grid[i][j] == '0')
                return;
            visited[i][j] = true;
            int n = grid[0].length;
            int current = i * n + j;
            int[][] dirs = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : dirs) {
                int i1 = dir[0] + i, j1 = dir[1] + j;
                if (i1 < 0 || i1 >= grid.length || j1 < 0 || j1 >= n || visited[i1][j1] || grid[i1][j1] == '0') continue;
                uf.unite(current, i1 * n + j1);
            }
        }
        
        static class UnionFind {
            int count;
            int[] parent;
            // 这个 rank 我是希望，越矮越好；你现在，x 小就让大的跟小的，然后小的自增，这样会导致大家平权，事与愿违
            // 所以 rank 是谁 rank 高，谁当 root，而不是反过来
            int[] rank;
            UnionFind(char[][] grid) {
                int m = grid.length, n = grid[0].length;
                parent = new int[m * n];
                rank = new int[m * n];
    
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (grid[i][j] == '1') {
                            count++;
                            int v = i * n + j;
                            parent[v] = v;
                        }
                    }
                }
            }
            
            // unite 是合并两个值，所以参数是两个
            void unite(int x, int y) {
                int rx = find(x), ry = find(y);
                if (rx != ry) {
                    if (rank[rx] < rank[ry]) {
                        int t = rx; rx = ry; ry = t;
                    }
                    parent[ry] = rx;
                    if (rank[rx] == rank[ry]) rank[rx]++;
                    count--;
                }
            }
            
            int find(int x) {
                if (x != parent[x]) {
                    parent[x] = find(parent[x]);
                }
                return parent[x];
            }
            
            int count() {
                return count;
            }
        }
    }
    
    /**
     * 这个是 bfs...
     *
     * 没意思就这？
     * 还是被我磨出来了
     * */
    public static class my_bfs {
    
        public int numIslands(char[][] grid) {
            int m = grid.length, n = grid[0].length;
            boolean[][] visited = new boolean[m][n];
            int count = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (bfs(grid, visited, i, j)) {
                        count++;
                    }
                }
            }
            return count;
        }
    
        boolean bfs(char[][] grid, boolean[][] visited, int i, int j) {
            if (visited[i][j] || grid[i][j] == '0') return false;
            Queue<int[]> q = new LinkedList<>();
            q.offer(new int[] {i, j});
            while (!q.isEmpty()) {
                int[] next = q.poll();
                int x = next[0], y = next[1];
                if (x >= grid.length || x < 0 || y >= grid[0].length || y < 0 || visited[x][y] || grid[x][y] == '0')
                    continue;
                visited[x][y] = true;
                q.offer(new int[] {x, y + 1});
                q.offer(new int[] {x + 1, y});
                q.offer(new int[] {x, y - 1});
                q.offer(new int[] {x - 1, y});
            }
            return true;
        }
    }
    
    
    public static class my_dfs {
    
        /**
         * 我以为我写的是 bfs 结果写的是 dfs
         * 明天再说吧，三道题做了两个半钟头！
         * @since  2022年5月6日23:25:17
         * */
        public int numIslands(char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            boolean[][] visited = new boolean[m][n];
            int count = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (dfs(grid, visited, i, j)) {
                        count++;
                    }
                }
            }
            return count;
        }
    
        boolean dfs(char[][] grid, boolean[][] visited, int i, int j) {
            if (i >= grid.length || j >= grid[0].length || i < 0 || j < 0 || visited[i][j] || grid[i][j] == '0')
                return false;
            visited[i][j] = true;
            // 一开始我这几个，用或连起来了，不知道怎么想的，生搬硬套！
            dfs(grid, visited, i, j + 1);
            dfs(grid, visited, i + 1, j);
            dfs(grid, visited, i, j - 1);
            dfs(grid, visited, i - 1, j);
            return true;
        }
    }
}
