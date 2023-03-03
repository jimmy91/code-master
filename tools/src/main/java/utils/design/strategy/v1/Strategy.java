package utils.design.strategy.v1;

/**
 * 策略接口
 * @author Jimmy
 * @date 2021/3/23 0023
 */
public interface Strategy {

	/** 1
	 * 公共的策略接口，不同的策略实现
	 */
	public int doOperation(int num1, int num2);
}
