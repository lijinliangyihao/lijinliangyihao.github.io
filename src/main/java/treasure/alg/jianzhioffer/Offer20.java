package treasure.alg.jianzhioffer;

/**
 * 实在鸡巴写不出来，抄《剑指 offer》吧
 * */
public class Offer20 {
    
    /*
        若干空格
        一个 小数 或者 整数
        （可选）一个 'e' 或 'E' ，后面跟着一个 整数
        若干空格
        -1.1111e+111
        1.
        .1
    * */
    
    String s;
    int index;
    public boolean isNumber(String s) {
        s = s.trim();
        this.s = s;
        boolean numeric = scanInteger();
        char c = readChar();
        if(c == '.') {
            index++;
            numeric = numeric || scanUnsigned();
        }
        c = readChar();
        if(c == 'e' || c == 'E') {
            index++;
            numeric = numeric && scanInteger();
        }
        return numeric && index == s.length();
    }
    
    char readChar() {
        return index < s.length() ? s.charAt(index) : 0;
    }
    
    boolean scanInteger() {
        char c = readChar();
        if(c == '+' || c == '-') index++;
        return scanUnsigned();
    }
    
    boolean scanUnsigned() {
        char c = readChar();
        int oldIndex = index;
        while(index < s.length() && c <= '9' && c >= '0') {
            index++;
            c = readChar();
        }
        return index - oldIndex > 0;
    }

}
