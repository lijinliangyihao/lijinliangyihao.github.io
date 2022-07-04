package hyy;

public class _0704_LongestCommonPrefix {

    public static void main(String[] args) {
        String[] strs = {"flower","flow","flight"};
        String prefix = longestCommonPrefix(strs);
        System.out.println(prefix);
    }

    /**
     * 横向扫描： LCP(s1...sn) = LCP(LCP(LCP(s1,s2),s3),...sn)
     * 1、依次遍历字符串数组中的每个字符串,对于每个遍历到的字符串,更新最长公共前缀
     * 2、当遍历完所有的字符串以后,即可得到字符串数组中的最长公共前缀
     * 3、如果未遍历完所有字符串,最长公共前缀已经是空串,则最长公共前缀一定是空串,因此不需要继续遍历剩下的字符串,直接返回空串
     * @param strs 字符串数组
     * @return 返回最长公共前缀
     */
    public static String longestCommonPrefix(String[] strs) {
        if(strs == null || strs.length == 0){
            return "";
        }
        String prefix = strs[0];
        for(int i = 1; i < strs.length; i++) {
            prefix = longestCommonPrefix(prefix,strs[i]);
            if(prefix.length() == 0) {
                break;
            }
        }
        return prefix;
    }


    /**
     * 两个字符串,取最长公共前缀
     * 1、逐个字符比较
     * 2、字符一致,继续往下比较,否则,以当前字符所在位置为末尾位置截取字符串
     * @param str1 字符串1
     * @param str2 字符串2
     * @return String
     */
    public static String longestCommonPrefix(String str1, String str2) {
        int length = Math.min(str1.length(),str2.length());
        int index = 0;
        while(index < length && str1.charAt(index) == str2.charAt(index)) {
            index ++;
        }
        return str1.substring(0,index);
    }
}
