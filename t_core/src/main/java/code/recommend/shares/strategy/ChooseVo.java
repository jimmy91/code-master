package code.recommend.shares.strategy;

import code.recommend.shares.item.SharesItem;
import lombok.Data;

import java.util.Arrays;

@Data
public class ChooseVo {

    private SharesItem sharesItem;

    private StrategyVo upVos = new StrategyVo();

    private StrategyVo downVos = new StrategyVo();


    public String toData() {
        StringBuffer vos = new StringBuffer();
        vos.append(upVos.toData());
        vos.append(downVos.toData());
        return String.format("%-8s      %s      %s      %s      " +
                        "%s\n",
                sharesItem.getCode(), sharesItem.getName(), sharesItem.getLimit()+"%", sharesItem.getPrice()+"￥",
                vos.toString()
                );
    }

    public static void main(String[] args) {
        ChooseVo chooseVo = new ChooseVo();

        StrategyVo vo = new StrategyVo();
        vo.setTag("kwgkwg");
        vo.setKeywords(Arrays.asList("001", "0002"));

        chooseVo.setSharesItem(new SharesItem());
        System.out.println(chooseVo.toData());
    }
}
