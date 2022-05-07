package treasure.alg.top100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class _56_merge_section {
    
    public static class mine {
        /**
         * 我有一种感觉，排完序遍历一遍就行了
         * [1,3],[2,6],[8,10],[15,18]
         * 1 3
         *   3 7
         *  2  6
         *       8  10
         *              15  18
         *
         * */
        public int[][] merge(int[][] intervals) {
            Arrays.sort(intervals, (a, b) -> {
                int c = a[0] - b[0];
                return c == 0 ? a[1] - b[1] : c;
            });
            List<int[]> res = new ArrayList<>();
            int[] last = intervals[0];
            for (int i = 1; i < intervals.length; i++) {
                if (last[1] >= intervals[i][0]) {
                    if (last[1] < intervals[i][1])
                        last[1] = intervals[i][1];
                }
                else {
                    res.add(last);
                    last = intervals[i];
                }
            }
            res.add(last);
            return res.toArray(new int[0][]);
        }
    }
    
    /**
     * 一个叫老汤的，他肯定是花钱买广告了
     * 他这个没我的快
     * */
    public static class old_tang {
        public int[][] merge(int[][] intervals) {
            Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
            List<int[]> res = new ArrayList<>();
            for (int[] interval : intervals) {
                if (res.isEmpty())
                    res.add(interval);
                else {
                    int[] last = res.get(res.size() - 1);
                    if (interval[0] > last[1])
                        res.add(interval);
                    else
                        last[1] = Math.max(last[1], interval[1]);
                }
            }
            return res.toArray(new int[0][]);
        }
    
        // 稍作改动，变得不好理解了！
        public int[][] merge2(int[][] intervals) {
            Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
            List<int[]> res = new ArrayList<>();
            for (int[] interval : intervals) {
                int[] last;
                if (res.isEmpty() || (last = res.get(res.size() - 1))[1] < interval[0])
                    res.add(interval);
                else {
                    last[1] = Math.max(last[1], interval[1]);
                }
            }
            return res.toArray(new int[0][]);
        }
    }
}
