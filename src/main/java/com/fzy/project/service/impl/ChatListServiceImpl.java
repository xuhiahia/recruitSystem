package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.ChatList;
import com.fzy.project.service.ChatListService;
import com.fzy.project.mapper.ChatListMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【chat_list】的数据库操作Service实现
* @createDate 2023-04-24 01:58:01
*/
@Service
public class ChatListServiceImpl extends ServiceImpl<ChatListMapper, ChatList>
    implements ChatListService{

}




