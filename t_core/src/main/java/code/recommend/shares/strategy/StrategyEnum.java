package code.recommend.shares.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import code.recommend.shares.SharesConstants;
import code.recommend.shares.SharesUtil;
import code.recommend.shares.item.SharesCurveItem;
import code.recommend.shares.item.SharesDayData;
import code.recommend.shares.item.SharesItem;
import com.google.common.math.DoubleMath;
import jodd.util.MathUtil;
import lombok.Getter;
import lombok.Setter;
import utils.tools.coll.CollectionUtils;

import java.util.*;

/**
 * 常见的热门股票，有哪些走势操作：拉高出货，低位拉涨，震荡上升，尾盘抢筹，低位修复，连续补跌，持续上涨。
 * @author jimmy
 */
@Getter
public enum StrategyEnum {


    LGCH("拉高出货", "快速涨高快速回落", -1){
        @Override
        List<StrategyVo> check(SharesDayData dayData, List<SharesCurveItem> curves, SharesItem item) {

            List<StrategyVo> vos = new ArrayList<>();
            List<Double> curs = CollectionUtils.convertList(curves, SharesCurveItem::getRatio);
            List<CurveVo> fastUp = SharesUtil.findFastUp(curs, 30, 3);
            List<CurveVo> fastDown = SharesUtil.findFastDown(curs, 30, 3);
            if(CollUtil.isNotEmpty(fastUp)){
                StrategyVo vo = new StrategyVo();
                vo.setMatch(Boolean.TRUE);
                fastUp.stream().forEach(p -> { vo.setTag(p.getTag());vo.getKeywords().addAll(p.getKeywords());});
                vos.add(vo);
            }if(CollUtil.isNotEmpty(fastDown)){
                StrategyVo vo = new StrategyVo();
                vo.setMatch(Boolean.TRUE);
                fastDown.stream().forEach(p ->{ vo.setTag(p.getTag());vo.getKeywords().addAll(p.getKeywords());});
                vos.add(vo);
            }
            return vos;

        }
    },


    ;


    private String name;

    private String strategy;

    /**
     * 评分
     */
    private Integer score;


    StrategyEnum(String name, String strategy, Integer score){
        this.name = name;
        this.name = strategy;
        this.score = score;
    }

    abstract List<StrategyVo> check(SharesDayData dayData, List<SharesCurveItem> curves, SharesItem item);


   static StrategyVo fastCheck(SharesDayData dayData, List<SharesCurveItem> curves, SharesItem item) {

       if (!check(dayData)) return null;
       // 成交量放大

        StrategyVo vo = new StrategyVo();
        return vo;
    }

   public static boolean check(SharesDayData dayData) {
        // 涨幅在2%-5%
        if(dayData.getPriceLimitNum() < 2.0D && dayData.getPriceLimitNum() > 5.0D){
            return false;
        }
        // 量比大于1
        if(dayData.getVolumeRatio() < 1.0D){
            return false;
        }
        // 换手4%-10%
        if(dayData.getTurnoverRatioNum() < 4.0D || dayData.getTurnoverRatioNum() > 10.0D){
            return false;
        }
        // 市值50-200亿
        if(dayData.getCapitalizationNum() > 200 || dayData.getCapitalizationNum() < 50){
            return false;
        }
        return true;
    }

    public static List<StrategyVo> doOperation(SharesDayData dayData, List<SharesCurveItem> curves, SharesItem item) {
        List<StrategyVo> enums = new ArrayList<>();
        if(Objects.nonNull(fastCheck(dayData, curves, item))){
            for(StrategyEnum e : values()){
                List<StrategyVo> vos = e.check(dayData, curves, item);
                if(Objects.nonNull(vos) && CollUtil.isNotEmpty(vos)){
                    enums.addAll(vos);
                }
            }
            return enums;
        }

        return null;

    }


}
