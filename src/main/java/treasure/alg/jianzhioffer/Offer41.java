package treasure.alg.jianzhioffer;

import java.util.PriorityQueue;

/**
 * 数据流的中位数
 * 标记是困难，其实两个堆就搞定了，听起来很简单！
 *
 * 一遍过，记忆力不错！
 * */
public class Offer41 {
    
    /*
    * 两个堆，一大一小
    * 让前面的比后面的可以多一个数
    * 取中位数，偶数的话都取，奇数的话从第一个堆取
    * */
    class MedianFinder {
        
        PriorityQueue<Integer> bigHeap = new PriorityQueue<>((a, b) -> b - a);
        PriorityQueue<Integer> littleHeap = new PriorityQueue<>();
        int size;
        
        public void addNum(int num) {
            if((size & 1) == 1) {
                bigHeap.offer(num);
                littleHeap.offer(bigHeap.poll());
            } else {
                // 偶数虽然要往最大堆放，但是可能它比最小堆里的数还要大
                // 所以得过一遍最小堆
                littleHeap.offer(num);
                bigHeap.offer(littleHeap.poll());
            }
            size++;
        }
        
        public double findMedian() {
            if((size & 1) == 1) return bigHeap.peek();
            return (bigHeap.peek() + littleHeap.peek()) / 2.0;
        }
    }
    
}
