package treasure.moka;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 写一个函数：
 * 1、输入，两个有序的数组（正序）
 * 2、输出，两个数组合并后的有序数组（正序）
 * 要求：
 * 1、两个数组中，有重复的数据，要过滤掉
 *
 * （不考虑输入输出）
 * 用例一：
 * 输入：
 * 1,2,4,5,6,8
 * 2,8,34,56
 * 输出：
 * 1,2,4,5,6,8,34,56
 * 用例二：
 * 输入：
 * 1,3,5,7
 * 2,4,5,7,8
 * 输出：
 * 1,2,3,4,5,7,8
 * */
public class Round3 {
    
    public int[] mergeArray(int[] a, int[] b) {
        int i = 0, j = 0;
        int n1 = a.length, n2 = b.length;
        if(n1 == 0) return b;
        if(n2 == 0) return a;
        List<Integer> res = new ArrayList<>();
        while(i < n1 || j < n2) {
            int v1 = i == n1 ? Integer.MAX_VALUE : a[i];
            int v2 = j == n2 ? Integer.MAX_VALUE : b[j];
            int value;
            if(v1 < v2) {
                value = v1;
                i++;
            } else {
                value = v2;
                j++;
            }
            if(res.size() == 0 || res.get(res.size() - 1) < value) {
                res.add(value);
            }
        }
        return res.stream().mapToInt(Integer::valueOf).toArray();
    }
}
