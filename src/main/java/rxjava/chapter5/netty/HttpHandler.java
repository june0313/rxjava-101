package rxjava.chapter5.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * 요청을 처리하는 비즈니스 로직 컴포넌트
 */
@Slf4j
@Sharable
public class HttpHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            sendRequest(ctx);
        }
    }

    private void sendRequest(ChannelHandlerContext ctx) {
        // 응답객체 생성
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("OK".getBytes(StandardCharsets.UTF_8)));
        response.headers().add("Content-length", 2);

        // ChannelFuture를 반환한다.
        ctx.writeAndFlush(response);
                // 비동기적으로 채널을 닫을 수 있다.
//                .addListener(ChannelFutureListener.CLOSE);
        log.info("OK");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Error", cause);
        ctx.close();
    }
}
