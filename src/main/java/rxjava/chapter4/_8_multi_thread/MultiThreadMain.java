package rxjava.chapter4._8_multi_thread;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rxjava.chapter4._5_concurrency.SleepUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MultiThreadMain {
    ExecutorService poolA = Executors.newFixedThreadPool(10, threadFactory("Scheduler-A"));
    ExecutorService poolB = Executors.newFixedThreadPool(10, threadFactory("Scheduler-B"));
    ExecutorService poolC = Executors.newFixedThreadPool(10, threadFactory("Scheduler-C"));

    Scheduler schedulerA = Schedulers.from(poolA);
    Scheduler schedulerB = Schedulers.from(poolB);
    Scheduler schedulerC = Schedulers.from(poolC);


    public static void main(String[] args) {
        MultiThreadMain main = new MultiThreadMain();
//        main.immediate();
//        main.trampoline();
//        main.subscribeOn();
//        main.observeOn();
//        main.observeOn2();
        main.delay();
        main.sleepOneSecond();
    }

    void immediate() {
        Scheduler scheduler = Schedulers.immediate();
        Scheduler.Worker worker = scheduler.createWorker();

        log.info("Main start");
        worker.schedule(() -> {
            log.info(" Outer start");
            sleepOneSecond();
            worker.schedule(() -> {
                log.info("  Inner start");
                sleepOneSecond();
                log.info("  Inner end");
            });
            log.info(" Outer end");
        });
        log.info("Main end");
        worker.unsubscribe();
    }

    void trampoline() {
        Scheduler scheduler = Schedulers.trampoline();
        Scheduler.Worker worker = scheduler.createWorker();

        log.info("Main start");
        worker.schedule(() -> {
            log.info(" Outer start");
            sleepOneSecond();
            worker.schedule(() -> {
                log.info("  Inner start");
                sleepOneSecond();
                log.info("  Inner end");
            });
            log.info(" Outer end");
        });
        log.info("Main end");
        worker.unsubscribe();
    }

    void subscribeOn() {
        log.info("Starting");
        final Observable<String> obs = simple();
        log.info("Created");
        obs.subscribeOn(Schedulers.computation())
                .map(String::toLowerCase)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        x -> log.info("Got " + x),
                        Throwable::printStackTrace,
                        () -> log.info("Completed"));
        log.info("Exiting");
    }

    void observeOn() {
        log.info("Starting");
        final Observable<String> obs = simple();
        log.info("Created");
        obs
                .doOnNext(x -> log.info("Found 1 : " + x))
                .observeOn(Schedulers.io())
                .doOnNext(x -> log.info("Found 2 : " + x))
                .subscribe(
                        x -> log.info("Got 1: " + x),
                        Throwable::printStackTrace,
                        () -> log.info("Completed")
                );
        log.info("Exiting");
    }

    void observeOn2() {
        log.info("Starting");
        final Observable<String> obs = simple();
        log.info("Created");
        obs
                .doOnNext(x -> log.info("Found 1 : " + x))
                .observeOn(schedulerA)
                .doOnNext(x -> log.info("Found 2 : " + x))
                .observeOn(schedulerB)
                .doOnNext(x -> log.info("Found 3 : " + x))
                .subscribeOn(schedulerC)
                .subscribe(
                        x -> log.info("Got 1: " + x),
                        Throwable::printStackTrace,
                        () -> log.info("Completed")
                );
        log.info("Exiting");
    }

    void delay() {
        Observable.just("A", "B")
                .delay(500, TimeUnit.MILLISECONDS, schedulerA)
                .subscribe(log::info);
    }

    private Observable<String> simple() {
        return Observable.create(subscriber -> {
            log.info("subscribed");
            subscriber.onNext("A");
            subscriber.onNext("B");
            subscriber.onCompleted();
        });
    }

    private void sleepOneSecond() {
        SleepUtil.sleep(1000);
    }

    private ThreadFactory threadFactory(String name) {
        return r -> {
            Thread thread = new Thread(r);
            thread.setName(name + "-" + thread.getId());
            return thread;
        };
    }

}
