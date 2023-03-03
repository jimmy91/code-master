package utils.design.factory.factory_method;

import utils.design.factory.Shape;
import utils.design.factory.Square;

/**
 * 工厂接口实现二：生产Square
 * @author lwq
 * @date 2021/3/24 0024
 */
public class SquareFactory implements Factory{

	/** 3
	 * 具体工厂子类生产具体某一种产品
	 */
	@Override
	public Shape getShape() {
		return new Square();
	}
}
