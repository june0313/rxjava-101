package rxjava.chapter4._5_concurrency.blocking;

import rxjava.chapter4._5_concurrency.SleepUtil;

public class BlockingMain {
    public static void main(String[] args) {
        Flight flight = lookupFlight("LOT 783");
        Passenger passenger = findPassenger(42);
        Ticket ticket = bookTicket(flight, passenger);
        sendEmail(ticket);
    }

    private static void sendEmail(Ticket ticket) {
        System.out.println("send : " + ticket);
    }

    private static Ticket bookTicket(Flight filght, Passenger passenger) {
        SleepUtil.sleep(2000);
        return new Ticket();
    }

    private static Passenger findPassenger(int id) {
        SleepUtil.sleep(1000);
        return new Passenger();
    }

    private static Flight lookupFlight(String flightNo) {
        SleepUtil.sleep(3000);
        return new Flight();
    }
}
