package code.netty.client;

import cn.hutool.core.date.DateUtil;
import code.netty.packet.PacketInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 心跳检测服务端 对应的服务端在netty-server heartbeat 包下的NettyClient
 * @author Jimmy
 */
public class NettyClient {

    private static Bootstrap bootstrap = new Bootstrap();

    public static void main(String[] args) throws InterruptedException {


        try {
            NioEventLoopGroup group = new NioEventLoopGroup(8);
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //空闲状态的handler
                                    .addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder((s) -> PacketInfo.class))
                                    .addLast(new NettyClientHandler())

                            ;
                        }
                    });

            //连接
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重连方法
     */
    public static void connect() {
        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8787).sync()
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture ch) throws Exception {
                            if (!ch.isSuccess()) {
                                ch.channel().close();
                                final EventLoop loop = ch.channel().eventLoop();
                                loop.schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.err.println("服务端链接不上，开始重连操作...");
                                        //重连
                                        connect();
                                    }
                                }, 3L, TimeUnit.SECONDS);
                            } else {
                                PacketInfo p = new PacketInfo("ping", "首次连接",  DateUtil.formatDateTime(DateUtil.date()));
                                ch.channel().writeAndFlush(p);
                                System.out.println("服务端链接成功...");
                            }
                        }
                    });
        } catch (Exception e) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("服务端链接异常，再次尝试连接：" + e.getMessage());
            //再重连
            connect();
        }
    }
}
