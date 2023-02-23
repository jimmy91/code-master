package utils.generator.common.exception;

import lombok.Data;

/**
 * 错误码对象
 * <p>
 * @author Jimmy
 */
@Data
public class ErrorCode {

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }
}
