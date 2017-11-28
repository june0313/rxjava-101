package rxjava.chapter6.periodic_sampling;

import rx.Observable;
import rxjava.chapter4._5_concurrency.SleepUtil;

import java.util.concurrent.TimeUnit;

public class Sampling {
    public static void main(String[] args) {
        Sampling sampling = new Sampling();
//        sampling.sample1();
        sampling.sample2();

        // 메인스레드 대기
        SleepUtil.sleep(90000);
    }

    private void sample1() {
        long startTime = System.currentTimeMillis();
        Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .timestamp()
                .sample(1, TimeUnit.SECONDS)
                .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
                .take(5)
                .subscribe(System.out::println);
    }

    private void sample2() {
        Observable<String> names = Observable.just(
                "Mary", "Wayne", "Ozil",
                "Conan",
                "Jaime", "James", "Yanoon", "Panda",
                "Hong2", "Suri");

        Observable<Long> absoluteDelayMillis = Observable.just(
                0.1, 0.6, 0.9,
                1.1,
                3.3, 3.4, 3.5, 3.6,
                4.4, 4.8)
                .map(d -> (long) (d * 1_000));

        Observable<String> delayedNames = names.zipWith(absoluteDelayMillis,
                (n, d) -> Observable.just(n).delay(d, TimeUnit.MILLISECONDS))
                .flatMap(o -> o);

//        delayedNames.sample(1, TimeUnit.SECONDS)
//                .subscribe(System.out::println);
//        delayedNames.throttleLast(1, TimeUnit.SECONDS)
//                .subscribe(System.out::println);
        delayedNames.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);

    }
}
