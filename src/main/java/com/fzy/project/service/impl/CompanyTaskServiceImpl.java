package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.CompanyTask;
import com.fzy.project.service.CompanyTaskService;
import com.fzy.project.mapper.CompanyTaskMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【company_task(企业项目)】的数据库操作Service实现
* @createDate 2023-04-21 00:43:11
*/
@Service
public class CompanyTaskServiceImpl extends ServiceImpl<CompanyTaskMapper, CompanyTask>
    implements CompanyTaskService{

}




