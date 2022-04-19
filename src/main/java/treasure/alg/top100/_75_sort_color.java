package treasure.alg.top100;

public class _75_sort_color {
    
    /**
     * 两趟处理，一趟把所有 0 放到开头，一趟把所有 1 放到 0 末尾
     * */
    public static class SinglePointer {
    
        public void sortColors(int[] nums) {
            int z = 0;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == 0)
                    swap(nums,i, z++);
            }
            for (int i = z; i < nums.length; i++) {
                if (nums[i] == 1)
                    swap(nums, i, z++);
            }
        }
        void swap(int[] nums, int i, int j) {
            if (i != j) {
               int t = nums[i];
               nums[i] = nums[j];
               nums[j] = t;
            }
        }
    }
    
    /**
     * 这个解法，两个指针都从 0 开始
     * 但是移动规则不好想，我是没想到的
     * */
    public static class DoublePointer1 {
    
        public void sortColors(int[] nums) {
            int p0 = 0, p1 = 0;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == 1) {
                    swap(nums, i, p1++);
                } else if (nums[i] == 0) {
                    swap(nums, i, p0);
                    // 下面这四行代码是人能想出来的吗？
                    
                    // 小，说明 p0 指向一个 1，交换完 1 跑到 i 那了，所以得继续交换一次 p1 和 i
                    if (p0 < p1)
                        swap(nums, p1, i);
                    p0++;
                    // 这个保证了 p1 最差和 p0 相等，不会小于 p0，这样不会覆盖数据，妙！
                    p1++;
                }
            }
        }
        void swap(int[] nums, int i, int j) {
            if (i != j) {
                int t = nums[i];
                nums[i] = nums[j];
                nums[j] = t;
            }
        }
    }
    
    /**
     * 这个双指针好多了
     * 但是有点细节，这个细节我是想不到的
     * */
    public static class DoublePointer2 {
        public void sortColors(int[] nums) {
            int p0 = 0, p2 = nums.length - 1;
            // 这得取到等号，因为还要和 p0 比较
            for (int i = 0; i <= p2; i++) {
                // 因为可能 p2 本来就指向 2，所以交换完还得验证一发
                while (i < p2 && nums[i] == 2) {
                    swap(nums, i, p2--);
                }
                // 判断完 p2 接着判断 p0，不是互斥的
                if (nums[i] == 0) {
                    swap(nums, i, p0++);
                }
            }
        }
        void swap(int[] nums, int i, int j) {
            if (i != j) {
                int t = nums[i];
                nums[i] = nums[j];
                nums[j] = t;
            }
        }
    }
    
    /**
     * 我的笨逼解法
     * */
    public static class MyStupidSolution {
    
        public void sortColors(int[] nums) {
            int end = sort0(nums, 0, nums.length - 1);
            sort2(nums, end, nums.length - 1);
        }
    
        int sort0(int[] nums, int l, int r) {
            while (l < r) {
                while (l < r && nums[r] > 0) r--;
                while (l < r && nums[l] == 0) l++;
                if(nums[l] != nums[r]) {
                    int t = nums[l]; nums[l] = nums[r]; nums[r] = t;
                }
            }
            return l;
        }
    
        int sort2(int[] nums, int l, int r) {
            while (l < r) {
                while (l < r && nums[r] == 2) r--;
                while (l < r && nums[l] < 2) l++;
                if(nums[l] != nums[r]) {
                    int t = nums[l]; nums[l] = nums[r]; nums[r] = t;
                }
            }
            return l;
        }
    }
}
