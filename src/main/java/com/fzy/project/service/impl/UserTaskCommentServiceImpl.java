package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.UserTaskComment;
import com.fzy.project.service.UserTaskCommentService;
import com.fzy.project.mapper.UserTaskCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【user_task_comment(企业项目用户评价表)】的数据库操作Service实现
* @createDate 2023-04-23 15:53:17
*/
@Service
public class UserTaskCommentServiceImpl extends ServiceImpl<UserTaskCommentMapper, UserTaskComment>
    implements UserTaskCommentService{

}




