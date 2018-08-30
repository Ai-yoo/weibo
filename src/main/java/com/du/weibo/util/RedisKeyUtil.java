package com.du.weibo.util;


public class RedisKeyUtil {

    private static String SPLIT = ":";
    // 获取粉丝,,
    private static String BIZ_FOLLOWER = "FOLLOWER";
    // 关注对象,,
    private static String BIZ_FOLLOWEE = "FOLLOWEE";


    /**
     * 设置粉丝
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    /**
     * 设置关注
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
}
