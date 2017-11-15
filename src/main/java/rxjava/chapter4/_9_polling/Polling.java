package rxjava.chapter4._9_polling;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Polling {
    void polling() {
        Observable.interval(10, TimeUnit.MILLISECONDS)
                .map(x -> getOrderBookLength())
                .distinctUntilChanged();
    }

    Observable<Item> observeNewItems() {
        return Observable
                .interval(1, TimeUnit.MILLISECONDS)
                .flatMapIterable(x -> query())
                .distinct();
    }

    private List<Item> query() {
        // 파일 시스템의 디렉토리나 DB테이블의 스냅샷
        return new ArrayList<>();
    }

    private int getOrderBookLength() {
        return 0;
    }
}
