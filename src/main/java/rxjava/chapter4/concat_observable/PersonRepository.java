package rxjava.chapter4.concat_observable;

import rx.Observable;
import rxjava.chapter4.collection_to_observable.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonRepository {
    private static final int PAGE_SIZE = 30;

    // concatWith() : 왼쪽에 있는 Observable이 완료되면, subscriber에 완료를 전파하지 않고,
    // 인자로 받은 Observable을 구독한다.
    // a.concatWith(b).subscribe(...) 에서 구독자는 a이벤트를 다 받으면 b이벤트를 받는다.
    Observable<Person> allPeople(int initialPage) {
        return Observable
                .defer(() -> Observable.from(listPeople(initialPage)))
                .concatWith(Observable.defer(() -> allPeople(initialPage + 1)));
    }

    // 느긋한 페이지 분할 및 이어붙히기
    void allPages() {
        Observable<List<Person>> allPages = Observable
                .range(0, Integer.MAX_VALUE)
                .map(this::listPeople)
                .takeWhile(list -> !list.isEmpty());

        Observable<Person> people = allPages.concatMap(Observable::from);
        Observable<Person> people2 = allPages.concatMapIterable(page -> page);
    }

    List<Person> listPeople(int page) {
        return query("SELECT * FROM PEOPLE ORDER BY id LIMIT ? OFFSET ?", PAGE_SIZE, page * PAGE_SIZE);
    }

    private List<Person> query(String sql, int pageSize, int page) {
        return new ArrayList<>();
    }
}
