package hyy;

public class Hyy0701 {

    public static void main(String[] args) {
        int rev = reverse(123);
        System.out.println(rev);
    }

    /**
     * 数字反转
     * @param x
     * @return  int
     */
    public static int reverse(int x) {
        int rev = 0;
        while(x != 0) {
            if(rev < Integer.MIN_VALUE / 10 || rev > Integer.MAX_VALUE / 10) {
                return 0;
            }
            int digit = x % 10;
            x /= 10;
            rev = rev * 10 + digit;
        }
        return rev;
    }
}

