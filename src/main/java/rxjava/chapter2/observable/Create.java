package rxjava.chapter2.observable;

import rx.Observable;

public class Create {
    public static void main(String[] args) {
        log("before");
        Observable.range(5, 3).subscribe(i -> log(i));
        log("after");


        Observable<Object> ints = Observable.create(subscriber -> {
            log("Crated");
            subscriber.onNext(5);
            subscriber.onNext(6);
            subscriber.onNext(7);
            subscriber.onCompleted();
            log("Completed");
        }).cache();

        // 여러개의 구독자 관리하기
        log("Starting");
        ints.subscribe(i -> log(i));
        ints.subscribe(i -> log(i));
        ints.subscribe(i -> log(i));
        log("Exit");

        range(2, 5).subscribe(i -> log(i));

    }

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }

    static <T> Observable<T> never() {
        return Observable.create(subscriber -> {
        });
    }

    static <T> Observable<T> emtpy() {
        return Observable.create(subscriber -> subscriber.onCompleted());
    }

    static Observable<Integer> range(int start, int count) {
        return Observable.create(subscriber -> {
            for (int i = start; i < start + count; i++) {
                subscriber.onNext(i);
            }
            subscriber.onCompleted();
        });
    }
}
