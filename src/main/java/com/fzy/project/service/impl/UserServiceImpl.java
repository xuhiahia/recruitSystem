package com.fzy.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.exception.BusinessException;
import com.fzy.project.mapper.UserChatMapper;
import com.fzy.project.mapper.UserMapper;
import com.fzy.project.model.dto.note.NoteUpdateRequest;
import com.fzy.project.model.dto.user.*;
import com.fzy.project.model.dto.ws.MessageAddRelationRequest;
import com.fzy.project.model.entity.*;
import com.fzy.project.model.vo.*;
import com.fzy.project.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.intellij.lang.annotations.RegExp;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.util.DigestUtils;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static com.fzy.project.constant.RedisConstant.BLOG_COLLECT_KEY;
import static com.fzy.project.constant.UserConstant.*;

/**
* @author 徐小帅
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-04-16 23:28:17
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private InformationService informationService;
    @Resource
    private UserMapper userMapper;

    @Resource
    private ChiefService chiefService;

    @Resource
    private CompanyService companyService;

    @Resource
    private AdminsService adminsService;

    @Resource
    private NoteService noteService;

    @Resource
    private UserShareService userShareService;

    @Resource
    private BlogService blogService;

    @Resource
    private UserTaskService userTaskService;

    @Resource
    private UserTaskCommentService userTaskCommentService;

    @Resource
    private CompanyTaskService companyTaskService;

    @Resource
    private UserChiefService userChiefService;

    @Resource
    private ChatListService chatListService;

    @Resource
    private ChatMessageService chatMessageService;

    @Resource
    private UserChatService userChatService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "fzy";

    @Override
    public BaseResponse<UserVO> userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            //11111111111111111111111111111111111111111111111111111111
            User user = new User();
            user.setPhone(userAccount);
            user.setUserPwd(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user,userVO);
            //创建简历
            Note note = new Note();
            note.setUserId(user.getId());
            noteService.save(note);
            //创建系统消息
            Information information = new Information();
            information.setInformationTitle("注册成功");
            information.setInformationContent("欢迎您使用该系统，祝您前程似锦");
            information.setUserId(user.getId());
            informationService.save(information);
            return ResultUtils.success(userVO);
        }
    }

    @Override
    public BaseResponse<?extends Object> userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,userAccount);
        queryWrapper.eq(User::getUserPwd,encryptPassword);
        User user = this.getOne(queryWrapper);
        //管理员
        LambdaQueryWrapper<Admins> adminLQ = new LambdaQueryWrapper<>();
        adminLQ.eq(Admins::getAdminAccount,userAccount);
        adminLQ.eq(Admins::getAdminPwd,encryptPassword);
        Admins admins = adminsService.getOne(adminLQ);
        //公司
        LambdaQueryWrapper<Company> companyLQ = new LambdaQueryWrapper<>();
        companyLQ.eq(Company::getCompanyAccount,userAccount);
        companyLQ.eq(Company::getCompanyPwd,encryptPassword);
        Company company = companyService.getOne(companyLQ);
        // 用户不存在
        if (user != null) {
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user,userVO,"role");
            userVO.setRole("user");
            return ResultUtils.success(userVO);
        }else if(admins!=null){
            request.getSession().setAttribute(ADMIN_ROLE, admins);
            AdminVO adminVO = new AdminVO();
            adminVO.setRole("admin");
            BeanUtil.copyProperties(admins,adminVO,"role");
            return ResultUtils.success(adminVO);
        }else if(company!=null){
            request.getSession().setAttribute(COMPANY_ROLE,company);
            CompanyVO companyVO = new CompanyVO();
            BeanUtil.copyProperties(user,companyVO,"role");
            companyVO.setRole("company");
            companyVO.setId(company.getId());
            return ResultUtils.success(companyVO);
        }
        // 3. 记录用户的登录态
        log.info("user login failed, userAccount cannot match userPassword");
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }



    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public BaseResponse<List<ChiefVO>> listChief(UserCheckChiefRequest userCheckChiefRequest) {
        Collection<Chief> chiefs = setQuery(userCheckChiefRequest);
//        List<Chief> chiefs = chiefService.list(queryWrapper);
        List<ChiefVO> chiefVOS = chiefs.stream().map(chief -> {
            ChiefVO chiefVO = new ChiefVO();
            BeanUtil.copyProperties(chief, chiefVO, "chiefStatus");
            Long companyId = chief.getCompanyId();
            if(companyId==null||companyId<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"岗位缺少公司");
            }
            Company company = companyService.getById(companyId);
            chiefVO.setCompanyName(company.getCompanyName());
            chiefVO.setCompanyIndustry(company.getCompanyIndustry());
            chiefVO.setCompanyAddress(company.getAddress());
            chiefVO.setCompanyId(chief.getCompanyId());
            chiefVO.setCompanyAvatar(company.getCompanyAvatar());
            return chiefVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(chiefVOS);
    }

    @Override
    public BaseResponse<ChiefDetailVO> getChiefInfo(Long id) {
        Chief chief = chiefService.getById(id);
        if(chief==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"岗位已下架");
        }
        Long companyId = chief.getCompanyId();
        Company company = companyService.getById(companyId);
        if(company==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"公司已下架");
        }
        ChiefDetailVO chiefVO = new ChiefDetailVO();
        BeanUtil.copyProperties(chief,chiefVO,"chiefStatus");
        chiefVO.setCompanyAddress(company.getAddress());
        chiefVO.setCompanyIndustry(company.getCompanyIndustry());
        chiefVO.setCompanyName(company.getCompanyName());
        String mark = company.getMark();
//        if(mark!=null){
            chiefVO.setCompanyMark(mark);
//        }
        chiefVO.setCompanyScale(company.getScale()==0?"小":company.getScale()==1?"中":"大");
        return ResultUtils.success(chiefVO);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @Override
    public BaseResponse<UserDataVO> getUser(Long id) {
        if (id <= 0 ||id ==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = this.getById(id);
        UserDataVO userVO = new UserDataVO();
        BeanUtils.copyProperties(user, userVO,"gender");
        Integer gender = user.getGender();
        if(gender!=null){
            userVO.setGender(gender ==1?"男":"女");
        }
        return ResultUtils.success(userVO);
    }

    /**
     * 用户查看简历
     * @param id
     * @return
     */
    @Override
    public BaseResponse<UserNoteVO> getNote(Long id) {
        LambdaQueryWrapper<Note> noteLQ = new LambdaQueryWrapper<>();
        noteLQ.eq(Note::getUserId,id);
        Note note = noteService.getOne(noteLQ);
        if(note==null){ //帮他创建简历
            Note noteNew = createNote(id);
            note=noteNew;
//            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"没有简历");
        }
        UserNoteVO userNoteVO = new UserNoteVO();
        BeanUtil.copyProperties(note,userNoteVO,"jobStatus","noteId");
        userNoteVO.setNoteId(note.getId());
        Integer jobStatus = note.getJobStatus();
        if(jobStatus!=null){
            String jobStatusStr="";
            if(jobStatus==0){
                jobStatusStr="离校随时到岗";
            }else if(jobStatus==1){
                jobStatusStr="在校月内到岗";
            }else if (jobStatus==2){
                jobStatusStr="在校考虑机会";
            }else {
                jobStatusStr="在校暂不考虑";
            }
            userNoteVO.setJobStatus(jobStatusStr);
        }
        User user = this.getById(id);
        if(user==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"用户不存在");
        }
        if(user.getNoteId()==null){ //刚刚创建的
            user.setNoteId(note.getId());
        }
        this.updateById(user);
        userNoteVO.setUserName(user.getUserName());
        userNoteVO.setAge(user.getAge());
        userNoteVO.setGender(user.getGender()==0?"女":"男");
        return ResultUtils.success(userNoteVO);
    }

    /**
     * 用户修改简历
     * @param noteUpdateRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> updateNote(NoteUpdateRequest noteUpdateRequest) {
        Long noteId = noteUpdateRequest.getNoteId();
        if(noteId==null||noteId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Note note = noteService.getById(noteId);
        if(note==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"简历不存在");
        }
        BeanUtil.copyProperties(noteUpdateRequest,note,"jobStatus");
        String jobStatus = noteUpdateRequest.getJobStatus();
        Integer status = note.getJobStatus();
        if(jobStatus!=null){
            if("离校随时到岗".equals(jobStatus)){
                status=0;
            }else if("在校月内到岗".equals(jobStatus)){
                status=1;
            }else if ("在校考虑机会".equals(jobStatus)){
                status=2;
            }else {
                status=3;
            }
        }
        note.setJobStatus(status);
        boolean isSuccess = noteService.updateById(note);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 展示收藏
     * @param id
     * @return
     */
    @Override
    public BaseResponse<List<UserShareVO>> listShare(Long id) {
        LambdaQueryWrapper<UserShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserShare::getUserId,id);
        List<UserShare> shares = userShareService.list(queryWrapper);
        List<UserShareVO> shareVOs = shares.stream().map((share) -> {
            Long blogId = share.getBlogId();
            Blog blog = blogService.getById(blogId);
            UserShareVO userShareVO = new UserShareVO();
            if (blog == null) {
                userShareVO.setBlogTitle("文章已失效");
                userShareVO.setId(blogId);
                return userShareVO;
            }
            BeanUtil.copyProperties(blog,userShareVO);
            User user = this.getById(blog.getUserId());

            userShareVO.setBlogUserAvatar(user.getAvatarUrl());
            String blogImages = blog.getBlogImages();
            if(blogImages!=null) {
                String[] split = blogImages.split(",");
                List<String> images = new ArrayList<>();
                Collections.addAll(images, split);
                userShareVO.setBlogImages(images);
            }
            BeanUtil.copyProperties(share, userShareVO,"createTime","id");
            String formatTime = DateUtil.formatTime(share.getCreateTime());
            userShareVO.setCreateTime(formatTime);
            userShareVO.setBlogContent(blog.getBlogContent());
            return userShareVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(shareVOs);
    }

    /**
     * 删除用户收藏文章
     * @param userDeleteShareRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> deleShare(UserDeleteShareRequest userDeleteShareRequest) {
        Long blogId = userDeleteShareRequest.getBlogId();
        Long userId = userDeleteShareRequest.getUserId();
        if(userId==null||userId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        if(blogId==null||blogId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<UserShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserShare::getBlogId,blogId);
        boolean isSuccess = userShareService.remove(queryWrapper);
        if(isSuccess){ //处理是否删除了收藏
            String userSet=BLOG_COLLECT_KEY+blogId;
            Double score = stringRedisTemplate.opsForZSet().score(userSet, userId.toString());
            if(score!=null) {//删除
                boolean isSuccess1 = blogService.update().setSql("blog_collections=blog_collections-1").eq("id", blogId).update();
                if (isSuccess1) {

                        stringRedisTemplate.opsForZSet().remove(userSet, userId.toString());

                }
            }
        }
        return ResultUtils.success(isSuccess);
    }

    /**
     * 展示用户收到的系统消息
     * @param id
     * @return
     */
    @Override
    public BaseResponse<List<InformationVO>> listSystemInfo(long id) {
        LambdaQueryWrapper<Information> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Information::getUserId,id);
        List<Information> information = informationService.list(queryWrapper);
        List<InformationVO> infos = information.stream().map(infor -> {
            InformationVO informationVO = new InformationVO();
            BeanUtil.copyProperties(infor, informationVO, "createTime");
            informationVO.setCreateTime(DateUtil.formatTime(infor.getCreateTime()));
            return informationVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(infos);
    }

    /**
     * 用户给企业项目添加评论
     * @param userAddTaskCommentRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> addTaskComment(UserAddTaskCommentRequest userAddTaskCommentRequest) {
        Long userId = userAddTaskCommentRequest.getUserId();
        Long taskId = userAddTaskCommentRequest.getTaskId();
        UserTask userTask = userTaskService.getOne(new QueryWrapper<UserTask>().eq("user_id", userId).eq("task_id",taskId));
        if(userTask==null){ //没提交过
            return ResultUtils.success(false);
        }
        UserTaskComment userTaskComment = new UserTaskComment();
        BeanUtil.copyProperties(userAddTaskCommentRequest,userTaskComment);
        User user = this.getById(userId);
        userTaskComment.setUserName(user.getUserName());
        userTaskComment.setUserAvatar(user.getAvatarUrl());
        userTaskComment.setCompanyTaskId(userAddTaskCommentRequest.getTaskId());
        boolean isSuccess = userTaskCommentService.save(userTaskComment);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 用户查看项目评论
     * @param id
     * @return
     */
    @Override
    public BaseResponse<List<CompanyTaskCommentVO>> listTaskComment(long id) {//taskId
        List<UserTaskComment> userTaskComments = userTaskCommentService.list(new QueryWrapper<UserTaskComment>().eq("company_task_id", id).orderByDesc("create_time"));
        List<CompanyTaskCommentVO> commmentVOS = userTaskComments.stream().map(task -> {
            CompanyTaskCommentVO comment = new CompanyTaskCommentVO();
            BeanUtil.copyProperties(task, comment, "createTime");
            comment.setCreateTime(DateUtil.formatTime(task.getCreateTime()));
            User user = getById(task.getUserId());
            if(user.getAvatarUrl()!=null){
                comment.setUserAvatar(user.getAvatarUrl());
            }
            return comment;
        }).collect(Collectors.toList());
        return ResultUtils.success(commmentVOS);
    }

    /**
     * 用户提交项目
     * @param userCommitTaskRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> userCommitTask(UserCommitTaskRequest userCommitTaskRequest) {
        Long userId = userCommitTaskRequest.getUserId();
        if(userId==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"用户参数错误");
        }
        UserTask one = userTaskService.getOne(new QueryWrapper<UserTask>().eq("user_id", userId).eq("task_id", userCommitTaskRequest.getTaskId()));
        if(one==null){//之前提交过
            return ResultUtils.success(false);
        }
        UserTask userTask = new UserTask();
        BeanUtil.copyProperties(userCommitTaskRequest,userTask);
        CompanyTask task = companyTaskService.getById(userCommitTaskRequest.getTaskId());
        if(task==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"公司任务错误");
        }
        userTask.setTaskTitle(task.getTaskTitle());
        userTask.setTaskType(task.getTaskType());
        boolean isSuccess = userTaskService.save(userTask);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 用户提交简历
     * @param userCommitNoteRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> userCommitNote(UserCommitNoteRequest userCommitNoteRequest) {
        String noteUrl = userCommitNoteRequest.getNoteUrl();
        Long userId = userCommitNoteRequest.getUserId();
        User user = this.getById(userId);
        if(user==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"用户已注销");
        }
        //判断之前有没有提交过
        UserChief one = userChiefService.getOne(new LambdaQueryWrapper<UserChief>().eq(UserChief::getChiefId, userCommitNoteRequest.getChiefId()).eq(UserChief::getUserId, userId));
        if(one!=null){
            return ResultUtils.success(false);
        }
        Note note = noteService.getById(user.getNoteId());
        UserChief userChief = new UserChief();
        if(noteUrl!=null){ //有上传简历附件
            note.setNoteUrl(noteUrl);
            noteService.updateById(note);
        }
        BeanUtil.copyProperties(userCommitNoteRequest,userChief);
        boolean isSuccess = userChiefService.save(userChief);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 用户查看聊天列表
     * @param id
     * @return
     */
    @Override
    public BaseResponse<List<ChatListVO>> listChat(Long id) {//userId
        //我作为发送方
        List<ChatList> sendUser = chatListService.list(new QueryWrapper<ChatList>().eq("send_user", id));
        List<ChatListVO> chatListVOS = sendUser.stream().map(send -> {
            ChatListVO chatListVO = new ChatListVO();
            Long receiveUser = send.getReceiveUser();//拿到接收方的id
            chatListVO.setUserChatId(send.getUserChatId());
            chatListVO.setCreateTime(DateUtil.formatTime(send.getUpdateTime()));
            chatListVO.setReceiveId(receiveUser);
            setLastMessage(chatListVO,id,send);
            User user = this.getById(receiveUser);
            if (user == null) { //可能是公司
                Company company = companyService.getById(receiveUser);

                chatListVO.setReceiveAvatar(company.getCompanyAvatar());
                chatListVO.setReceiveName(company.getCompanyName());
                chatListVO.setUnread(send.getUnread());
                return chatListVO;

            }
            chatListVO.setReceiveAvatar(user.getAvatarUrl());
            chatListVO.setReceiveName(user.getUserName());
            chatListVO.setUnread(send.getUnread());

            return chatListVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(chatListVOS);
    }

    /**
     * 用户添加聊天关系
     * @param messageAddRelationRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> addMessage(MessageAddRelationRequest messageAddRelationRequest) {
        return null;
    }

    /**
     * 获取用户聊天历史记录
     * @param
     * @return
     */
    @Override
    public BaseResponse<List<NewMessageVO>> getMemory(String userChatId) {
        long chatId = Long.parseLong(userChatId);
        Long chatId1 = getChatId(userChatId);
        List<ChatMessage> list = chatMessageService.list(new QueryWrapper<ChatMessage>()
                .eq("user_chat_id", chatId).
                or()
                .eq("user_chat_id", chatId1).orderByDesc("create_time").last("limit 10"));
        List<NewMessageVO> newMessageVOS = list.stream().map(it -> {
            NewMessageVO newMessageVO = new NewMessageVO();
            User user = this.getById(it.getSendUser());
            if(user==null){
                Company company = companyService.getById(it.getSendUser());
                newMessageVO.setFromAvatar(company.getCompanyAvatar());
                newMessageVO.setFromName(company.getCompanyName());
                newMessageVO.setContent(it.getChatContent());
                return newMessageVO;
            }
            newMessageVO.setFromAvatar(user.getAvatarUrl());
            newMessageVO.setFromName(user.getUserName());
            newMessageVO.setContent(it.getChatContent());
            return newMessageVO;
        }).collect(Collectors.toList());
        ArrayList<NewMessageVO> jsons = new ArrayList<>();
        for(int i=0;i<newMessageVOS.size();i++){ //发送之前的消息
            NewMessageVO msg = newMessageVOS.get(newMessageVOS.size() - 1 - i);

            jsons.add(msg);
        }
        return ResultUtils.success(jsons);
    }


    private void setLastMessage(ChatListVO chatListVO,Long id,ChatList send){ //id是userId
        Integer unread = send.getUnread();
        if(unread>0){ //我作为发送方，有未读消息，最后一条消息是接收方发的
            Long receiveId = chatListVO.getReceiveId();
            ChatMessage one = chatMessageService.getOne(new QueryWrapper<ChatMessage>().eq("send_user", receiveId).eq("receive_user", id).eq("is_latest", 0));
            if(one==null){
                return; //说明没发过消息
            }
            chatListVO.setLastMessage(one.getChatContent());
        }else{ //我作为发送方，没有未读消息，最后一条消息是我发的
            ChatMessage one = chatMessageService.getOne(new QueryWrapper<ChatMessage>().eq("user_chat_id", chatListVO.getUserChatId()).eq("is_latest", 0));
            if(one==null){ //说明没发过消息
                return ;
            }
            chatListVO.setLastMessage(one.getChatContent());
        }
    }


    /**
     * 设置查询条件
     * @param userCheckChiefRequest
     * @return
     */
    private Collection<Chief> setQuery(UserCheckChiefRequest userCheckChiefRequest){
        LambdaQueryWrapper<Chief> queryWrapper = new LambdaQueryWrapper<>();
        String chiefName = userCheckChiefRequest.getChiefName();
        String chiefSalary = userCheckChiefRequest.getChiefSalary();
        String chiefAddress = userCheckChiefRequest.getChiefAddress();
        String companyName = userCheckChiefRequest.getCompanyName();
        if(StrUtil.isNotBlank(chiefName)){
            queryWrapper.like(Chief::getChiefName,chiefName);
        }
        if(StrUtil.isNotBlank(chiefSalary)){
//            queryWrapper.eq(Chief::getChiefSalary,chiefSalary);
            String start = chiefSalary.substring(0, chiefSalary.indexOf("-"));
            String end = chiefSalary.substring(chiefSalary.indexOf("-") + 1);
            Integer endMoney=0;
            Integer startMoney=0;
            if(StrUtil.isNotBlank(end)){
                 endMoney = Integer.parseInt(end);
            }
            if(StrUtil.isNotBlank(start)){
                 startMoney=Integer.parseInt(start);
            }

            if(StrUtil.isEmpty(start)&&StrUtil.isNotBlank(end)){//给最大 <=
                queryWrapper.le(Chief::getChiefSalary,endMoney);
            }
            if(StrUtil.isEmpty(end)&&StrUtil.isNotBlank(start)){//给最小
                queryWrapper.ge(Chief::getChiefSalary,startMoney);
            }
            if(endMoney+startMoney!=0){ //正常
                queryWrapper.between(Chief::getChiefSalary,startMoney,endMoney);
            }
        }
        if(StrUtil.isNotBlank(chiefAddress)){
            queryWrapper.like(Chief::getChiefAddress,chiefAddress);
        }
        queryWrapper.eq(Chief::getChiefStatus,1);
        List<Chief> list = chiefService.list(queryWrapper);
        //公司名字
        if(StrUtil.isNotBlank(companyName)){
            LambdaQueryWrapper<Company> companyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            companyLambdaQueryWrapper.like(Company::getCompanyName,companyName);
            List<Company> companyList = companyService.list(companyLambdaQueryWrapper);
            Set<Long> idCollect = companyList.stream().map(item -> {
                return item.getId();
            }).collect(Collectors.toSet());
            ArrayList<Long> idList = new ArrayList<>(idCollect);  //根据公司名字得到的id
            List<Chief> chiefByCompanyId = getChiefByCompanyId(idList);
            Collection<Chief> intersection = CollectionUtil.intersection(chiefByCompanyId, list);
            return intersection;
        }
        return list;
    }

    private List<Chief> getChiefByCompanyId(List<Long> ids){
        ArrayList<Chief> chiefs = new ArrayList<>();
        for(Long id:ids){
            List<Chief> companyChief = chiefService.list(new QueryWrapper<Chief>().eq("company_id", id));
            chiefs.addAll(companyChief);
        }
        return chiefs;
    }

    private Long getChatId(String userChatId){ //根据发送者的ID获取接收者视图 或者根据接收者获取发送者
        UserChat userchat = userChatService.getById(userChatId);
        Long sendUser = userchat.getSendUser();
        Long receiveUser = userchat.getReceiveUser();
        UserChat one = userChatService.getOne(new QueryWrapper<UserChat>().eq("send_user", receiveUser).eq("receive_user", sendUser));
        return one.getId();
    }

    /**
     * 给用户创建一个简历
     * @param userId
     * @return
     */
    private Note  createNote(Long userId){
        Note note = new Note();
        note.setUserId(userId);
        boolean save = noteService.save(note);
        if(save){
            return note;
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR,"简历创建失败");
    }
}




