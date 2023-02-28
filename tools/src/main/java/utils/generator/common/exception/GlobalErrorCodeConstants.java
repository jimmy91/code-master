package utils.generator.common.exception;

/**
 * 全局错误码枚举
 * <p>
 * 一般情况下，使用 HTTP 响应状态码 https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 * 虽然说，HTTP 响应状态码作为业务使用表达能力偏弱，但是使用在系统层面还是非常不错的
 * 比较特殊的是，因为之前一直使用 0 作为成功，就不使用 200 啦。
 * @author Jimmy
 */
public interface GlobalErrorCodeConstants {

    ErrorCode SUCCESS = new ErrorCode(0, "成功");

    // ========== 客户端错误段 ==========

    ErrorCode BAD_REQUEST = new ErrorCode(400, "请求参数不正确");
    ErrorCode UNAUTHORIZED = new ErrorCode(401, "账号未登录");
    ErrorCode FORBIDDEN = new ErrorCode(403, "没有该操作权限");
    ErrorCode NOT_FOUND = new ErrorCode(404, "请求未找到");
    ErrorCode METHOD_NOT_ALLOWED = new ErrorCode(405, "请求方法不正确");
    ErrorCode LOCKED = new ErrorCode(423, "请求失败，请稍后重试");
    ErrorCode TOO_MANY_REQUESTS = new ErrorCode(429, "请求过于频繁，请稍后重试");

    ErrorCode BAD_PARAM = new ErrorCode(410, "参数错误:{}");
    ErrorCode BAD_OPT = new ErrorCode(411, "操作错误:{}");
    ErrorCode CUSTOM_TIPS = new ErrorCode(440, "{}");

    // ========== 服务端错误段 ==========

    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统异常");
    ErrorCode TENANT_NOT_FOUND = new ErrorCode(501, "不存在租户编号");
    ErrorCode SERVER_ERROR = new ErrorCode(510, "服务异常:{}");

    // ========== 自定义错误段 ==========
    ErrorCode REPEATED_REQUESTS = new ErrorCode(900, "重复请求，请稍后重试");
    ErrorCode DEMO_DENY = new ErrorCode(901, "演示模式，禁止写操作");

    ErrorCode UNKNOWN = new ErrorCode(999, "未知错误");

    ErrorCode IS_WINDOWS = new ErrorCode(997, "windows");

    ErrorCode DATA_REPEATED = new ErrorCode(998, "数据重复");


    ErrorCode SENTINEL_LIMIT = new ErrorCode(10020, "服务治理");

    static boolean isMatch(Integer code) {
        return code != null
                && code >= SUCCESS.getCode() && code <= UNKNOWN.getCode();
    }

}
