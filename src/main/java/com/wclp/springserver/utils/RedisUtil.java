package com.wclp.springserver.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 读取数据
     */
    public String getString(final String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public Object getHash(final String key, String hashKey) {
        return redisTemplate.opsForHash().get(key,hashKey);
    }
    public String getList(final String key,long index) {
        return redisTemplate.opsForList().index("testList",index);
    }

    /**
     * 写入数据
     */
    public boolean setString(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public long setList(final String key, String value) {
        long result = 0;
        try {
            result = redisTemplate.opsForList().leftPush("testList","testList1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新数据
     */
    public boolean getAndSetString(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean getAndSetList(final String key,Long index, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForList().set(key,index,value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public void setHash(final String key, String hashKey, String value) {
        try {
            redisTemplate.opsForHash().put(key,hashKey,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除数据
     */
    public boolean delete(final String key) {
        boolean result = false;
        try {
            redisTemplate.delete(key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

