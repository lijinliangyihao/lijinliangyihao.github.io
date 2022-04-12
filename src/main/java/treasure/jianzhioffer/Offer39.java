package treasure.jianzhioffer;

/**
 * 蒙过去了！
 * */
public class Offer39 {
    
    public int majorityElement(int[] nums) {
        int n = nums.length;
        int number = nums[0];
        int count = 1;
        for (int i = 1; i < n; i++) {
            if(nums[i] == number) {
                count++;
            } else {
                if(count == 0) {
                    count = 1;
                    number = nums[i];
                } else {
                    count--;
                }
            }
        }
        return number;
    }

}
