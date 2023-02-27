package utils.exception;


import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.http.ResponseEntity;

/**
 * @author Jimmy
 */
public class ExceptionUtil {

    public static ResponseEntity<String>  fallback(Integer id, Throwable e){
        return ResponseEntity.ok("===被异常降级啦===");
    }

    public static ResponseEntity<String> handleException(Integer id, BlockException e){
        return  ResponseEntity.ok("===被限流啦===");
    }

}