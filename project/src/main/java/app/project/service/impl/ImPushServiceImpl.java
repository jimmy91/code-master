
package app.project.service.impl;


import app.netty.MessageDto;
import app.netty.NettyConfig;
import app.project.service.ImPushService;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Jimmy
 */
@Service
@Slf4j
public class ImPushServiceImpl implements ImPushService {

    @Override
    public void sendMsgToOne(String type, MessageDto msg) {
        ConcurrentHashMap<String, Channel> userChannelMap = NettyConfig.getUserChannelMap();
        Channel channel = userChannelMap.get(type);
        String jsonMsg = JSONObject.toJSONString(msg);
        channel.writeAndFlush(new TextWebSocketFrame(jsonMsg));
        log.info("pushMsgToOne消息发送成功={}", jsonMsg);
    }

    @Override
    public void sendMsgToAll(MessageDto msg) {
        String jsonMsg = JSONObject.toJSONString(msg);
        NettyConfig.getChannelGroup().writeAndFlush(new TextWebSocketFrame(jsonMsg));
        log.info("pushMsgToAll消息发送成功={}", msg);
    }
}
