package com.dongshan.tikutong.constant;

/**
 * Redis 常量
 */
public interface RedisConstant {
    /**
     * 用户签到记录的 Redis key 前缀
     */
    public static final String USER_SIGN_IN_REDIS_KEY_PREFIX = "user:signins";

    /**
     * 获取用户签到记录的 Redis key
     * @param year 年份
     * @param userId 用户id
     * @return
     */
    static String getUserSignInRedisKey(int year, long userId){
        return String.format("%s:%s:%s",USER_SIGN_IN_REDIS_KEY_PREFIX,year,userId);
    }
}
