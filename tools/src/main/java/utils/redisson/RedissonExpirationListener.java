package utils.redisson;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryCreatedListener;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;

import java.util.concurrent.TimeUnit;

/**
 * Redis 是一种高性能的内存键值存储系统，提供了丰富的数据结构和操作接口，包括字符串、列表、集合、哈希表、有序集合等，同时支持持久化、复制、高可用等功能。其中，Redis 的消息过期监听机制可以用来实现诸如缓存失效、任务调度等功能，下面简要介绍其使用场景和原理。
 *
 * 使用场景：
 * Redis 的消息过期监听机制通常用于实现以下功能：
 *
 * 缓存失效：将缓存对象设置过期时间，当过期时间到达后，自动将缓存对象从 Redis 中删除。
 * 任务调度：使用 Redis 的列表数据结构作为任务队列，将需要执行的任务作为列表元素插入到队列中，并为每个任务设置过期时间，当过期时间到达后，自动将任务从队列中弹出并执行。
 * 原理：
 * Redis 的消息过期监听机制基于 Redis 的事件机制实现，具体流程如下：
 *
 * 客户端向 Redis 发送 SET 命令，设置键值对，并为键设置过期时间。
 * Redis 保存键值对，并将过期时间设置为键的属性之一，同时启动一个定时器，定时检查键的过期时间。
 * 当定时器检测到键的过期时间已经到达时，Redis 会将该键从数据库中删除，并向已订阅该键的客户端发送一个过期事件。
 * 已订阅该键的客户端收到过期事件后，根据事件类型进行相应的处理，例如重新生成缓存、重新插入任务等。
 * 需要注意的是，Redis 的过期键监听机制并不是实时的，而是通过定时器定期检查过期键，因此存在一定的时间误差。同时，在 Redis 中保存过期键并不会对内存使用造成影响，因为 Redis 内部使用了一种惰性删除机制，只有在键被访问时才会进行删除操作。
 * @author Jimmy
 */
public class RedissonExpirationListener {

    public static void demoTest() throws Exception {
        RedissonClient redisson = RedissLockUtil.getRedissonClient();

        // 创建 MapCache，设置过期时间和监听器
        RMapCache<String, String> mapCache = redisson.getMapCache("testMapCache");
        mapCache.put("key1", "value1", 5, TimeUnit.SECONDS);
        mapCache.addListener(new EntryCreatedListener() {
            public void  onCreated(EntryEvent event) {
                System.out.println("创建：key=" + event.getKey() +
                        ", value=" + event.getValue() +
                        ", event=" + event.getType().name());
            }
        });
        mapCache.put("key2", "value2", 10, TimeUnit.SECONDS);
        mapCache.addListener(new EntryExpiredListener() {
            public void onExpired(EntryEvent event) {
                System.out.println("失效：key=" + event.getKey() +
                        ", value=" + event.getValue() +
                        ", event=" + event.getType().name());
            }
        });
        mapCache.put("key3", "value3", 15, TimeUnit.SECONDS);

    }
}
