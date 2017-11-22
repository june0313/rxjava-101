package rxjava.chapter5.rxnetty;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 독립 실행형 TCP/IP 서버
 */
@Slf4j
public class EurUsdCurrencyTcpServer {
    private static final BigDecimal RATE = new BigDecimal("1.06448");

    public static void main(String[] args) {
        // 8080 포트로 통신하는 새로운 TCP/IP를 작성
        TcpServer.newServer(8080)
                .<String, String>pipelineConfigurator(pipeline -> {
                    // ByteBuf 순열을 여러 줄로 이루어진 목록으로 바꿈
                    pipeline.addLast(new LineBasedFrameDecoder(1024));
                    // String 객체로 변환
                    pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
                })
                .start(connection -> {
                    Observable<String> output = connection.getInput().map(BigDecimal::new)
                            .flatMap(EurUsdCurrencyTcpServer::eurToUsd);
                    return connection.writeAndFlushOnEach(output);
                })
                .awaitShutdown();
    }

    private static Observable<String> eurToUsd(BigDecimal eur) {
        return Observable
                .just(eur.multiply(RATE))
                .map(amount -> eur + " EUR is " + amount + " USD\n")
                .doOnNext(event -> log.info(event))
                .delay(3000, TimeUnit.MILLISECONDS);
    }
}
