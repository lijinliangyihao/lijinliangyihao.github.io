package treasure.alg.top100;

import java.util.*;

/**
 * 准备直接看答案了，不知道为什么
 * 可能是太菜了！
 *
 * 不要脸了，开始摆烂了！
 *
 * */
public class _207_class_schedule {
    
    public static class bfs_topological_sort {
    
        /**
         * 主要是创建图，这一块的，方方面面的
         * 傻逼
         *
         * */
        public boolean canFinish(int numCourses, int[][] prerequisites) {
            int[] indegrees = new int[numCourses];
            
            List<List<Integer>> graph = new ArrayList<>();
            // 我这被坑了一手：我想用 collections.fill，但是这个要求 list 必须有长度，空的不行
            // 我又找到 ncopies，发现他妈的还真是 copies，全是拷贝共用同一个 list
            // 被迫 for 循环，去你妈的！
            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }
            
            Queue<Integer> q = new LinkedList<>();
    
            for (int[] each : prerequisites) {
                indegrees[each[0]]++;
                graph.get(each[1]).add(each[0]);
            }
    
            for (int i = 0; i < numCourses; i++) {
                if (indegrees[i] == 0)
                    q.offer(i);
            }
            
            while (!q.isEmpty()) {
                Integer from = q.poll();
                numCourses--;
                for (int to : graph.get(from)) {
                    if (--indegrees[to] == 0)
                        q.offer(to);
                }
            }
            
            return numCourses == 0;
        }
    }
    
    /**
     * 这个比 bfs 要更快一些
     * */
    public static class dfs {
        
        /**
         * 这个代码简单，但是有门槛
         * bfs 用到一个入度数组，这里需要一个状态数组
         * flag: 0 初始
         *       1 被自己访问过，碰到说明有环，返回 false
         *      -1 被分支访问过，碰到 -1 返回 true
         * */
        public boolean canFinish(int numCourses, int[][] prerequisites) {
            int[] flags = new int[numCourses];
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] each : prerequisites) {
                graph.get(each[1]).add(each[0]);
            }
            for (int i = 0; i < numCourses; i++) {
                if (!helper(graph, flags, i))
                    return false;
            }
            return true;
        }
        
        boolean helper(List<List<Integer>> graph, int[] flags, int i) {
            if (flags[i] == -1)
                return true;
            if (flags[i] == 1)
                return false;
            flags[i] = 1;
            for (int each : graph.get(i)) {
                if (!helper(graph, flags, each))
                    return false;
            }
            flags[i] = -1;
            return true;
        }
    }
}
