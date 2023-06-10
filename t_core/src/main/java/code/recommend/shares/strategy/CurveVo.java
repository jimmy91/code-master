package code.recommend.shares.strategy;

import lombok.Data;

import java.util.List;

/**
 * @author PLXQ
 */
@Data
public class CurveVo {

    private List<Double> records;

    private String tag;

    private List<String> keywords;

}
