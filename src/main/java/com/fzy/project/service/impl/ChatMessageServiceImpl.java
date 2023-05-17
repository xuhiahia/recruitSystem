package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.ChatMessage;
import com.fzy.project.service.ChatMessageService;
import com.fzy.project.mapper.ChatMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【chat_message(聊天内容)】的数据库操作Service实现
* @createDate 2023-04-24 01:58:01
*/
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
    implements ChatMessageService{

}




