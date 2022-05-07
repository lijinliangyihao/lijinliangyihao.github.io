package treasure.alg.top100;

import java.util.*;


public class _347_first_k_high_frequency_element {
    
    /**
     * 这个题不太简单，可能有数量一样的数，我的 treemap 计划泡汤了
     * 后来构造一个节点放到 list 里排序，取，搞定了
     *
     * 一段时间后我看到这个解法，感觉非常的傻屄，还 treemap
     * 难道我当时神志不清？
     *
     * */
    public static class count_and_sort_all {
        public int[] topKFrequent(int[] nums, int k) {
            
            class pair {
                int num;
                int count;
            }
            
            Map<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            }
            
            List<pair> list = new ArrayList<>();
            
            map.forEach((e, v) -> {
                pair p = new pair();
                p.num = e; p.count = v;
                list.add(p);
            });
            
            list.sort((a, b) -> b.count - a.count);
        
            int[] res = new int[k];
            for (int i = 0; i < k; i++) {
                res[i] = list.get(i).num;
            }
            return res;
        }
    }
    
    public static class priority_queue {
    
        /**
         * 也是数每个数字的个数，这个必不可少，然后优先队列处理
         * */
        public int[] topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            }
            PriorityQueue<int[]> q = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
            map.forEach((e, v) -> {
                q.offer(new int[] {e, v});
                if (q.size() > k)
                    q.poll();
            });
            int[] res = new int[k];
            for (int i = 0; i < k; i++) {
                res[i] = q.poll()[0];
            }
            return res;
        }
    }
    
    public static class quick_select {
        /**
         * 你非常擅长的快速选择排序
         * 也是得先统计出来每个数字出现几次
         *
         * 这个确实比优先队列快了一倍
         *
         * */
        public int[] topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            }
            List<int[]> list = new ArrayList<>();
            map.forEach((e, c) -> list.add(new int[] {e, c}));
            quickSelect(list, 0, list.size() - 1, k);
            int[] res = new int[k];
            for (int i = 0; i < k; i++) {
                res[i] = list.get(i)[0];
            }
            return res;
        }
    
        public static void main(String[] args) {
            int[] ints = new quick_select().topKFrequent(new int[]{4, 1, -1, 2, -1, 2, 3}, 2);
            System.out.println(Arrays.toString(ints));
        }
        /**
         * 我只会写这一种较快的快排，另一种较慢的也得会写！基于交换的
         * */
        void quickSelect(List<int[]> list, int from, int to, int k) {
            class swapper {
                static void swap(List<int[]> list, int from, int to){
                    if (from == to) return;
                    int[] t = list.get(from);
                    list.set(from, list.get(to));
                    list.set(to, t);
                }
            }
            if (from >= to) return;
    
            int l = from + 1;
            for (int i = l; i <= to; i++) {
                if (list.get(i)[1] >= list.get(from)[1]) {
                    swapper.swap(list, l++, i);
                }
            }
            // 我在 2022年5月7日19:58:18 被卡了将近两个小时
            // 一开始我还想，为什么还得交换一次？后来一想，操他妈的，可不吗，中轴数据，得放到最后的索引
            // 这样才能保证它在正确的位置！
            // 好好想想吧，长脑子的一想就能明白
            swapper.swap(list, l - 1, from);
            
            if (l == k) return;
            if (l < k)
                quickSelect(list, l, to, k);
            else
                quickSelect(list, from, l - 2, k);
        }
        
        void quickSelect2(List<int[]> list, int from, int to, int k) {
            if (from >= to) return;
            int[] pivot = list.get(from);
            int l = from, r = to;
            while (l < r) {
                while (l < r && list.get(r)[1] >= pivot[1]) r--;
                list.set(l, list.get(r));
                while (l < r && list.get(l)[1] <= pivot[1]) l++;
                list.set(r, list.get(l));
            }
            list.set(l, pivot);
            if (l == k) return;
            if (l < k)
                quickSelect(list, l + 1, to, k);
            else
                quickSelect(list, from, l - 1, k);
        }
        
        
    }
    
}
