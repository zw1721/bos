package com.taotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    private interface Function<R> {
        public R run(ShardedJedis shardedJedis);
    }

    private <R> R execute(Function<R> func) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            // 保存数据
            return func.run(shardedJedis);
        } finally {
            if (null != shardedJedis) {
                shardedJedis.close();
            }
        }
    }

    /**
     * 执行Set命令
     * 
     * @param key
     * @param value
     */
    public String set(final String key, final String value) {
        return this.execute(new Function<String>() {
            @Override
            public String run(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
            }
        });
    }

    /**
     * 执行get命令
     * 
     * @param key
     */
    public String get(final String key) {
        return this.execute(new Function<String>() {
            @Override
            public String run(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }

    /**
     * 执行del命令
     * 
     * @param key
     */
    public Long del(final String key) {
        return this.execute(new Function<Long>() {
            @Override
            public Long run(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
            }
        });
    }

    /**
     * 执行expire命令
     * 
     * @param key
     */
    public Long expire(final String key, final Integer seconds) {
        return this.execute(new Function<Long>() {
            @Override
            public Long run(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, seconds);
            }
        });
    }

    /**
     * 执行set命令，同时设置时间
     * 
     * @param key
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute(new Function<String>() {
            @Override
            public String run(ShardedJedis shardedJedis) {
                // 先添加
                String result = shardedJedis.set(key, value);
                // 在设置时间
                shardedJedis.expire(key, seconds);
                return result;
            }
        });
    }
}
