package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.UserChat;
import com.fzy.project.service.UserChatService;
import com.fzy.project.mapper.UserChatMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【user_chat(用户聊天关系主表)】的数据库操作Service实现
* @createDate 2023-04-24 01:58:01
*/
@Service
public class UserChatServiceImpl extends ServiceImpl<UserChatMapper, UserChat>
    implements UserChatService{

}




