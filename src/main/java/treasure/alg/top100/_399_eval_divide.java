package treasure.alg.top100;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个题我不可能做出来
 * 给我一亿光年我也做不出来
 *
 * 光年？
 *
 * 处理有传递性关系的问题，可以使用「并查集」
 *
 * 不吹不黑，真他吗难这道题
 *
 * */
public class _399_eval_divide {
    
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        int n = values.length;
        Uf uf = new Uf(n * 2);
        Map<String, Integer> map = new HashMap<>();
        int[] id = {0};
        for (int i = 0; i < n; i++) {
            List<String> equation = equations.get(i);
            String name1 = equation.get(0);
            String name2 = equation.get(1);
            map.computeIfAbsent(name1, k -> id[0]++);
            map.computeIfAbsent(name2, k -> id[0]++);
            double value = values[i];
            uf.union(map.get(name1), map.get(name2), value);
        }
    
        int size = queries.size();
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            List<String> query = queries.get(i);
            Integer id1 = map.get(query.get(0));
            Integer id2 = map.get(query.get(1));
            if (id1 == null || id2 == null)
                res[i] = -1.0;
            else {
                res[i] = uf.divide(id1, id2);
            }
        }
        return res;
    }
    
    public static class Uf {
        int[] parent;
        double[] weight;
        public Uf(int len) {
            parent = new int[len];
            weight = new double[len];
            for (int i = 0; i < len; i++) {
                parent[i] = i;
                weight[i] = 1.0;
            }
        }
        
        public void union(int x, int y, double value) {
            int rootX = find(x), rootY = find(y);
            if (rootX != rootY) {
                parent[rootX] = rootY;
                weight[rootX] = value * weight[y] / weight[x];
            }
        }
        
        public int find(int x) {
            int p = parent[x];
            if (p != x) {
                parent[x] = find(p);
                weight[x] *= weight[p];
            }
            return parent[x];
        }
        
        public double divide(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX != rootY)
                return -1.0;
            return weight[x] / weight[y];
        }
    }
}
