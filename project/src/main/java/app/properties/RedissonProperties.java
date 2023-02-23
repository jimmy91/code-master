package app.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jimmy
 */
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonProperties {

    private int timeout = 3000;

    private String host;

    private int port;

    private String address;

    private String password;

    private int connectionPoolSize = 64;
    
    private int connectionMinimumIdleSize=10;

    private int slaveConnectionPoolSize = 250;

    private int masterConnectionPoolSize = 250;

    private String[] sentinelAddresses;

    private String masterName;

    public String getAddress() {
        return "http://"+host+":"+port;
    }
}
