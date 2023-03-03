package utils.design.factory;

import utils.design.factory.factory_method.CircleFactory;
import utils.design.factory.factory_method.RectangleFactory;
import utils.design.factory.factory_method.SquareFactory;
import utils.design.factory.simple_factory.ShapeFactory;

/**
 * @author Jimmy
 * @date 2021/3/23 0023
 */
public class FactoryPatternDemo {

	public static void main(String[] args) {

		// simpleFactoryPattern();

		factoryMethonPattern();
	}

	/**
	 * 简单工厂
	 */
	public static void simpleFactoryPattern() {
		/** 3
		 * 工厂类实例化
		 */
		ShapeFactory shapeFactory = new ShapeFactory();

		/** 4
		 * 生产不同的产品，并使用
		 */
		Shape circle = shapeFactory.getShape("circle");
		circle.draw();

		Shape square = shapeFactory.getShape("square");
		square.draw();

		Shape rectangle = shapeFactory.getShape("rectangle");
		rectangle.draw();
	}

	/**
	 * 工厂模式（工厂方法模式）
	 */
	public static void factoryMethonPattern() {
		/** 4
		 * 具体工厂子类实例化
		 */
		RectangleFactory rectangleFactory = new RectangleFactory();
		SquareFactory squareFactory = new SquareFactory();
		CircleFactory circleFactory = new CircleFactory();

		/** 5
		 * 各个工厂子类生产各自的产品
		 */
		Shape circle = circleFactory.getShape();
		circle.draw();

		Shape square = squareFactory.getShape();
		square.draw();

		Shape rectangle = rectangleFactory.getShape();
		rectangle.draw();
	}
}
