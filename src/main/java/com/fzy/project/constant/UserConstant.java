package com.fzy.project.constant;

/**
 * 用户常量
 *
 * @author ShuaiGeF
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 系统用户 id（虚拟用户）
     */
    long SYSTEM_USER_ID = 0;

    //  region 权限

    /**
     * 默认权限
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员权限
     */
    String ADMIN_ROLE = "admin";
    /**
     * 公司
     */
    String COMPANY_ROLE="company";
    /**
     * 聊天框状态 0-在线 1-下线
     */
    Integer DONE=0;
    Integer ALIVE=1;


    // endregion
}
