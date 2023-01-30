package code.netty.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Jimmy
 * https://blog.csdn.net/zwjzone/article/details/125905607
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PacketInfo implements Serializable {

    private String msg;

    private String content;

    private String time;

}
