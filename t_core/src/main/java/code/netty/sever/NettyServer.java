package code.netty.sever;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * 地址： https://blog.csdn.net/weixin_43333483/article/details/127468726
 *
 * NettyServer 心跳检测服务端
 * <p>
 * Netty心跳检测与断线重连
 * 需求：
 * 1、客户端利用空闲状态给服务端发送心跳ping命令，保持长连接不被关闭；
 * 2、服务端如果超过指定的时间没有收到客户端心跳，则关闭连接；
 * 3、服务端关闭连接触发客户端的channelInactive方法，在此方法中进行重连；
 * @author Jimmy
 */
public class NettyServer {

    public static final int port = 8787;

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.run();
    }

    public void start(){
        NettyServer nettyServer = new NettyServer();
        nettyServer.run();
    }

    private void run() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap().group(boss, work);
            bootstrap.channel(NioServerSocketChannel.class)
                    //这个处理器可以不写
                    .handler(new ChannelInitializer<ServerSocketChannel>() {
                        @Override
                        protected void initChannel(ServerSocketChannel ch) throws Exception {
                            System.out.println("服务正在启动中......");
                        }
                    })
                    //业务处理
                    .childHandler(NettyServerChannelInitializer.INSTANCE);

            ChannelFuture future = bootstrap.bind(port).sync();

            future.addListener(f -> {
                if (future.isSuccess()) {
                    System.out.println("服务启动成功");
                } else {
                    System.out.println("服务启动失败");
                }
            });
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}


