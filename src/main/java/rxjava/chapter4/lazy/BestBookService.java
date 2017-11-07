package rxjava.chapter4.lazy;

import rxjava.chapter4.collection_to_observable.Person;

public class BestBookService {
    public void bestBookFor(Person person) {
        Book book;

        try {
            book = recommend(person);
        } catch (Exception e) {
            book = bestSeller();
        }

        display(book.getTitle());
    }

    private void display(String title) {
        System.out.println(title);
    }

    private Book bestSeller() {
        return new Book();
    }

    private Book recommend(Person person) {
        return new Book();
    }
}
