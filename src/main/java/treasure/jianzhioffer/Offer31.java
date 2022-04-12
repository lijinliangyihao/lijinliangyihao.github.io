package treasure.jianzhioffer;

import java.util.Stack;

public class Offer31 {
    
    /**
     * 非常侥幸一次通过了
     * */
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        int pushIndex = 0;
        int popIndex = 0;
        int len = pushed.length;
        Stack<Integer> stack = new Stack<>();
        while(pushIndex < len) {
            while(pushIndex < len && pushed[pushIndex] != popped[popIndex]) {
                stack.push(pushed[pushIndex]);
                pushIndex++;
            }
            if(pushIndex == len) return false;
            popIndex++;
            pushIndex++;
            while(!stack.isEmpty() && stack.peek() == popped[popIndex]) {
                popIndex++;
                stack.pop();
            }
        }
        return popIndex == len;
    }
    
}
