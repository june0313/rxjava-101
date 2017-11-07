package rxjava.chapter4.collection_to_observable;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class PersonRepository {
    public List<Person> listPeople() {
        return query("SELECT * FROM PEOPLE");
    }

    // collection 을 Observable로!
    public Observable<Person> observablePeople() {
        final List<Person> people = query("SELECT * FROM PEOPLE");
        return Observable.from(people);
    }

    // lazy한 Observable 생성하기
    public Observable<Person> lazyPeople() {
        return Observable.defer(() -> Observable.from(query("SELECT * FROM PEOPLE")));
    }

    private List<Person> query(String sql) {
        return new ArrayList<>();
    }
}
