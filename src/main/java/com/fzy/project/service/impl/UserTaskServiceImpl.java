package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.UserTask;
import com.fzy.project.service.UserTaskService;
import com.fzy.project.mapper.UserTaskMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【user_task(企业项目用户对应表)】的数据库操作Service实现
* @createDate 2023-04-21 00:43:11
*/
@Service
public class UserTaskServiceImpl extends ServiceImpl<UserTaskMapper, UserTask>
    implements UserTaskService{

}




