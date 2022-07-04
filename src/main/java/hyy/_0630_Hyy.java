package hyy;

import java.util.HashMap;

/**
 * 2022.6.30
 */
public class _0630_Hyy {

    public static void main(String[] args) {
        int[] nums = {3,2,4};
        int target = 6;
        int[] results = twoSum2(nums,target);
        for(int result : results) {
            System.out.println(result);
        }
    }

    /**
     * 两数之和(暴力解法)
     * @param nums
     * @param target
     * @return int[]
     */
    public static int[] twoSum1(int[] nums, int target) {
        for(int i=0;i<nums.length;i++) {
            for(int j=i+1;j<nums.length;j++) {
                if(nums[i] + nums[j] == target) {
                    return new int[]{i,j};
                }
            }
        }
        return new int[0];
    }

    /**
     * 两数之和
     * @param nums
     * @param target
     * @return int[]
     */
    public static int[] twoSum2(int[] nums,int target) {
        HashMap<Integer,Integer> hashMap = new HashMap<>(nums.length);
        for(int i=0;i<nums.length;i++) {
            if(hashMap.containsKey(target-nums[i])) {
                return new int[] {hashMap.get(target-nums[i]),i};
            }
            hashMap.put(nums[i],i);
        }
        return new int[0];
    }


}
