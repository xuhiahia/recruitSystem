package com.fzy.project.common;

import com.fzy.project.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 用户登录校验工具
 *
 * @author ShuaiGeF
 * @date 2023/1/13 20:18
 */
public class LoginVerifyUtils {

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public static long getUserId(StringRedisTemplate stringRedisTemplate) {
        String tokenKey = UserHolder.get();
        if (StringUtils.isBlank(tokenKey)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String userIdStr = stringRedisTemplate.opsForValue().get(tokenKey);
        if (StringUtils.isBlank(userIdStr)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return Long.parseLong(userIdStr);
    }

    /**
     * 获取 tokenKey
     *
     * @param stringRedisTemplate
     * @return tokenKey
     */
    public static String getTokenKey(StringRedisTemplate stringRedisTemplate) {
        String tokenKey = UserHolder.get();
        if (StringUtils.isBlank(tokenKey)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return tokenKey;
    }

}
