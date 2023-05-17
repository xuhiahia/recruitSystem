package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.UserReport;
import com.fzy.project.service.UserReportService;
import com.fzy.project.mapper.UserReportMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【user_report(用户举报表)】的数据库操作Service实现
* @createDate 2023-04-23 16:15:25
*/
@Service
public class UserReportServiceImpl extends ServiceImpl<UserReportMapper, UserReport>
    implements UserReportService{

}




