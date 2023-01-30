package app.netty;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jimmy
 * @Description: 配置websocket后台客户端
 */
@Slf4j
@Component
public class WebSocketListenerClient {

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:"+NettyConfig.port + NettyConfig.webSocketPath), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[websocket] 连接成功");
                    Map<String, Object> map = new HashMap<>();
                    map.put("typeId", "netty_msg_first");
                    String jsonMsg = JSONObject.toJSONString(map);
                    this.send(jsonMsg);
                    log.info("[websocket] 回发连接成功消息!");
                }

                @Override
                public void onMessage(String message) {
                    log.info("[websocket] 收到消息={}", message);
                    if (ObjectUtils.isEmpty(message)) {
                        return;
                    }

                    Map<String, Object> map = JSONObject.parseObject(message, Map.class);
                    String typeId = String.valueOf(map.get("typeId"));
                    switch (typeId) {
                        case "netty_client_20220204":
                            log.info("[websocket] 收到类型ID为={},消息={}", typeId, message);
                            break;
                    }

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[websocket] 连接错误={}", ex.getMessage());
                }
            };
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
