package rxjava.chapter5.rxnetty;

import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

public class RxNettyHttpServer {
    private static final Observable<String> RESPONSE_OK = Observable.just("OK2");

    public static void main(String[] args) {
        HttpServer
                .newServer(8086)
                .start((req, res) -> res
                        .setHeader("Content-length", 3).writeStringAndFlushOnEach(RESPONSE_OK)
                )
                .awaitShutdown();
    }
}
