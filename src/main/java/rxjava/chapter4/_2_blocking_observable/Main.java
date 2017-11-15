package rxjava.chapter4._2_blocking_observable;

import rx.Observable;
import rx.observables.BlockingObservable;
import rxjava.chapter4._1_collection_to_observable.Person;
import rxjava.chapter4._1_collection_to_observable.PersonRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PersonRepository personRepository = new PersonRepository();
        List<Person> people = personRepository.listPeople();
        String json = marshal(people);

        //////////////////////////////////////////////////////

        Observable<Person> personObservable = personRepository.observablePeople();

        // toList() 메서드는 onComplete() 이벤트를 받을 때 까지 모든 Person이벤트를 메모리에 버퍼 처리한다.
        // 완료 통지를 받으면 그 시점에 모든 이벤트를 포함하는 List<Person> 단일 이벤트로 방출
        Observable<List<Person>> peopleList = personObservable.toList();

        // BlockingObservable은 다른 비동기 Observable의 정적 블로킹 뷰(?)를 제공해야 할 때 적절한 방식
        BlockingObservable<List<Person>> peopleBlocking = peopleList.toBlocking();

        // single() 연산자는 observable을 걷어내고 항목 하나만을 뽑아낸다.
        List<Person> single = peopleBlocking.single();

        ///////////////////////////////////////////////////

        List<Person> people2 = personRepository.observablePeople()
                .toList()
                .toBlocking()
                .single();
    }

    private static String marshal(List<Person> people) {
        return "";
    }
}
