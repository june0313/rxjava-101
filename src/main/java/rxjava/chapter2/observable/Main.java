package rxjava.chapter2.observable;

import rx.Observable;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // 하나의 값을 방출하고 종료하는 Observable
        Observable.just(1).subscribe(x -> System.out.println(x));

        // 2개 ~ 9개까지 인자로 취할 수 있다.
        Observable.just(1, 3, 5, 7, 9).subscribe(i -> System.out.println(i));

        // 컬렉션을 받아 컬렉션의 값을 순차적으로 방출
        Observable.from(Arrays.asList(1, 2, 3)).subscribe(i -> System.out.println(i));

        // start 부터 count 만큼 방출
        Observable.range(5, 3).subscribe(i -> System.out.println(i));
        Observable.range(1, 100).filter(i -> i % 33 == 0).subscribe(i -> System.out.println(i));

        Observable.empty();
        Observable.never();
        Observable.error(new RuntimeException()).subscribe(
                i -> System.out.println(i),
                error -> System.out.println(error.getMessage())
        );

    }
}
