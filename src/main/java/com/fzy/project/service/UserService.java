package com.fzy.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.dto.note.NoteUpdateRequest;
import com.fzy.project.model.dto.user.*;
import com.fzy.project.model.dto.ws.MessageAddRelationRequest;
import com.fzy.project.model.entity.Blog;
import com.fzy.project.model.entity.Information;
import com.fzy.project.model.entity.User;
import com.fzy.project.model.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 徐小帅
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-04-16 23:28:17
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    BaseResponse<UserVO> userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    BaseResponse<?extends Object>  userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 展示招聘信息
     * @param userCheckChiefRequest
     * @return
     */
    public BaseResponse<List<ChiefVO>> listChief(UserCheckChiefRequest userCheckChiefRequest);

    /**
     * 查看具体的招聘信息
     * @param id
     * @return
     */
    public BaseResponse<ChiefDetailVO> getChiefInfo(Long id);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    public BaseResponse<UserDataVO> getUser(Long id);

    /**
     * 用户读取简历
     * @return
     */
    public BaseResponse<UserNoteVO> getNote(Long id);

    /**
     * 修改简历
     * @return
     */
    public BaseResponse<Boolean> updateNote(NoteUpdateRequest noteUpdateRequest);

    /**
     * 展示收藏
     * @param id
     * @return
     */
    public BaseResponse<List<UserShareVO>> listShare(Long id);

    /**
     * 删除收藏文章
     * @param userDeleteShareRequest
     * @return
     */
    public BaseResponse<Boolean> deleShare(UserDeleteShareRequest userDeleteShareRequest);

    /**
     * 展示用户的系统消息
     * @param id
     * @return
     */
    public BaseResponse<List<InformationVO>> listSystemInfo(long id);

    /**
     * 用户给企业项目添加评论
     * @param userAddTaskCommentRequest
     * @return
     */
    public BaseResponse<Boolean> addTaskComment(UserAddTaskCommentRequest userAddTaskCommentRequest);

    /**
     * 用户查看项目评论
     * @param id
     * @return
     */
    public BaseResponse<List<CompanyTaskCommentVO>> listTaskComment(long id);

    /**
     * 用户提交项目
     * @param userCommitTaskRequest
     * @return
     */
    public BaseResponse<Boolean> userCommitTask(UserCommitTaskRequest userCommitTaskRequest);

    /**
     * 用户提交简历
     * @param userCommitNoteRequest
     * @return
     */
    public BaseResponse<Boolean> userCommitNote(UserCommitNoteRequest userCommitNoteRequest);

    /**
     * 用户查看聊天列表
     * @param id
     * @return
     */
    public BaseResponse<List<ChatListVO>> listChat(Long id);

    /**
     * 用户添加聊天关系
     * @param messageAddRelationRequest
     * @return
     */
    public BaseResponse<Boolean> addMessage(MessageAddRelationRequest messageAddRelationRequest);

    /**
     * 获取历史记录
     * @param userChatId
     * @return
     */
    public BaseResponse<List<NewMessageVO>> getMemory(String userChatId);
}
