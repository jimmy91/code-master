package app.netty;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Jimmy
 */
@Data
public class MessageDto implements Serializable {

    public MessageDto() {
    }

    public MessageDto(String nettyId, String type, String status, String msg, Map<String, Object> objectMap, String typeId, String date) {
        this.nettyId = nettyId;
        this.type = type;
        this.status = status;
        this.msg = msg;
        this.objectMap = objectMap;
        this.typeId = typeId;
        this.date = date;
    }

    private String nettyId;

    private String type;

    private String status;

    private String msg;

    private String typeId;

    private String date;

    private Map<String, Object> objectMap;


}