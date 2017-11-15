package rxjava.chapter4._8_multi_thread;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.schedulers.Schedulers;
import rxjava.chapter4._5_concurrency.SleepUtil;

import java.math.BigDecimal;

@Slf4j
public class RxGroceries {
    public static void main(String[] args) {
        RxGroceries rxGroceries = new RxGroceries();
        rxGroceries.getTotalPrice3().subscribe();
        SleepUtil.sleep(10000);
    }


    Observable<BigDecimal> getTotalPrice() {
        return Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(Schedulers.io())
                .map(prod -> doPurchase(prod, 1))
                .reduce(BigDecimal::add)
                .single();
    }

    Observable<BigDecimal> getTotalPrice2() {
        return Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(Schedulers.io())
                .flatMap(prod -> purchase(prod, 1))
                .reduce(BigDecimal::add)
                .single();
    }

    Observable<BigDecimal> getTotalPrice3() {
        return Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .flatMap(prod -> purchase(prod, 1).subscribeOn(Schedulers.io()))
                .reduce(BigDecimal::add)
                .single();
    }

    Observable<BigDecimal> purchase(String productName, int quantity) {
        return Observable.fromCallable(() -> doPurchase(productName, quantity));
    }

    private BigDecimal doPurchase(String productName, int quantity) {
        log.info("Purchasing " + quantity + " " + productName);
        SleepUtil.sleep(1000);
        log.info("Done " + quantity + " " + productName);
        return new BigDecimal("1");
    }
}
