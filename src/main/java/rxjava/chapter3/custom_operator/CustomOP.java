package rxjava.chapter3.custom_operator;

import org.apache.commons.lang3.tuple.Pair;
import rx.Observable;

public class CustomOP {
    public static void main(String[] args) {
        Observable<Boolean> trueFalse = Observable.just(true, false).repeat();
        Observable<Integer> upstream = Observable.range(0, 101);

        // apache commons lang3 라이브러리 사용
        Observable<Integer> downstream = upstream.zipWith(trueFalse, Pair::of)
                .filter(Pair::getRight)
                .map(Pair::getLeft);
        downstream.subscribe(System.out::println);

        // 써드파티 라이브러리를 사용하지 않고 구현
        Observable<?> downStream2 = upstream.zipWith(trueFalse, (i, bool) -> bool ? Observable.just(i) : Observable.empty())
                .flatMap(observable -> observable);
        downStream2.subscribe(System.out::println);

        // 유틸리티 메서드를 활용
        CustomOP.odd(upstream).subscribe(System.out::println);

        // compose 연산자를 활용
        upstream.compose(odd()).subscribe(System.out::println);
    }

    /**
     *  사용자 정의 연산자
     */
    private static Observable.Transformer<Integer, Integer> odd() {
        Observable<Boolean> trueFalse = Observable.just(true, false).repeat();
        return upstream -> upstream.zipWith(trueFalse, Pair::of)
                .filter(Pair::getRight)
                .map(Pair::getLeft);
    }

    /**
     * 유틸리티 메서드
     */
    static <T> Observable<T> odd(Observable<T> upstream) {
        Observable<Boolean> trueFalse = Observable.just(true, false).repeat();
        return upstream.zipWith(trueFalse, Pair::of)
                .filter(Pair::getRight)
                .map(Pair::getLeft);
    }
}
