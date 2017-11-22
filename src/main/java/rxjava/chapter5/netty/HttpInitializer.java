package rxjava.chapter5.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * ByteBuf가 도착하는대로 처리하는 파이프라인 구축
 */
public class HttpInitializer extends ChannelInitializer<SocketChannel> {

    private final HttpHandler httpHandler = new HttpHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                // 도착한 원시 바이트를 상위 단계인 HTTP 요청 객체(HttpRequest))로 디코딩한다.
                .addLast(new HttpServerCodec())
                // 실제 요청을 처리하는 비즈니스 로직 컴포넌트
                .addLast(httpHandler);
    }
}
