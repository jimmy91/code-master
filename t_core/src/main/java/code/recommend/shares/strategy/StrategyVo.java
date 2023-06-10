package code.recommend.shares.strategy;


import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jimmy
 */
@Data
public class StrategyVo {

    private Boolean match = Boolean.FALSE;

    private StrategyEnum strategyModel;

    private String tag;

    private List<String> keywords = new ArrayList<>();

    public String toData(){
        return String.format("【%s : %s】", tag, keywords);
    }

    public static void main(String[] args) {
        StrategyVo vo = new StrategyVo();
        vo.setTag("kwgkwg");
        vo.setKeywords(Arrays.asList("001", "0002"));
        System.out.println(vo.toData());
    }


}
