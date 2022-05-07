package treasure.alg.top100;

import java.util.*;

/**
* 我被卡住了！
* */
public class _253_meeting_room_II {

    /**
     * 一开始以为是合并区间其实不然！
     * [0,30],[5,10],[15,20]
     * 0                30
     *  4  6
     *   5      10
     *       7 8
     *            15   20
     * 是有几处重叠，就得几个会议室！跟合并区间很类似
     * 还是先排序，然后一趟遍历搞定
     *
     * 虽然我的解法，结果是对的，但是非常的傻屄。
     * 我不希望再看到这种解法。
     * */
    public static class mine {
        public int minMeetingRooms(int[][] intervals) {
            Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
            int count = 1;
            LinkedList<int[]> q = new LinkedList<>();
            q.offer(intervals[0]);
            for (int i = 1; i < intervals.length; i++) {
                if (intervals[i][0] < q.peekLast()[1]) {
                    q.offer(intervals[i]);
                    // 想想这里是不是可以用优先队列来优化呢？
                    for (int j = 0; j < q.size(); j++) {
                        if (q.get(j)[1] <= intervals[i][0])
                            q.remove(j);
                    }
                    count = Math.max(q.size(), count);
                }
                // 如果用优先队列，这个 else 也可以省去了
                else {
                    q.pollLast();
                    q.offer(intervals[i]);
                }
            }
            return count;
        }
    }
    
    public static class min_heap {
    
        /**
         * 也是先排序
         * 然后有一个优先队列，这个队列维护会议结束时间；
         * 如果最早的结束时间比当前会议开始时间晚，当前会议结束时间入队；否则最早结束的会议结束时间出队
         * 当前会议因为刚开始，所以务必入队
         * 这导致一个问题，队列数量不变，或 +1
         * 最终队列长度就是答案
         * */
        public int minMeetingRooms(int[][] intervals) {
            Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
            PriorityQueue<Integer> q = new PriorityQueue<>();
            q.offer(intervals[0][1]);
            for (int i = 1; i < intervals.length; i++) {
                if (q.peek() <= intervals[i][0])
                    q.poll();
                q.offer(intervals[i][1]);
            }
            return q.size();
        }
    }
    
    public static class two_array {
    
        /**
         * 这个解法只能用巧妙来形容
         * */
        public int minMeetingRooms(int[][] intervals) {
            int n = intervals.length;
            int[] start = new int[n];
            int[] end = new int[n];
            for (int i = 0; i < intervals.length; i++) {
                start[i] = intervals[i][0];
                end[i] = intervals[i][1];
            }
            Arrays.sort(start);
            Arrays.sort(end);
            int i = 0, j = 0, room = 0;
            while (i < n) {
                boolean empty = false;
                if (start[i] >= end[j]) {
                    empty = true;
                    j++;
                }
                i++;
                if (!empty)
                    room++;
            }
            return room;
        }
    }
}
