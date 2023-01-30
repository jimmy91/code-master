package app.project.service;

import app.netty.MessageDto;

/**
 * @author Jimmy
 */
public interface ImPushService {

    /**
     * 推送给指定
     * @param type
     * @param msg
     */
    void sendMsgToOne(String type, MessageDto msg);

    /**
     * 推送给所有
     * @param msg
     */
    void sendMsgToAll(MessageDto msg);
}


