
package app.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * TextWebSocketFrame类型， 表示一个文本帧
 *
 * @author
 * @date 2020-01-13-20:15
 */

@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
    //标识
    private static final String CHANNEL_TYPE = "netty_msg";


    /**
     * 连接，第一个被执行
     *
     * @param ctx
     * @throws Exception
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded 被调用:{}", ctx.channel().id().asLongText());
        // 添加到channelGroup 通道组
        NettyConfig.getChannelGroup().add(ctx.channel());
    }


    /**
     * 接收消息
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgObj) throws Exception {
        // 1. 获取客户端发送的消息
        try {
            TextWebSocketFrame msg = (TextWebSocketFrame) msgObj;
            if (!ObjectUtils.isEmpty(msg.text())) {
                Map<String, Object> map = new HashMap<>();
                map = com.alibaba.fastjson.JSONObject.parseObject(msg.text(), Map.class);
                String typeId = String.valueOf(map.get("typeId"));
                switch (typeId) {
                    //netty_msg_first类型ID:首次建立连接管道信息
                    case "netty_msg_first":
                        log.info("(类型ID:netty_msg_first)服务器收到消息连接成功：{}", msg.text());
                        // 获取CHANNEL_TYPE,关联channel(自定义发送类型)
                        NettyConfig.getUserChannelMap().put(CHANNEL_TYPE, ctx.channel());
                        // 将用type作为自定义属性加入到channel中，方便随时channel中获取type
                        AttributeKey<String> key = AttributeKey.valueOf("type");
                        ctx.channel().attr(key).setIfAbsent(CHANNEL_TYPE);
                        break;

                    //netty_web_20220204类型ID:接收前端发送的消息类型
                    case "netty_web_20220204":
                        log.info("(类型ID:netty_web_20220204)服务器收到前端发送消息：{}", msg.text());

                        break;

                    //netty_web_20220204类型ID:接收客户端发送的消息类型
                    case "netty_client_20220204":
                        log.info("(类型ID:netty_client_20220204)服务器收到前端发送消息：{}", msg.text());

                        break;
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        try {
            System.out.println("接收到的信息：" + textWebSocketFrame.toString());
        } finally {
            ReferenceCountUtil.release(textWebSocketFrame);
        }
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved 被调用:{}", ctx.channel().id().asLongText());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("异常：{}", cause.getMessage());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();
    }


    /**
     * 删除用户与channel的对应关系
     *
     * @param ctx
     */

    private void removeUserId(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("type");
        String userId = ctx.channel().attr(key).get();
        NettyConfig.getUserChannelMap().remove(userId);
    }
}


