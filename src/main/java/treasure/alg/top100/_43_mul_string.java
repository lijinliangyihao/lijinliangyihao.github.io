package treasure.alg.top100;

public class _43_mul_string {
    
    public static class mine {
    
        public String multiply(String num1, String num2) {
            int l1 = num1.length(), l2 = num2.length();
            if (l1 == 1 && num1.charAt(0) == '0' || l2 == 1 && num2.charAt(0) == '0')
                return "0";
            int[] arr = new int[l1 + l2];
        /*
            1111
             111
         */
            for (int i = l2 - 1; i >= 0; i--) {
                int num = num2.charAt(i) - '0';
                int carry = 0;
                int pos = l1 + i;
                // j 遍历的是 l1
                for (int j = l1 - 1; j >= 0; j--) {
                    int tmp = carry + num * (num1.charAt(j) - '0') + arr[pos];
                    int rem = tmp % 10;
                    carry = tmp / 10;
                    arr[pos--] = rem;
                }
                if (carry != 0)
                    arr[pos] += carry;
            }
        
            int i = 0;
            while (arr[i] == 0)
                i++;
        
            StringBuilder sb = new StringBuilder();
            for (; i < arr.length; i++) {
                sb.append(arr[i]);
            }
            return sb.toString();
        }
    }
    
    public static class official {
    
        public String multiply(String num1, String num2) {
            if ("0".equals(num1) || "0".equals(num2))
                return "0";
            int m = num1.length(), n = num2.length();
            int[] arr = new int[m + n];
    
            for (int i = m - 1; i >= 0 ; i--) {
                int num = num1.charAt(i) - '0';
                for (int j = n - 1; j >= 0 ; j--) {
                    arr[i + j + 1] += num * (num2.charAt(j) - '0');
                }
            }
    
            for (int i = arr.length - 1; i > 0; i--) {
                arr[i - 1] += arr[i] / 10;
                arr[i] %= 10;
            }
            
            StringBuilder sb = new StringBuilder();
            int i = arr[0] == 0 ? 1 : 0;
            for (; i < arr.length; i++) {
                sb.append(arr[i]);
            }
            return sb.toString();
        }
    }
    
    /**
     * 官方题解，比较笨逼，但是可以提升你的编码水平
     *
     * 它不是性能高低的问题
     * 它是你知道这个解法，却不能正确写出来，那就是你还不会！
     *
     * 你只会写高性能的不会写低性能的这可能吗？如果你真的懂，那不可能！
     *
     * 如果你会的话，高性能低性能的都能写出来！
     *
     * */
    public static class official_benbi {
    
        public String multiply(String num1, String num2) {
            if ("0".equals(num1) || "0".equals(num2))
                return "0";
        
            int m = num1.length(), n = num2.length();
            String res = "0";
            for (int i = n - 1; i >= 0; i--) {
                StringBuilder mul = new StringBuilder();
                // 这一行是在对齐
                for (int j = n - 1; j > i; j--) {
                    mul.append('0');
                }
                // 这里忘了 - '0'
                int y = num2.charAt(i) - '0';
                int carry = 0;
                for (int j = m - 1; j >= 0; j--) {
                    int sum = carry + (num1.charAt(j) - '0') * y;
                    mul.append(sum % 10);
                    carry = sum / 10;
                }
                if (carry != 0)
                    mul.append(carry);
            
                res = add(res, mul.reverse().toString());
            }
            return res;
        }
    
        String add(String a, String b) {
            StringBuilder sb = new StringBuilder();
            int carry = 0;
            int l = a.length() - 1, l2 = b.length() - 1;
            while (l >= 0 || l2 >= 0 || carry > 0) {
                // 这里忘了 - '0'
                int v1 = l >= 0 ? a.charAt(l) - '0' : 0;
                int v2 = l2 >= 0 ? b.charAt(l2) - '0' : 0;
                int sum = v1 + v2 + carry;
                sb.append(sum % 10);
                carry = sum / 10;
                l--;
                l2--;
            }
            return sb.reverse().toString();
        }
        
    }
    
}
