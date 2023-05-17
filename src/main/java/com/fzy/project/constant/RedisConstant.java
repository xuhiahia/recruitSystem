package com.fzy.project.constant;

/**
 * @author ShuaiGeF
 * @date 2023/1/2 22:03
 */
public interface RedisConstant {
    String LOGIN_CODE_KEY = "login_code:";
    Long LOGIN_CODE_TTL = 5L;
    String LOGIN_USER_KEY = "login_token:";
    Long LOGIN_USER_TTL = 30L;

    String BLOG_LIKE_KEY="recruitSystem:blog:liked:";

    String BLOG_COLLECT_KEY="recruitSystem:blog:collect";
}
