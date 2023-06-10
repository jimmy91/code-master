package code.recommend.shares.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jimmy
 * [{"ename":"open","name":"今开","value":"7.36","status":"up"},{"ename":"high","name":"最高","value":"7.46","status":"up"},
 * {"ename":"volume","name":"成交量","value":"3.27万手"},{"ename":"preClose","name":"昨收","value":"7.35"},
 * {"ename":"low","name":"最低","value":"7.32","status":"down"},{"ename":"amount","name":"成交额","value":"2417万"},
 * {"ename":"turnoverRatio","name":"换手率","value":"1.02%"},{"ename":"peratio","name":"市盈(TTM)","helpIcon":"1","value":"22.39"},
 * {"ename":"capitalization","name":"总市值","value":"30.33亿"},{"ename":"volumeRatio","name":"量比","helpIcon":"1","value":"0.64","status":"down"},
 * {"ename":"lyr","name":"市盈(静)","value":"26.69"},{"ename":"totalShareCapital","name":"总股本","value":"4.12亿"},
 * {"ename":"weibiRatio","name":"委比","value":"-60.58%"},{"ename":"avgPrice","name":"均价","value":"7.39","status":"up"},
 * {"ename":"currencyValue","name":"流通值","value":"23.73亿"},{"ename":"limitUp","name":"涨停","value":"8.09","status":"up"},
 * {"ename":"priceLimit","name":"涨跌幅","value":"+0.27%","status":"up"},{"ename":"circulatingCapital","name":"流通股","value":"3.22亿"},
 * {"ename":"limitDown","name":"跌停","value":"6.62","status":"down"},{"ename":"amplitudeRatio","name":"振幅","value":"1.90%"},
 * {"ename":"inside","name":"内盘","helpIcon":"1","value":"1.45万","status":"down"},{"ename":"priceSaleRatio","name":"市销率","helpIcon":"1","value":"--"},
 * {"ename":"bvRatio","name":"市净率","value":"1.59"},{"ename":"outside","name":"外盘","value":"1.82万","status":"up"},
 * {"ename":"w52_high","name":"52周高","value":"7.69"},{"ename":"w52_low","name":"52周低","value":"5.23"}]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharesDayData implements Serializable {

    private String code;

    /**
     * 开盘价
     */
    private Double open;

    private Double preClose;

    /**
     * 最高价
     */
    private Double high;
    private Double low;

    /**
     * 成交量
     */
    private String volume;
    private Integer volumeNum;

    /**
     * 成交额
     */
    private String amount;
    private Integer amountNum;

    /**
     * 换手率
     */
    private String turnoverRatio;
    private Double turnoverRatioNum;

    /**
     * 量比
     */
    private Double volumeRatio;

    /**
     * 总市值
     */
    private String capitalization;
    private Double capitalizationNum;

    /**
     * 涨跌幅
     */
    private String priceLimit;
    private Double priceLimitNum;

    public Double getPriceLimitNum() {
        return Double.parseDouble(this.priceLimit.replace("%",  ""));
    }

    public Double getCapitalizationNum() {
        Double tPrice = capitalization.endsWith("万亿") ? 10000 * Double.parseDouble(capitalization.replace("万亿", ""))
                : Double.parseDouble(capitalization.replace("亿", ""));
        return tPrice;
    }

    public Double getTurnoverRatioNum() {
        return Double.parseDouble(this.turnoverRatio.replace("%",  ""));
    }
}
