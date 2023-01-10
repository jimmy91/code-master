package app.aop;


import app.annotation.Idempotent;
import app.handler.idempotent.IdempotentKeyResolver;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import utils.generator.common.exception.GlobalErrorCodeConstants;
import utils.generator.common.exception.ServiceException;
import utils.tools.coll.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

/**
 * 拦截声明了 {@link Idempotent} 注解的方法，实现幂等操作
 * @author Jimmy
 */
@Aspect
@Slf4j
@Component
public class IdempotentAspect {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * IdempotentKeyResolver 集合
     */
    private Map<Class<? extends IdempotentKeyResolver>, IdempotentKeyResolver> keyResolvers;

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void IdempotentAspectInit() {
        Collection<IdempotentKeyResolver> keyResolverImpls = applicationContext.getBeansOfType(IdempotentKeyResolver.class).values();
        this.keyResolvers = CollectionUtils.convertMap(keyResolverImpls, IdempotentKeyResolver::getClass);;
    }

    @Before("@annotation(idempotent)")
    public void beforePointCut(JoinPoint joinPoint, Idempotent idempotent) {
        // 获得 IdempotentKeyResolver
        IdempotentKeyResolver keyResolver = keyResolvers.get(idempotent.keyResolver());
        Assert.notNull(keyResolver, "找不到对应的 IdempotentKeyResolver");
        // 解析 Key
        String key = keyResolver.resolver(joinPoint, idempotent);

        // 锁定 Key。
        boolean success = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "", idempotent.timeout(), idempotent.timeUnit()));
        // 锁定失败，抛出异常
        if (!success) {
            log.info("[beforePointCut][方法({}) 参数({}) 存在重复请求]", joinPoint.getSignature().toString(), joinPoint.getArgs());
            throw new ServiceException(GlobalErrorCodeConstants.REPEATED_REQUESTS);
        }
    }

}
