package utils.design.proxy;

/**
 * @author Jimmy
 * @date 2022/10/25 0025
 * @since
 */
public class TargetObjectImpl implements TargetObject {
	@Override
	public String method() {
		System.out.println("target method");
		return "done";
	}
}
