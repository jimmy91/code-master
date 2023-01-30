package code.netty.client;


import cn.hutool.core.date.DateUtil;
import code.netty.packet.PacketInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.TimeUnit;

public class  NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接激活 == " + ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断线了......" + ctx.channel().id());
        ctx.channel().eventLoop().schedule(() -> {
            System.out.println("断线重连......");
            //重连
            NettyClient.connect();
        }, 3L, TimeUnit.SECONDS);
    }

    /**
     * 用户事件的回调方法
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果是空闲状态事件
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
                System.out.println("空闲"  + ctx.channel().id());
                //发送ping 保持心跳链接
                PacketInfo p = new PacketInfo("ping", "心跳监测", DateUtil.formatDateTime(DateUtil.date()));
                ctx.writeAndFlush(p);
            }
        }else {
            userEventTriggered(ctx,evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
