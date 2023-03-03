## Java

> ### 笔记点
>> #### 参考文档地址
>> 关键词：**
>
>> 内容
>
>

多线程中future中get方法是怎么实现的

> ### seate与elk分布式事务
>> #### 参考文档地址
>> 关键词：**
>
>> 内容
>

SpringCloud包含的组件很多，有很多功能是重复的。其中最常用组件包括：
•注册中心组件：Eureka、Nacos等
•负载均衡组件：Ribbon
•远程调用组件：OpenFeign
•网关组件：Zuul、Gateway
•服务保护组件：Hystrix、Sentinel
•服务配置管理组件：SpringCloudConfig、Nacos


> ### Feign
>> #### https://blog.csdn.net/BASK2311/article/details/128048199
>> 关键词：**
>
>> 内容
>

> ### hystrix与sentinel熔断降级
>> #### https://github.com/alibaba/Sentinel/wiki/Sentinel-%E4%B8%8E-Hystrix-%E7%9A%84%E5%AF%B9%E6%AF%94
>> 关键词：**
>
>> 内容
>

> ### 系统架构
>> #### 参考文档地址  
>> 关键词：**
>![img_1.png](../img/img_1.png)

> ### 多线程参数传递
>> ####  https://blog.csdn.net/fuliyingIT/article/details/128603599 <br> https://www.cnblogs.com/throwable/p/12817754.html
>> 关键词：ThreadLocal、InheritableThreadLocal(可遗传本地线程)、TransmittableThreadLocal(可传递本地线程)、TaskDecorator(线程池设置)
>
>> - TheadLocal单个线程生命周期强绑定，只能在某个线程的生命周期内对ThreadLocal进行存取，不能跨线程存取。
>> - InheritableThreadLocal这种方案不建议使用，在线程池中使用会存在失败的问题，原因：InheritableThreadLocal 在父线程创建子线程的时候，会将父线程当前存在的本地线程变量拷贝到子线程的本地线程变量中，但是在web的容器中使用了线程池，线程会被创建回收重复的利用，不会被销毁重新创建，所以会存在失效的场景。  
>> - TransmittableThreadLocal是阿里开源的工具，在线程提交的时候要进行上下文的复制,解决了InheritableThreadLocal不能进行线程池间传递数据的缺陷，在使用线程池等会池化复用线程的执行组件情况下，提供ThreadLocal值的传递功能，解决异步执行时上下文传递的问题。  
>> - TaskDecorator这是一个执行回调方法的装饰器，主要应用于传递上下文，或者提供任务的监控/统计信息。  
>> 示例：ThreadApp.java
>


> ### Spring - 三级缓存 + 循环依赖解决
>> #### https://www.cnblogs.com/frankcui/p/13449973.html  
>> Spring为上层应用提供bean的生命周期管理。二级缓存是为了解决循环依赖，三级缓存就是给AOP动态代理准备的，进行提前爆光对象而不生成代理对象。三级缓存实例化bean[只生成引用]，会自动升级到二级缓存，初始化完成，升级为一级缓存 <br>
>![img.png](../img/img.png)
>> - singletonObjects：用于存放完全初始化好的 bean，从该缓存中取出的 bean 可以直接使用  
>> - earlySingletonObjects：提前曝光的单例对象的cache，存放原始的 bean 对象（尚未填充属性），用于解决循环依赖  
>> - singletonFactories：单例对象工厂的cache，存放 bean 工厂对象，用于动态代理对象
> 


> ### Zookeeper集群“脑裂”问题处理
>> #### https://blog.51cto.com/mingongge/3063433 <br>
>> 关键词：Zookeeper容错、奇数部署、过半机制、假死、Leader、follower、Quorums(法定人数)、epoch(标号) <br>
>
>> 脑裂：由于假死会发起新的Leader选举，选举出一个新的Leader，但旧的Leader网络又通了，导致出现了两个Leader ，有的客户端连接到老的Leader，而有的客户端则连接到新的leader。<br>
ZooKeeper默认采用了Quorums这种方式来防止"脑裂"现象。<br/>
假设某个Leader假死，其余的followers选举出了一个新的Leader。这时，旧的Leader复活并且仍然认为自己是Leader，这个时候它向其他followers发出写请求也是会被拒绝的。
因为每当新Leader产生时，会生成一个epoch标号(标识当前属于那个Leader的统治时期)，这个epoch是递增的，followers如果确认了新的Leader存在，知道其epoch，就会拒绝epoch小于现任Leader epoch的所有请求。
那有没有follower不知道新的Leader存在呢？有可能，但肯定不是大多数，否则新Leader无法产生。Zookeeper的写也遵循quorum机制，因此，得不到大多数支持的写是无效的，旧Leader即使各种认为自己是leader，依然没有什么作用。
>


> ### RabbitMq
>> #### https://github.com/powerLeePlus/java-samples/tree/master/rabbitmq   <br>
>> 关键词：交换机、路由、消息、持久化、Ack机制、消费者确认：basicAck、消费者拒绝：basicNack、生产者确认的两种模式：事务与confirm机制<br>
>
>> 当你声明了一个名为”hello_queue”的队列，RabbitMQ会自动将其绑定到默认交换机上，绑定（binding）的路由键名称也是为”hello_queue”,消费者通过绑定的路由进行消费。<br>
> 如果要设置重回队列的话，要设置最大处理次数，例如为3，记录消费者处理失败的次数，当处理失败次数小于3调用Nack重回队列；如果达到了最大重试次数，则调用Ack删除消息，同时持久化该消息，后期采用定时任务或者手工处理。
> - Fanout exchange（扇型交换机）- Publish/Subscribe（发布订阅） - 一对多消费
> - Direct exchange（直连交换机）- 够对消息按照一定的规则进行转发，消息会被推送至binding key和消息routing key完全匹配的队列，即使用特定的 binding key发送消息 - 一对一消费
> - Topic exchange（主题交换机）- 发送到topic交换机的消息不能具有任意的  routing_key —— 它必须是由点分隔的单词列表   
    > \*（星号）可以替代一个单词， ＃（hash）可以替换零个或多个单词, 主题类型的转发器非常强大，可以实现其他类型的转发器。  
    当一个队列与绑定键#绑定，将会收到所有的消息，类似fanout类型转发器。    
    当绑定键中不包含任何#与\*时，类似direct类型转发器。
> - Dead Letter Exchange（DLX-死信交换机）  
    > 死信消息有如下几种情况：1、消息被拒绝(Basic.Reject/Basic.Nack) ，井且设置requeue(重回队列) 参数为false ; 2、消息过期 ; 3、队列达到最大长度
>

> ### java相关学习示例
>> #### https://github.com/powerLeePlus/java-samples <br>
>> 这里有很多示例，可以学习




 