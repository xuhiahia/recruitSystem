package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.UserChief;
import com.fzy.project.service.UserChiefService;
import com.fzy.project.mapper.UserChiefMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【user_chief(用户-岗位对应表)】的数据库操作Service实现
* @createDate 2023-04-18 21:57:09
*/
@Service
public class UserChiefServiceImpl extends ServiceImpl<UserChiefMapper, UserChief>
    implements UserChiefService{

}




