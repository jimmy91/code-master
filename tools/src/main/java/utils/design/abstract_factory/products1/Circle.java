package utils.design.abstract_factory.products1;

/** 产品三
 * @author Jimmy
 * @date 2021/3/23 0023
 */
public class Circle implements Shape {
	@Override
	public void draw() {
		System.out.println("Inside Circle::draw() method.");
	}
}
