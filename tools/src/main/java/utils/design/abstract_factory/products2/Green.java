package utils.design.abstract_factory.products2;

/**
 *  产品二
 * @author Jimmy
 * @date 2021/3/24 0024
 */
public class Green implements Color {
	@Override
	public void fill() {
		System.out.println("Inside Green::fill() method.");
	}
}
