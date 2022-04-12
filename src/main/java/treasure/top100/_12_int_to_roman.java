package treasure.top100;

/**
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 *
 * 一般大数跟小数，但是有以下特殊情况，小数放大数左边
 *
 * I可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
 * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
 * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900
 *
 *
 * */
public class _12_int_to_roman {
    
    public static class Enumerate {
        
        // 全枚举出来一把梭哈
        public String intToRoman(int num) {
            String[] _1000 = {"", "M", "MM", "MMM"};
            String[] _100 = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
            String[] _10 = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
            String[] _1 = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
            return _1000[num / 1000] + _100[num % 1000 / 100] + _10[num % 100 / 10] + _1[num % 10];
        }
    }
    
    public static class Mapping {
        public String intToRoman(int num) {
            int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
            String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                int c = num / values[i];
                while(c-- > 0) sb.append(symbols[i]);
                num %= values[i];
                if(num == 0) break;
            }
            return sb.toString();
        }
    }
    
    /**
     * 这个解法我称之为 小机灵鬼
     * */
    public static class xiao_ji_ling_gui {
        public String intToRoman(int num) {
            StringBuilder sb = new StringBuilder();
            while(num >= 1000) {
                sb.append("M");
                num -= 1000;
            }
            if(num >= 900) {
                sb.append("CM");
                num -= 900;
            }
            while(num >= 500) {
                sb.append("D");
                num -= 500;
            }
            if(num >= 400) {
                sb.append("CD");
                num -= 400;
            }
            while(num >= 100) {
                sb.append("C");
                num -= 100;
            }
            if(num >= 90) {
                sb.append("XC");
                num -= 90;
            }
            while(num >= 50) {
                sb.append("L");
                num -= 50;
            }
            if(num >= 40) {
                sb.append("XL");
                num -= 40;
            }
            while(num >= 10) {
                sb.append("X");
                num -= 10;
            }
            if(num == 9) {
                sb.append("IX");
                num -= 9;
            }
            while(num >= 5) {
                sb.append("V");
                num -= 5;
            }
            if(num == 4) {
                sb.append("IV");
                num -= 4;
            }
            while(num > 0) {
                sb.append("I");
                num--;
            }
            return sb.toString();
        }
    }
}
