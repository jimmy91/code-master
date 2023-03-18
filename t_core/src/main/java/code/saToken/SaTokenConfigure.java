package code.saToken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册 Sa-Token 拦截器，打开注解式鉴权功能
 *
 * @author Jimmy
 * https://sa-token.cc/doc.html#/use/route-check
 */
@Configuration
@Slf4j
public class SaTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        // 校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> {
                    log.info("进入鉴权校验 url={}", SaHolder.getRequest().getRequestPath());
                    StpUtil.checkLogin();
                }))
               .addPathPatterns("/oauth/token/**");
    }

}