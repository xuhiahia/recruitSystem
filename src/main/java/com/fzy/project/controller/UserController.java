package com.fzy.project.controller;

import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.DeleteRequest;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.exception.BusinessException;
import com.fzy.project.model.dto.note.NoteUpdateRequest;
import com.fzy.project.model.dto.user.*;
import com.fzy.project.model.dto.ws.MessageAddRelationRequest;
import com.fzy.project.model.entity.User;
import com.fzy.project.model.vo.*;
import com.fzy.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author ShuaiGeF
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<UserVO> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<?extends Object>  userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        String gender = userUpdateRequest.getGender();
        user.setGender("女".equals(userUpdateRequest.getGender())?0:1);
        BeanUtils.copyProperties(userUpdateRequest, user);
        if(userUpdateRequest.getPwd()!=null&&userUpdateRequest.getNewPwd()!=null){
            String encryptPassword = DigestUtils.md5DigestAsHex(("fzy" + userUpdateRequest.getPwd()).getBytes());
            User userOld = userService.getById(userUpdateRequest.getId());
            String userPwd = userOld.getUserPwd();

            if(!encryptPassword.equals(userPwd)){
                return ResultUtils.error(ErrorCode.OPERATION_ERROR,"密码错误");
            }
            if (userUpdateRequest.getNewPwd().length() < 8) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
            }
            String newPwd = DigestUtils.md5DigestAsHex(("fzy" + userUpdateRequest.getNewPwd()).getBytes());
            user.setUserPwd(newPwd);
        }
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserDataVO> getUserById(Long id, HttpServletRequest request) {
        if (id <= 0 ||id ==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        UserDataVO userVO = new UserDataVO();
        BeanUtils.copyProperties(user, userVO,"gender");
        userVO.setGender(user.getGender()==1?"男":"女");
        return ResultUtils.success(userVO);
    }

    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
//    @GetMapping("/list")
//    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest, HttpServletRequest request) {
//        User userQuery = new User();
//        if (userQueryRequest != null) {
//            BeanUtils.copyProperties(userQueryRequest, userQuery);
//        }
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
//        List<User> userList = userService.list(queryWrapper);
//        List<UserVO> userVOList = userList.stream().map(user -> {
//            UserVO userVO = new UserVO();
//            BeanUtils.copyProperties(user, userVO);
//            return userVO;
//        }).collect(Collectors.toList());
//        return ResultUtils.success(userVOList);
//    }

    /**
     * 分页获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
//    @GetMapping("/list/page")
//    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest, HttpServletRequest request) {
//        long current = 1;
//        long size = 10;
//        User userQuery = new User();
//        if (userQueryRequest != null) {
//            BeanUtils.copyProperties(userQueryRequest, userQuery);
//            current = userQueryRequest.getCurrent();
//            size = userQueryRequest.getPageSize();
//        }
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
//        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
//        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
//        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
//            UserVO userVO = new UserVO();
//            BeanUtils.copyProperties(user, userVO);
//            return userVO;
//        }).collect(Collectors.toList());
//        userVOPage.setRecords(userVOList);
//        return ResultUtils.success(userVOPage);
//    }
    // endregion

    /**
     * 学生查看招聘首页
     * @param userCheckChiefRequest
     * @return
     */
    @GetMapping("/list/chief")
    public BaseResponse<List<ChiefVO>> listChief(UserCheckChiefRequest userCheckChiefRequest){
        if(userCheckChiefRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.listChief(userCheckChiefRequest);
    }

    /**
     * 学生查看某个岗位的具体信息
     * @param id
     * @return
     */
    @GetMapping("/chief")
    public BaseResponse<ChiefDetailVO> chiefInfo(Long id){
        if(id==null||id<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.getChiefInfo(id);
    }

    /**
     * 学生查看简历
     * @param id
     * @return
     */
    @GetMapping("/note")
    public BaseResponse<UserNoteVO> getNote(String id){
        long ids = Long.parseLong(id);
        if(ids<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.getNote(ids);
    }

    /**
     * 学生修改简历
     * @param noteUpdateRequest
     * @return
     */
    @PostMapping("/note")
    public BaseResponse<Boolean> updateNote(@RequestBody NoteUpdateRequest noteUpdateRequest){
        if(noteUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.updateNote(noteUpdateRequest);
    }

    /**
     * 展示收藏列表
     * @param id
     * @return
     */
    @GetMapping("/share")
    public BaseResponse<List<UserShareVO>> listShare(String id){
        if(id==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long ids = Long.parseLong(id);
        if(ids<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.listShare(ids);
    }

    /**
     * 用户删除收藏的
     * @param userDeleteShareRequest
     * @return
     */
    @DeleteMapping("/delete/share")
    public BaseResponse<Boolean> deleShare(@RequestBody UserDeleteShareRequest userDeleteShareRequest){
        if (userDeleteShareRequest==null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.deleShare(userDeleteShareRequest);
    }

    /**
     * 展示用户的系统消息
     * @param userId
     * @return
     */
    @GetMapping("/information")
    public BaseResponse<List<InformationVO>> listSystemInfo(String userId){
        if (userId==null||userId.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long id = Long.parseLong(userId);
        return userService.listSystemInfo(id);
    }

    /**
     * 用户评价企业项目
     * @param userAddTaskCommentRequest
     * @return
     */
    @PutMapping("/add/taskComment")
    public BaseResponse<Boolean> addTaskComment(@RequestBody UserAddTaskCommentRequest userAddTaskCommentRequest){
        if(userAddTaskCommentRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.addTaskComment(userAddTaskCommentRequest);
    }

    /**
     * 用户查看企业项目评论
     * @param taskId
     * @return
     */
    @GetMapping("/task")
    public BaseResponse<List<CompanyTaskCommentVO>> listTaskComment(String taskId){
        if(taskId==null||taskId.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long id = Long.parseLong(taskId);
        return userService.listTaskComment(id);
    }

    /**
     * 用户提交项目
     * @param userCommitTaskRequest
     * @return
     */
    @PutMapping("/commit/task")
    public BaseResponse<Boolean> userCommitTask(@RequestBody UserCommitTaskRequest userCommitTaskRequest){
        if(userCommitTaskRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.userCommitTask(userCommitTaskRequest);
    }

    /**
     * 用户提交简历
     * @param userCommitNoteRequest
     * @return
     */
    @PutMapping("/commit/note")
    public BaseResponse<Boolean> userCommitNote(@RequestBody UserCommitNoteRequest userCommitNoteRequest){
        if(userCommitNoteRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.userCommitNote(userCommitNoteRequest);
    }


    /**
     * 用户查看聊天列表
     * @param userId
     * @return
     */
    @GetMapping("/list/chat")
    public BaseResponse<List<ChatListVO>> userGetChatList(String userId){
        if(userId==null|| userId.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long id = Long.parseLong(userId);
        return userService.listChat(id);
    }

    /**
     * 用户添加聊天关系
     * @param messageAddRelationRequest
     * @return
     */
    @PutMapping("/add/chat")
    public BaseResponse<Boolean> addMessage(@RequestBody MessageAddRelationRequest messageAddRelationRequest){
        if(messageAddRelationRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.addMessage(messageAddRelationRequest);
    }

    /**
     * 获取历史记录
     * @param userChatId
     * @return
     */
    @GetMapping("/memory")
    public BaseResponse<List<NewMessageVO>> getMemory(String userChatId){
        if(userChatId==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.getMemory(userChatId);
    }
}
