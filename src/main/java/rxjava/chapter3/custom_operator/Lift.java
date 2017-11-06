package rxjava.chapter3.custom_operator;

import rx.Observable;
import rx.Subscriber;

/**
 * lift()로 고급 연산자 만들기
 */
public class Lift {
    public static void main(String[] args) {
        Observable
                .range(1, 1000)
                .filter(x -> x % 3 == 0)
                .distinct()
                .reduce((a, x) -> a + x)
                .map(Integer::toHexString)
                .subscribe(System.out::println);

        /////////////////////////////////////////////

        // 모든 홀수번 요소의 toString()을 방출하는 연산자 구현하기
        Observable
                .range(1, 9)
                .lift(toStringOfOdd())
                .subscribe(System.out::println);

        // 내장 연산자로 구현
        Observable
                .range(1, 9)
                .buffer(1, 2)
                .concatMapIterable(x -> x)
                .map(Object::toString)
                .subscribe(System.out::println);
    }

    private static <T> Observable.Operator<String, T> toStringOfOdd() {
        return new Observable.Operator<String, T>() {
            private boolean odd = true;

            @Override
            public Subscriber<? super T> call(Subscriber<? super String> child) {
                return new Subscriber<T>(child) {
                    @Override
                    public void onCompleted() {
                        child.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        if (odd) {
                            child.onNext(t.toString());
                        } else {
                            request(1);
                        }

                        odd = !odd;
                    }
                };
            }
        };
    }
}
