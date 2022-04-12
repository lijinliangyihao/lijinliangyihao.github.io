package treasure;

import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Play {
    
    public static void main(String[] args) throws Exception {
    
        Exchanger<String> e = new Exchanger<>();
        
        Thread t1 = new Thread(() -> {
            try {
                String res = e.exchange("caonima");
                System.out.println("t2 说：" + res);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                String res = e.exchange("滚你妈的");
                System.out.println("t1 说：" + res);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        
    }
}
