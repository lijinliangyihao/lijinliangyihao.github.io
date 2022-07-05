package hyy;

public class _0705_LongestCommonPrefix {

    public static void main(String[] args) {
        String strs[] = {"flower","flow","flight"};

    }

    /**
     * 纵向扫描
     * 1、从前往后遍历所有字符串的每一列,比较相同列上的字符是否相同
     * 2、如果相同则继续对下一列进行比较,如果不相同则当前列不再属于公共前缀
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
        if(strs == null || strs.length == 0){
            return "";
        }

        //以字符串数组的第一个字符为基准
        int length = strs[0].length();
        for(int i = 0; i < length; i++) {
            int c = strs[0].charAt(i);
            //纵向遍历每个字符串
            for(int j=1;j<strs.length;j++) {
                //依次取出每个字符串i位置的字符,与之比较
                if(i == strs[j].length() || c != strs[j].charAt(i)) {
                    return strs[0].substring(0,i);
                }
            }
        }
        return strs[0];
    }
}
