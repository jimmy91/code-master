package utils.design.responsibility;

/**
 *  责任链其一
 * @author Jimmy
 * @date 2021/3/22 0022
 */
public class ErrorLogger extends AbstractLogger {

	public ErrorLogger(int level) {
		this.level = level;
	}

	@Override
	void write(String message) {
		System.out.println("Error Logger：" + message);
	}
}
