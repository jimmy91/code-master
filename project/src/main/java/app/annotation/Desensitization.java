package app.annotation;


import app.handler.desensitization.DesensitizationTypeEnum;
import app.handler.desensitization.JacksonSerializeHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jimmy
 * 脱敏
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = JacksonSerializeHandler.class)
public @interface Desensitization {
    /**
     * 脱敏数据类型，只要在CUSTOMER的时候，startInclude和endExclude生效
     */
    DesensitizationTypeEnum type() default DesensitizationTypeEnum.CUSTOMER;

    /**
     * 开始位置（包含）
     */
    int startInclude() default 0;

    /**
     * 结束位置（不包含）
     */
    int endExclude() default 0;
}

