package com.fzy.project.ws;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.exception.BusinessException;
import com.fzy.project.model.dto.ws.MessageRequest;
import com.fzy.project.model.entity.*;
import com.fzy.project.model.vo.MessageVO;
import com.fzy.project.model.vo.NewMessageVO;
import com.fzy.project.service.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static com.fzy.project.constant.UserConstant.ALIVE;
import static com.fzy.project.constant.UserConstant.DONE;
import static java.awt.SystemColor.text;

@Slf4j
@Component
@ServerEndpoint(value = "/chat/{userChatId}")
public class WebSocket {

    //线程安全的无序集合
    private static final CopyOnWriteArraySet<Session> SESSIONS = new CopyOnWriteArraySet<>();
    //存储连接
    private static final Map<String, Session> SESSION_POOL = new HashMap<>(0);

    private static ChatListService chatListService;

    private static UserChatService userChatService;

    private static ChatMessageService chatMessageService;

    private static UserService userService;

    private static CompanyService companyService;
    //存储当前消息
    private Session session;

    /**
     * 初始化service
     * @param chatListService
     */
    @Resource
    public void setHeatMapService(ChatListService chatListService) {
        WebSocket.chatListService = chatListService;
    }

    @Resource
    public void setHeatMapService(UserChatService userChatService) {
        WebSocket.userChatService = userChatService;
    }
    @Resource
    public void setHeatMapService(ChatMessageService chatMessageService) {
        WebSocket.chatMessageService = chatMessageService;
    }
    @Resource
    public void setHeatMapService(UserService userService) {
        WebSocket.userService = userService;
    }

    @Resource
    public void setHeatMapService(CompanyService companyService) {
        WebSocket.companyService = companyService;
    }
    /**
     * 建立连接的时候
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userChatId")String userChatId) {
        try {
            if (StringUtils.isBlank(userChatId) || "undefined".equals(userChatId)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            this.session = session;
            SESSIONS.add(session);
            SESSION_POOL.put(userChatId, session);
            log.info("连接成功,连接id:{}", userChatId);
            changeStatus(userChatId,ALIVE);
//            List<NewMessageVO> old = getOld(userChatId);
//            for(int i=0;i<old.size();i++){ //发送之前的消息
//                NewMessageVO msg = old.get(old.size() - 1 - i);
//                session.getAsyncRemote().sendText(JSONUtil.toJsonStr(msg));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接断开的时候
     */
    @OnClose
    public void onClose(Session session, @PathParam(value="userChatId")String userChatId) {
        try {
            if (!SESSION_POOL.isEmpty()) {
                SESSION_POOL.remove(userChatId);
                SESSIONS.remove(session);
            }
            log.info("连接断开：聊天id：{}",userChatId);
            changeStatus(userChatId,DONE); //用户不在当前聊天窗口

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息的时候
     * @param message
     */
    @OnMessage
    public void onMessage(String message,@PathParam("userChatId")String userChatId){ //发送者的聊天id


        NewMessageVO newMessageVO = sendOnlyOnline(message, userChatId);
        Long chatId = getChatId(userChatId);
        Session session1 = SESSION_POOL.get(userChatId);
        Session session2 = SESSION_POOL.get(chatId.toString());
        savaMessage(message,userChatId);
        session1.getAsyncRemote().sendText(JSONUtil.toJsonStr(newMessageVO));

        if(session2!=null){
            session2.getAsyncRemote().sendText(JSONUtil.toJsonStr(newMessageVO));
        }else{
            chatListService.update().setSql("unread=unread+1").eq("user_chat_id", chatId).update();
        }


    }

    /**
     * 保存聊天记录
     * @param msg
     * @param userChatId
     */
    private void savaMessage(String msg,String userChatId){
        ChatMessage chatMessage = new ChatMessage();
        long chatId = Long.parseLong(userChatId);
        UserChat chat = userChatService.getById(chatId);
        chatMessage.setUserChatId(chat.getId());
        chatMessage.setChatContent(msg);
        chatMessage.setReceiveUser(chat.getReceiveUser());
        chatMessage.setSendUser(chat.getSendUser());
        chatMessageService.save(chatMessage);
    }
    private NewMessageVO sendOnlyOnline(String message,String userChatId){
        NewMessageVO newMessageVO = new NewMessageVO();
        UserChat userChat = userChatService.getById(Long.parseLong(userChatId));
        newMessageVO.setContent(message);
        User sendUser = userService.getById(userChat.getSendUser());
        if(sendUser==null){//说明是企业
            Company company = companyService.getById(userChat.getSendUser());
            newMessageVO.setFromAvatar(company.getCompanyAvatar());
            newMessageVO.setFromName(company.getCompanyName());
            return newMessageVO;
        }
        newMessageVO.setFromName(sendUser.getUserName());
        newMessageVO.setFromAvatar(sendUser.getAvatarUrl());
        return newMessageVO;
    }


    /**
     * 修改用户是否在聊天窗口的状态
     * @param userChatId
     * @param status
     */
    private void changeStatus(String userChatId,Integer status){
        long chatId = Long.parseLong(userChatId);
        ChatList chat = chatListService.getOne(new QueryWrapper<ChatList>().eq("user_chat_id", chatId));
        chat.setSendWindow(status);
        chatListService.updateById(chat);
        Long sendUser = chat.getSendUser();
        Long receiveUser = chat.getReceiveUser();
        ChatList one = chatListService.getOne(new QueryWrapper<ChatList>().eq("send_user", receiveUser).eq("receive_user", sendUser));
        one.setReceiveWindow(status);
        chatListService.updateById(one);
    }



    /**
     * 保存聊天记录
     * @param userChatId
     * @param messageContent
     */
    private void savaChat(String userChatId, String messageContent) { //被接受的id
        long chatId = Long.parseLong(userChatId);
        UserChat chat = userChatService.getById(chatId);
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setChatContent(messageContent);
        chatMessage.setUserChatId(chatId);
        chatMessage.setSendUser(chat.getReceiveUser());
        chatMessage.setReceiveUser(chat.getSendUser());
        chatMessageService.save(chatMessage);
    }


    private Long getChatId(String userChatId){ //根据发送者的ID获取接收者视图 或者根据接收者获取发送者
        UserChat userchat = userChatService.getById(userChatId);
        Long sendUser = userchat.getSendUser();
        Long receiveUser = userchat.getReceiveUser();
        UserChat one = userChatService.getOne(new QueryWrapper<UserChat>().eq("send_user", receiveUser).eq("receive_user", sendUser));
        return one.getId();
    }

    private List<NewMessageVO> getOld(String userChatId){//获取旧的记录
        long chatId = Long.parseLong(userChatId);
        Long chatId1 = getChatId(userChatId);
        List<ChatMessage> list = chatMessageService.list(new QueryWrapper<ChatMessage>()
                .eq("user_chat_id", chatId).
                or()
                .eq("user_chat_id", chatId1).orderByDesc("create_time").last("limit 10"));
        List<NewMessageVO> newMessageVOS = list.stream().map(it -> {
            NewMessageVO newMessageVO = new NewMessageVO();
            User user = userService.getById(it.getSendUser());
            newMessageVO.setFromAvatar(user.getAvatarUrl());
            newMessageVO.setFromName(user.getUserName());
            newMessageVO.setContent(it.getChatContent());
            return newMessageVO;
        }).collect(Collectors.toList());
        return newMessageVOS;
    }

}
