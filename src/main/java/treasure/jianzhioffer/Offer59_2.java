package treasure.jianzhioffer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 请定义一个队列并实现函数 max_value 得到队列里的最大值，
 * 要求函数max_value、push_back 和 pop_front 的均摊时间复杂度都是O(1)。
 *
 * 若队列为空，pop_front 和 max_value 需要返回 -1
 *
 * */
public class Offer59_2 {
    
    /**
     * 这个真有点难，想了半天也没想出来怎么搞
     * */
    public static class MaxQueue {
    
        Queue<Integer> q = new LinkedList<>();
        LinkedList<Integer> maxQueue = new LinkedList<>();
        public int max_value() {
            if(q.isEmpty()) return -1;
            return maxQueue.peek();
        }
    
        public void push_back(int value) {
            q.offer(value);
            while(!maxQueue.isEmpty() && maxQueue.peekLast() < value) {
                maxQueue.pollLast();
            }
            maxQueue.offer(value);
        }
    
        public int pop_front() {
            if(q.isEmpty()) return -1;
            int ans = q.poll();
            
            /*
            * 问题就处在这了，相等就弹出，这个我怎么就没想到
            * 一直想着怎么把它的索引记住
            * 好像确实没有必要
            * */
            if(ans == maxQueue.peek()) maxQueue.poll();
            return ans;
        }
    }
    
}
