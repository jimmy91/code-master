package app.annotation;
import java.lang.annotation.*;

/**
 * 自定义注解 同步锁
 * @author Jimmy
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface ServiceLock {
	 String description()  default "";
}
