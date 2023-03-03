package utils.design.abstract_factory;

import utils.design.abstract_factory.products1.Shape;
import utils.design.abstract_factory.products2.Color;

/**
 * 生产多个产品的工厂1
 * @author lwq
 * @date 2021/3/24 0024
 */
public class Factory1 extends AbstractFactory {

	/** 3
	 * 生产产品1-形状
	 */
	@Override
	public Shape createShape(String shapeType) {
		System.out.println("我是工厂1-生产形状");
		Shape shape = shapeFactory.getShape(shapeType);
		return shape;
	}

	/** 3
	 * 生产产品2-颜色
	 */
	@Override
	public Color createColor(String colorType) {
		System.out.println("我是工厂1-生产颜色");
		Color color = colorFactory.getColor(colorType);
		return color;
	}
}
