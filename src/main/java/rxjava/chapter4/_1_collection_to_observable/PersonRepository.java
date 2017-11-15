package rxjava.chapter4._1_collection_to_observable;

import rx.Observable;

import java.util.ArrayList;
import java.util.Iterator;
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

    public Observable<Person> byCreate() {
        final List<Person> people = query("SELECT * FROM PEOPLE");

        return Observable.create(subscriber -> {
            Iterator<Person> iterator = people.iterator();

            while (iterator.hasNext()) {
                subscriber.onNext(iterator.next());
            }
            subscriber.onCompleted();
        });
    }

    // lazy한 Observable 생성하기
    public Observable<Person> lazyPeople() {
        return Observable.defer(() -> Observable.from(query("SELECT * FROM PEOPLE")));
    }

    private List<Person> query(String sql) {
        return new ArrayList<>();
    }
}
