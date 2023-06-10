package code.recommend.shares.strategy;

import code.recommend.shares.item.SharesCurveItem;
import code.recommend.shares.item.SharesItem;

import java.util.List;

/**
 * 策略接口
 * @author Jimmy
 * @date 2021/3/23 0023
 */
public interface Strategy {

	/** 1
	 * 公共的策略接口，不同的策略实现
	 */
	List<StrategyEnum> doOperation(SharesItem item, List<SharesCurveItem> curves);
}
