package rxjava.chapter2.observable;

import rx.Observable;
import rx.Subscription;

import java.math.BigInteger;

public class InfiniteStream {
    public static void main(String[] args) {

        Observable<BigInteger> naturalNumbers = Observable.create(subscriber -> {
            Runnable r = () -> {
                BigInteger i = BigInteger.ZERO;
                while (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(i);
                    i = i.add(BigInteger.ONE);
                }
            };

            new Thread(r).start();
        });

        Subscription subscribe = naturalNumbers.subscribe(x -> log(x));
        // 시간이 어느정도 지난 다음 구독을 해지한다.
        sleep(2000);
        subscribe.unsubscribe();
        System.out.println(Thread.currentThread().getName());
    }

    private static void sleep(int sec){
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
