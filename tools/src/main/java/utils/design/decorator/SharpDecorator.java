package utils.design.decorator;


/**
 * @author Jimmy
 * @date 2020/5/9 0009
 */
public abstract class SharpDecorator implements Sharp {
	protected Sharp decoratedSharp;

	public SharpDecorator(Sharp decoratedSharp) {
		this.decoratedSharp = decoratedSharp;
	}

	@Override
	public void draw() {
		decoratedSharp.draw();
	}
}
