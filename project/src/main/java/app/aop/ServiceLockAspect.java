package app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步锁 AOP
 * @author Jimmy
 */
@Component
@Scope
@Aspect
public class ServiceLockAspect {
	/**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
	 * 互斥锁 参数默认false，不公平锁
     */
	private static  Lock lock = new ReentrantLock(true);

	/**
	 * Service层切点m用于记录错误日志
	 */
	@Pointcut("@annotation(app.annotation.ServiceLock)")
	public void lockAspect() {
		
	}
	
    @Around("lockAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
		Object obj = null;
    	lock.lock();
		try {
			obj = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally{
			lock.unlock();
		}
    	return obj;
    } 
}
