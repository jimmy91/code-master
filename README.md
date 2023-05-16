

## 项目描述

项目记录了不错的工具类和优秀的第三方框架，并提供了简单易用的代码示例和集成指南。旨在为开发人员提供便利，加快开发进程，简化繁琐的重复工作流程。  
目标是让Java开发变得更加**简单、快捷、高效**，并为您的开发工作提供更多的乐趣和成就感，助你早日成为编程专家。  
开 发 者：杨 * 玉(Jimmy)  
代码仓库：https://github.com/jimmy91/code-master


### 1.项目入口地址
|     标题      | 入口地址                                     | 描述  |
|:-----------:|------------------------------------------|-----|
| swagger接口文档 | http://localhost:8089/app/doc.html#/home |     |
|    web首页    | http://localhost:8089/app/view/index     |     |
|  IM Web聊天   | http://localhost:8089/app/view/im        |     |
|             |                                          |     |


### 2.目录结构描述
```
code-master
├── doc                         // 文件&笔记等
│   ├──                         
├── project.app                 // 应用服务
│   ├── annotation              // 注释类
│   ├── aop                     // 切面类
│   ├── config                  // 应用服务配置
│   ├── contanst                // 常量
│   ├── feign                   // Feign
│   ├── generator               // 自动生成API
│   ├── handler                 // 应用框架处理类
│   ├── netty                   // Netty webIM
│   ├── project                 // （通用）应用服务API
│   ├── Application             // 应用服务启动类
├── t_code                      // 框架应用服务&业务代码
│   ├── code                    // 框架应用服务启动类
│   │   ├── framework.*         // 第三方服务，拿来即用 
│   │   │   ├── geo.*           // redis GEO
│   │   │   ├── retry.*         // 重试
│   │   │   ├── asToken.*       // 鉴权验证工具
│   │   │   ├── screw.*         // 数据库文档生成工具
│   │   ├── netty.*             // netty websocket 
│   │   ├── oauth.*             // 鉴权 shiro
│   │   ├── queue               // 消息队列
│   │   ├── reusability.*       // 可复用（模仿）业务代码
│   │   ├── trace.*             // MDC链路追踪
├── tools.utils                 // 工具包
│   ├── algorithm               // 算法-布隆过滤器、排序、回溯、动态规划等
│   ├── design                  // 设计模式
│   ├── generator               // mybaits-plus自动生成工具
│   ├── jvm                     // JVM
│   ├── multi_thread            // 多线程
│   ├── ratelimter              // 限流工具类
│   ├── redisson                // Redisson
│   ├── tools.*                 // 工具类
├── readme.md                   // 说明文档

```
###  3.好用的技术文档
*  **「Good Good Study. Day Day Up.」**
   > 我们看过很多技术文章，却依然不知道架构代码应该咋整。
>
|            标题            | 参考文档地址                                                                                                                                                                         | 描述                            |
|:------------------------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------|
|      SpringBoot 多模块      | https://gitee.com/zhijiantianya/ruoyi-vue-pro <br/>   https://github.com/YunaiV/ruoyi-vue-pro                                                                                  | 芋道源码-最强的后台管理系统  -mini分支       |
|     SpringCloud 微服务      | https://gitee.com/zhijiantianya/yudao-cloud <br/>   https://github.com/YunaiV/yudao-cloud                                                                                      | 芋道源码-最强的后台管理系统                |
|       Mybatis-Plus       | https://baomidou.com/pages/24112f/                                                                                                                                             | MyBatis的增强工具                  |
|         XXL-Job          | https://www.xuxueli.com/xxl-job/                                                                                                                                               | 分布式任务调度平台                     |
|         Sa-Token         | https://sa-token.cc/doc.html#/up/mutex-login                                                                                                                                   | 轻量级 Java 权限认证框架               |
|         Easypoi          | http://easypoi.mydoc.io/ <br/> https://gitee.com/lemur/easypoi                                                                                                                 | 文档处理工具                        |
|         JustAuth         | https://github.com/justauth/JustAuth <br/> https://justauth.wiki/guide/                                                                                                        | 第三方授权登录的工具类库                  |
|         Knife4j          | https://doc.xiaominfo.com/                                                                                                                                                     | 集成Swagger生成Api文档的增强解决方案       |
|        Nacos注册中心         | https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html                                                                                                                     |                               |
|         阿里云链路追踪          | https://help.aliyun.com/document_detail/104185.html <br> https://arms.console.aliyun.com/#/integrations                                                                        |                               |
|         省市区GEO采集         | https://github.com/xiangyuecn/AreaCity-JsSpider-StatsGov                                                                                                                       |                               |
|        RabbitMQ安装        | https://blog.csdn.net/senir/article/details/122952669  <br> https://blog.csdn.net/Milogenius/article/details/125224527  <br> https://www.rabbitmq.com/community-plugins.html   | RabbitMQ 安装及插件                |
|       RabbitMQ练习场        | https://github.com/powerLeePlus/java-samples/tree/master/rabbitmq                                                                                                              |                               |
|      Sentinel限流熔断降级      | https://blog.csdn.net/csl12919/article/details/128033414 <br> https://www.cnblogs.com/buchizicai/p/17093746.html                                                               |                               |
| spring-cloud-alibaba中文文档 | https://github.com/alibaba/spring-cloud-alibaba/tree/2021.x/spring-cloud-alibaba-docs/src/main/asciidoc-zh   <br> https://blog.csdn.net/qq_42082701/article/details/100983913/ |                               |
|      springcloud学习       | https://www.macrozheng.com/cloud/cloud_catalog.html <br> https://github.com/macrozheng/springcloud-learning                                                                    |                               |
|          canal           | https://github.com/alibaba/canal/wiki/ClientExample                                                                                                                            | canal Mysql Binlog 的增量订阅和消费组件 |
|  X Spring File Storage   | https://spring-file-storage.xuyanwu.cn/#/                                                                                                                                      | 一行代码实现文件上传 20个平台              |
|      美团leaf分布式ID生成       | https://tech.meituan.com/2017/04/21/mt-leaf.html                                                                                                                               |                               |
|    arthas（阿尔萨斯）线上监控诊断    | https://arthas.aliyun.com/doc/quick-start.html                                                                                                                                 |                               |
|        京东零售(并发框架)        | https://gitee.com/jd-platform-opensource/asyncTool                                                                                                                             | 多线程并行、串行、阻塞、依赖、回调的并行框架        |
|        WebMagic爬虫        | http://webmagic.io/docs/zh/                                                                                                                                                    |                               |
|                          |                                                                                                                                                                                |                               |
|                          |                                                                                                                                                                                |                               |
|                          |                                                                                                                                                                                |                               |

###  4.内置功能

* mybaits-plus自动生成工具
* 限流工具类
* swagger接口文档
* 幂等防重复请求
* 基于Netty实现IM WEB聊天
* 阿里云链路追踪+SkyWalking
* screw 数据库文档一键生成
* spring retry 重试机制
* spring 文件处理
* MDC链路追踪




