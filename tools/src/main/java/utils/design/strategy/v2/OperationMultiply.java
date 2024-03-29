package utils.design.strategy.v2;

import org.springframework.stereotype.Service;

/**
 * 策略三
 * @author Jimmy
 * @date 2021/3/23 0023
 */
@Service
public class OperationMultiply implements Strategy {
	@Override
	public int doOperation(int num1, int num2) {
		return num1 * num2;
	}

	@Override
	public String getStrategyType() {
		return Context.STRATEGY_TYPE_MULTIPLY;
	}
}
