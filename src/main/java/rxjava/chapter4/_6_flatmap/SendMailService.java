package rxjava.chapter4._6_flatmap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import rx.Observable;
import rx.schedulers.Schedulers;
import rxjava.chapter4._5_concurrency.blocking.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SendMailService {
    private ExecutorService pool = Executors.newFixedThreadPool(1);

    public void sendEmail(List<Ticket> tickets) {
        List<Ticket> failures = new ArrayList<>();

        for (Ticket ticket : tickets) {
            try {
                sendEmail(ticket);
            } catch (Exception e) {
                log.warn("Failed to send", ticket, e);
                failures.add(ticket);
            }
        }

    }

    private SmtpResponse sendEmail(Ticket ticket) {
        System.out.println("send : " + ticket);
        return new SmtpResponse();
    }

    public void sendMailAsync(List<Ticket> tickets) {
        List<Pair<Ticket, Future<SmtpResponse>>> tasks = tickets.stream()
                .map(ticket -> Pair.of(ticket, sendEmailAsync(ticket)))
                .collect(Collectors.toList());

        List<Ticket> failures = tasks.stream()
                .flatMap(pair -> {
                    try {
                        Future<SmtpResponse> future = pair.getRight();
                        future.get(1, TimeUnit.SECONDS);
                        return Stream.empty();
                    } catch (Exception e) {
                        Ticket ticket = pair.getLeft();
                        log.warn("Failed to send", ticket, e);
                        return Stream.of(ticket);
                    }
                })
                .collect(Collectors.toList());
    }

    private Future<SmtpResponse> sendEmailAsync(Ticket ticket) {
        return pool.submit(() -> sendEmail(ticket));
    }

    public void rxSendMail(List<Ticket> tickets) {
        Observable.from(tickets)
                .flatMap(ticket -> rxSendEmail(ticket)
                        .flatMap(response -> Observable.empty())
                        .doOnError(e -> log.warn("Failed to send", ticket, e))
                        .onErrorReturn(err -> ticket))
                .toList()
                .toBlocking()
                .single();
    }

    public void rxSendMail2(List<Ticket> tickets) {
        Observable.from(tickets)
                .flatMap(ticket -> rxSendEmail(ticket)
                        .ignoreElements()
                        .doOnError(e -> log.warn("Failed to send", ticket, e))
//                        .onErrorReturn(err -> ticket) // 컴파일 에러가 발생한다. 원인은?
                        .subscribeOn(Schedulers.io()))
                .toList()
                .toBlocking()
                .single();
    }

    private Observable<SmtpResponse> rxSendEmail(Ticket ticket) {
        return Observable.fromCallable(() -> sendEmail(ticket));
    }
}