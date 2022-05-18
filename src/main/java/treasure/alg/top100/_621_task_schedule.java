package treasure.alg.top100;

import java.util.*;

/**
 * 老规矩，直接看答案了
 */
public class _621_task_schedule {
    
    /**
     * 官方解法一
     */
    public int leastInterval_1(char[] tasks, int n) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char ch : tasks) {
            freq.put(ch, freq.getOrDefault(ch, 0) + 1);
        }
        
        // 任务种类数
        int m = freq.size();
        // 保存下次开始时间、剩余次数两个列表
        List<Integer> nextValid = new ArrayList<>();
        List<Integer> rest = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            int value = entry.getValue();
            nextValid.add(1);
            rest.add(value);
        }
        
        int time = 0;
        for (int i = 0; i < tasks.length; ++i) {
            ++time;
            
            // 下面找出来下次开始时间的最小值，得到下次开始时间
            int minNextValid = Integer.MAX_VALUE;
            for (int j = 0; j < m; ++j) {
                if (rest.get(j) != 0) {
                    minNextValid = Math.min(minNextValid, nextValid.get(j));
                }
            }
            time = Math.max(time, minNextValid);
            
            // 然后找，不是 0 的且已经开始的剩余次数最多的任务开始执行
            int best = -1;
            for (int j = 0; j < m; ++j) {
                if (rest.get(j) != 0 && nextValid.get(j) <= time) {
                    if (best == -1 || rest.get(j) > rest.get(best)) {
                        best = j;
                    }
                }
            }
            // 找到最合适的任务，更新它下次可执行时间
            nextValid.set(best, time + n + 1);
            // 更新最合适任务的剩余执行次数
            rest.set(best, rest.get(best) - 1);
        }
        
        return time;
    }
    
    /**
     * 我抄的官方解法一
     */
    public int leastInterval(char[] tasks, int n) {
        int[] dict = new int[26];
        for (char task : tasks) {
            dict[task - 'A']++;
        }
        List<Integer> nextStartTime = new ArrayList<>();
        List<Integer> remains = new ArrayList<>();
        for (int k : dict) {
            if (k > 0) {
                nextStartTime.add(1);
                remains.add(k);
            }
        }
        int time = 0;
        int taskTypes = remains.size();
        for (int i = 0; i < tasks.length; i++) {
            time++;
            
            int nextMin = Integer.MAX_VALUE;
            for (int j = 0; j < taskTypes; j++) {
                if (remains.get(j) > 0) {
                    nextMin = Math.min(nextMin, nextStartTime.get(j));
                }
            }
            time = Math.max(time, nextMin);
            
            int nextTask = -1;
            for (int j = 0; j < taskTypes; j++) {
                if (remains.get(j) > 0 && nextStartTime.get(j) <= time &&
                    (nextTask == -1 || remains.get(j) > remains.get(nextTask)))
                    nextTask = j;
            }
            
            nextStartTime.set(nextTask, time + n + 1);
            remains.set(nextTask, remains.get(nextTask) - 1);
        }
        return time;
    }
    
    /**
     * 官方解法二，直接看答案吧，代码很短，分析很长
     * 淦！
     */
    public int leastInterval_2(char[] tasks, int n) {
        int[] dict = new int[26];
        int maxCount = 0, maxTask = 0;
        for (char task : tasks) {
            dict[task - 'A']++;
        }
        
        // 我这这个 naive 的优化居然导致它快了些许，这是我没有想到的
        for (int count : dict) {
            if (count >= maxTask) {
                if (count == maxTask)
                    maxCount++;
                else {
                    maxTask = count;
                    maxCount = 1;
                }
            }
        }
        return Math.max(tasks.length, maxCount + (n + 1) * (maxTask - 1));
    }
    
    /**
     * 路人的解法，目前是已知最快的
     * 它和官方题解二是一样的我感觉
     * <p>
     * 不是我感觉，它就是一样的
     */
    public int leastInterval_greedy(char[] tasks, int n) {
        //统计每个任务出现的次数，找到出现次数最多的任务
        int[] hash = new int[26];
        for (char task : tasks) {
            hash[task - 'A']++;
        }
        Arrays.sort(hash);
        //因为相同元素必须有n个冷却时间，假设A出现3次，n = 2，任务要执行完，至少形成AXX AXX A序列（X看作预占位置）
        //该序列长度为
        int minLen = (n + 1) * (hash[25] - 1) + 1;
        
        //此时为了尽量利用X所预占的空间（贪心）使得整个执行序列长度尽量小，将剩余任务往X预占的空间插入
        //剩余的任务次数有两种情况：
        //1.与A出现次数相同，比如B任务最优插入结果是ABX ABX AB，中间还剩两个空位，当前序列长度+1
        //2.比A出现次数少，若还有X，则按序插入X位置，比如C出现两次，形成ABC ABC AB的序列
        //直到X预占位置还没插满，剩余元素逐个放入X位置就满足冷却时间至少为n
        for (int i = 24; i >= 0; --i) {
            if (hash[i] == hash[25])
                minLen++;
        }
        //当所有X预占的位置插满了怎么办？
        //在任意插满区间（这里是ABC）后面按序插入剩余元素，比如ABCD ABCD发现D之间距离至少为n+1，肯定满足冷却条件
        //因此，当X预占位置能插满时，最短序列长度就是task.length，不能插满则取最少预占序列长度
        return Math.max(minLen, tasks.length);
    }
}
