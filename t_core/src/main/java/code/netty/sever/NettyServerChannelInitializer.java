package code.netty.sever;

import code.netty.packet.PacketInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 自定义的Channe（网络传输操作的接口）
 * ChannelPipeline提供了一个容器给ChannelHandler链并提供了一个API 用于管理沿着链入站和出站事件的流动，每个Channel都有自己的ChannelPipeline，ChannelHandler
 */
@ChannelHandler.Sharable
public class NettyServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    public static final NettyServerChannelInitializer INSTANCE = new NettyServerChannelInitializer();

//NioSocketChannel，异步的客户端TCP Socket连接

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                //空闲状态的处理器
                .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder((s) -> PacketInfo.class))

                //我们自己编写的websocket请求逻辑处理Handler
                .addLast(NettyServerHandler.INSTANCE)

                //WebSocket请求处理（是netty内置的handler，直接使用即可，websocket的请求路径是 ws://ip:port/im）
                //.addLast(new WebSocketServerProtocolHandler("/im")
        ;

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有新连接加入了++++......" + ctx.channel().id());
    }
}


