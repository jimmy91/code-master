
## 工具集成类项目

记录不错的工具类及优秀的代码片段，提供便捷研发过程，简化重复工作流程 <br/>
作者：杨 * 玉(Jimmy)


### 1.项目入口地址
|     标题      | 入口地址                                     | 描述  |
|:-----------:|------------------------------------------|-----|
| swagger接口文档 | http://localhost:8089/app/doc.html#/home |     |
|    web首页    | http://localhost:8089/app/view/index     |     |
|  IM Web聊天   | http://localhost:8089/app/view/im        |     |
|             |                                          |     |


### 2.目录结构描述
```
tools_project
├── project.app                 // 应用服务
│   ├── annotation              // 注释类
│   ├── aop                     // 切面类
│   ├── config                  // 应用服务配置
│   ├── exception               // 异常处理类
│   ├── generator               // 自动生成API
│   ├── handler                 // 应用框架处理类
│   ├── project                 // （通用）应用服务API
│   ├── Application             // 应用服务启动类
├── t_code                      // 业务代码设计
│   ├── code                    // 应用服务启动类
│   │   ├── algorithm.*         // 算法 
│   │   ├── design.*            // 设计模式 
│   │   ├── netty.*             // netty websocket 
│   │   ├── reusability.*       // 可复用（模仿）业务代码
├── tools.utils                 // 工具包
│   ├── ratelimter              // 限流工具类
│   ├── generator               // mybaits-plus自动生成工具
│   ├── tools.*                 // 工具类
├── readme.md                   // 说明文档

```
###  3.好用的技术文档
* 「Good Good Study. Day Day Up」
    > 我们看过很多技术文章，却依然不知道架构代码该咋整。
  > 
|       标题        | 参考文档地址                                                                                        | 描述                      |
|:---------------:|-----------------------------------------------------------------------------------------------|-------------------------|
| SpringBoot 多模块  | https://gitee.com/zhijiantianya/ruoyi-vue-pro <br/>   https://github.com/YunaiV/ruoyi-vue-pro | 芋道源码-最强的后台管理系统  -mini分支 |
| SpringCloud 微服务 | https://gitee.com/zhijiantianya/yudao-cloud <br/>   https://github.com/YunaiV/yudao-cloud     | 芋道源码-最强的后台管理系统          |
|  Mybatis-Plus   | https://baomidou.com/pages/24112f/                                                            | MyBatis的增强工具            |
|     XXL-Job     | https://www.xuxueli.com/xxl-job/                                                              | 分布式任务调度平台               |
|    Sa-Token     | https://sa-token.cc/doc.html#/up/mutex-login                                                  | 轻量级 Java 权限认证框架         |
|     Easypoi     | http://easypoi.mydoc.io/ <br/> https://gitee.com/lemur/easypoi                                | 文档处理工具                  |
|    JustAuth     | https://github.com/justauth/JustAuth <br/> https://justauth.wiki/guide/                       | 第三方授权登录的工具类库            |
|     Knife4j     | https://doc.xiaominfo.com/                                                                    | 集成Swagger生成Api文档的增强解决方案 |
|                 |                                                                                               |                         |

###  4.内置功能
 
* mybaits-plus自动生成工具
* 限流工具类
* swagger接口文档
* 幂等防重复请求
* IM WEB聊天
* 链路跟踪
 
