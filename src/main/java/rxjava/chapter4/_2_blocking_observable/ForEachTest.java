package rxjava.chapter4._2_blocking_observable;

import rx.Observable;
import rxjava.chapter4._5_concurrency.SleepUtil;

public class ForEachTest {
    public static void main(String[] args) {
        ForEachTest forEachTest = new ForEachTest();

        forEachTest.blocking();
        forEachTest.nonBlocking();
    }

    private void blocking() {
        baseObservable().toBlocking().subscribe(System.out::println);
    }

    private void nonBlocking() {
        baseObservable().subscribe(System.out::println);
    }

    private Observable<Integer> baseObservable() {
        return Observable.create(subscriber -> {
            SleepUtil.sleep(1000);
            subscriber.onNext(1);
            SleepUtil.sleep(1000);
            subscriber.onNext(2);
            SleepUtil.sleep(1000);
            subscriber.onNext(3);

            subscriber.onCompleted();
        });
    }
}
