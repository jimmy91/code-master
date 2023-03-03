package utils.design.bridge;

/**
 * 扩展抽象类 正方形
 * @author Jimmy
 * @date 2022/10/28 0028
 * @since
 */
public class Square extends Shape {
	public Square(Color color) {
		super(color);
	}

	@Override
	public void draw() {
		color.bepaint("正方形");
	}
}
