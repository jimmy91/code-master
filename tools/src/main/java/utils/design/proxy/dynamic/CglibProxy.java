package utils.design.proxy.dynamic;

import utils.design.proxy.TargetObjectImpl;
import io.lettuce.core.dynamic.intercept.MethodInterceptor;
import io.lettuce.core.dynamic.intercept.MethodInvocation;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB动态代理
 * @author lwq
 * @date 2022/10/26 0026
 * @since
 */
public class CglibProxy implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();

	public Object getProxy(Class clazz) {
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		//enhancer.setCallback();
		// 通过字节码技术动态创建子类实例
		Object obj = enhancer.create();
		return obj;
	}

	// 实现MethodInterceptor接口方法
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		// 做一些事情
		System.out.println("cglib proxy method before");
		// 通过代理类调用父类中的方法
		// Object invoke = method.invoke(obj, new String[]{}); // 可以改变原方法参数
		Object invoke = methodProxy.invokeSuper(obj, args);
		// 做一些事情
		System.out.println("cglib proxy method after");
		// return invoke + "新增的返回内容"; // 可以改变原方法的返回值
		return invoke;
	}

	public static void main(String[] args) {
		CglibProxy cglibProxy = new CglibProxy();
		TargetObjectImpl proxy = (TargetObjectImpl) cglibProxy.getProxy(TargetObjectImpl.class);
		String method = proxy.method();
		System.out.println(method);
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return null;
	}
}
