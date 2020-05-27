package cn.chm.o2o.cache;

import cn.chm.o2o.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisUtilTest extends BaseTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testRedisUtil() {
        String key = "arealist";
        String value = "keysValue";
        System.out.println(redisUtil.containsKey(key));
        redisUtil.removeKeys("area");
        System.out.println(redisUtil.containsKey(key));
    }
}
