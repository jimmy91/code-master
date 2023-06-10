package code.recommend.shares.dto;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 关系数据
 * @author jimmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharesRelateDTO implements Serializable {

    /**
     * 相关代码
     */
    private String code;

    /**
     * 相关名称
     */
    private String name;

    /**
     * 相关度
     */
    private Double relate;

    /**
     * 价格
     */
    private String price;

    /**
     * 统计
     */
    private String xRatios;
    private String yRatios;

    public String toData() {
        if(StrUtil.isEmpty(xRatios)){
            return String.format("%s        %s        %-6s        %s\n", code, name, relate, "--");
        }else{
            return String.format("%s        %s        %s        %s\n" +
                    "                %-30s              %s\n", code, name, relate, xRatios, price, yRatios);
        }

    }


}
