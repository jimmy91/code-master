package code.recommend.shares.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jimmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharesItem implements Serializable {

    private String code;

    private String name;

    private String totalPrice;

    private String price;

    /**
     * 涨跌幅
     */
    private Double limit;

    /**
     * 换手率
     */
    private Double changeRate;

    public String toData() {
        return String.format("%s        %-6s    %-8s    %-4s    %-4s    %-4s\n", code, name, totalPrice, price, limit+"%", changeRate+"%");
    }
}
