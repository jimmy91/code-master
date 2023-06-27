package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Swagger2 自动配置类
 * @author Jimmy
 */
@Configuration
@EnableSwagger2
// 允许使用 swagger.enable=false 禁用 Swagger
/*@ConditionalOnProperty(prefix = "pala.swagger", value = "enable", matchIfMissing = true)*/
public class SwaggerAutoConfiguration {

    /**
     * API 摘要信息
     */
    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("工具项目")
                .description("mini springboot项目")
                .contact(new Contact("Jimmy", null, null))
                .version("1.0.0")
                .build();
    }

    /**
     * 安全模式，这里配置通过请求头 Authorization 传递 token 参数
     */
    private static List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey(HttpHeaders.AUTHORIZATION, "Authorization", "header"));
    }

    /**
     * 安全上下文
     *
     * @see #securitySchemes()
     * @see #authorizationScopes()
     */
    private static List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(securityReferences())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
    }

    private static List<SecurityReference> securityReferences() {
        return Collections.singletonList(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes()));
    }

    private static AuthorizationScope[] authorizationScopes() {
        return new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")};
    }


    private static List<RequestParameter> globalRequestParameters() {
        RequestParameterBuilder authorizationParameter = new RequestParameterBuilder().name("Authorization").description("用户Token")
                .in(ParameterType.HEADER).query(parameterSpecificationBuilder -> parameterSpecificationBuilder
                        .model(modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING))
                        .defaultValue("Bearer "))
                .example(new ExampleBuilder().value("Bearer test").build());
        RequestParameterBuilder tenantParameter = new RequestParameterBuilder().name("tenant-id").description("租户编号")
                .in(ParameterType.HEADER).query(parameterSpecificationBuilder -> parameterSpecificationBuilder
                        .model(modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING))
                        .defaultValue("1"))
                .example(new ExampleBuilder().value(1L).build());
        List<RequestParameter> requestParameters = new ArrayList<>();
        requestParameters.add(authorizationParameter.build());
        requestParameters.add(tenantParameter.build());
        return requestParameters;
    }


    @Bean
    public Docket createProjectApi() {
        // 创建 Docket 对象
        return new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该 API 的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                .groupName("Project模块")
                // 设置扫描指定 package 包下的
                .select()
                .apis(RequestHandlerSelectors.basePackage("app.project"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(globalRequestParameters())
                .securityContexts(securityContexts());
    }


    @Bean
    public Docket createGeneratorApi() {
        // 创建 Docket 对象
        return new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该 API 的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                .groupName("Framework模块")
                // 设置扫描指定 package 包下的
                .select()
                .apis(RequestHandlerSelectors.basePackage("code.controller"))
                .paths(PathSelectors.any())
                //.paths(PathSelectors.regex("(?!/error.*).*"))
                .build()
                .globalRequestParameters(globalRequestParameters())
                .securityContexts(securityContexts());
    }




}
