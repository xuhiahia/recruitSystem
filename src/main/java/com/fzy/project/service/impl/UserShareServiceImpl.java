package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.UserShare;
import com.fzy.project.service.UserShareService;
import com.fzy.project.mapper.UserShareMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【user_share(用户收藏表)】的数据库操作Service实现
* @createDate 2023-04-21 20:40:45
*/
@Service
public class UserShareServiceImpl extends ServiceImpl<UserShareMapper, UserShare>
    implements UserShareService{

}




