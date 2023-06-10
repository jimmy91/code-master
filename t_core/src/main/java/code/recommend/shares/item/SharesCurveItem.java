package code.recommend.shares.item;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jimmy
 */
@Data
public class SharesCurveItem implements Serializable {

    private String code;

    private String name;

    private Date time;

    private String strtime;

    /**
     * 价格
     */
    private Double price;

    /**
     * 均价
     */
    private Double avgPrice;

    /**
     * 涨幅
     */
    private Double ratio;

    /**
     * 成交量
     */
    private Double volume;


}
