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

    private List<Person> query(String sql) {
        return new ArrayList<>();
    }
}
