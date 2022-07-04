package treasure.alg.top100;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个题做错了我是没想到的
 * 难道我只配拿 13k
 * */
public class _1_two_sum {
    
    // 做这个题就是浪费我时间
    public int[] twoSum_wrong(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i]) && map.containsKey(target - nums[i])) {
                return new int[] {map.get(nums[i]), map.get(target - nums[i])};
            } else {
                map.put(nums[i], i);
            }
        }
        return null;
    }// 万万没想到，做错了
    
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[] {i, map.get(target - nums[i])};
            }
            map.put(nums[i], i);
        }
        return null;
    }
}
