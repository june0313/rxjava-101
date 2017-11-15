package rxjava.chapter4._5_concurrency.rx;

import rx.Observable;
import rx.schedulers.Schedulers;
import rxjava.chapter4._5_concurrency.SleepUtil;
import rxjava.chapter4._5_concurrency.blocking.Flight;
import rxjava.chapter4._5_concurrency.blocking.Passenger;
import rxjava.chapter4._5_concurrency.blocking.Ticket;

public class RxMain {
    public static void main(String[] args) {
        RxMain rxMain = new RxMain();
        rxMain.clientCodeNonBlocking();
//        rxMain.clientCodeBlocking();
        SleepUtil.sleep(8000);
    }

    public void clientCodeNonBlocking() {
        System.out.println("client code start");

        Observable<Flight> flight = rxLookupFlight("LOT 783").subscribeOn(Schedulers.io());
        Observable<Passenger> passenger = rxFindPassenger(42).subscribeOn(Schedulers.io());

        Observable<Ticket> ticket = flight
                .zipWith(passenger, (f, p) -> rxBookTicket(f, p))
                .flatMap(obs -> obs);
        ticket.subscribe(this::sendEmail);

        System.out.println("client code end");
    }

    public void clientCodeBlocking() {
        System.out.println("client code start");

        Observable<Flight> flight = rxLookupFlight("LOT 783");
        Observable<Passenger> passenger = rxFindPassenger(42);

        Observable<Ticket> ticket = flight
                .zipWith(passenger, (f, p) -> bookTicket(f, p));
        ticket.subscribe(this::sendEmail);

        System.out.println("client code end");
    }

    Observable<Flight> rxLookupFlight(String flightNo) {
        return Observable.defer(() -> Observable.just(lookupFlight(flightNo)));
    }

    Observable<Passenger> rxFindPassenger(int id) {
        return Observable.defer(() -> Observable.just(findPassenger(id)));
    }

    Observable<Ticket> rxBookTicket(Flight flight, Passenger passenger) {
        return Observable.defer(() -> Observable.just(bookTicket(flight, passenger)));
    }

    private void sendEmail(Ticket ticket) {
        System.out.println("sendEmail() : " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
    }

    private Ticket bookTicket(Flight filght, Passenger passenger) {
        SleepUtil.sleep(1000);
        System.out.println("bookTicket() : " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
        return new Ticket();
    }

    private static Passenger findPassenger(int id) {
        SleepUtil.sleep(3000);
        System.out.println("findPassenger() : " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
        return new Passenger();
    }

    private static Flight lookupFlight(String flightNo) {
        SleepUtil.sleep(3000);
        System.out.println("lookupFlight() : " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
        return new Flight();
    }
}
