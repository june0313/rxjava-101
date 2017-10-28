package rxjava.chapter2.observable;

import rx.Observable;

public class ErrorPropagation {
    public static void main(String[] args) {

    }

    Observable<Integer> rxload(int id) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(id);
                subscriber.onCompleted();
            } catch (Exception e) {
                // 에러 (Throwable)을 로깅하거나 다시 던지는 대신 전파해야 한다.
                // 에러를 일급객체로 다루어야 한다.
                subscriber.onError(e);
            }
        });
    }

    Observable<Integer> rxLoad2(int id) {
        // fromCallable : try-catch 문장으로 감싸는 구현이 되어있는 내장 연산자
        return Observable.fromCallable(() -> id);
    }
}
