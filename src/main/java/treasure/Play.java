package treasure;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

public class Play {
    
    public static void main(String[] args) throws Exception {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
    
        Flow.Processor p ;
        
        publisher.subscribe(new Flow.Subscriber<>() {
            Subscription sub;
            @Override
            public void onSubscribe(Subscription subscription) {
                sub = subscription;
                sub.request(1);
            }
    
            @Override
            public void onNext(Integer item) {
                System.out.println(item);
                sub.request(1);
            }
    
            @Override
            public void onError(Throwable throwable) {
        
            }
    
            @Override
            public void onComplete() {
        
            }
        });
        
        publisher.submit(11);
        publisher.submit(11);
        publisher.submit(11);
        publisher.submit(11);
        publisher.submit(11);
    
        Thread.sleep(999999);
    }
}
