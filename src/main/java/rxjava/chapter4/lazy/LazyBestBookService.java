package rxjava.chapter4.lazy;

import rx.Observable;
import rxjava.chapter4.collection_to_observable.Person;

public class LazyBestBookService {
    public void bestBookFor(Person person) {
        recommend(person)
                .onErrorResumeNext(bestSeller())
                .map(Book::getTitle)
                .subscribe(this::display);
    }

    private void display(String title) {
        System.out.println(title);
    }

    private Observable<Book> bestSeller() {
        return Observable.defer(Observable::empty);
    }

    private Observable<Book> recommend(Person person) {
        return Observable.defer(Observable::empty);
    }

}
