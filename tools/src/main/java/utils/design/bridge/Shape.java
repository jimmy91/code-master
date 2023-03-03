package utils.design.bridge;

/**
 * 抽象类 形状
 * @author Jimmy
 * @date 2022/10/28 0028
 * @since
 */
public abstract class Shape {
	// 持有颜色的引用
	protected Color color;

	public Shape(Color color) {
		this.color = color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	// 具体形状中需要调用引用颜色的相关方法
	public abstract void draw();
}
