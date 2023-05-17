package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.model.entity.Information;
import com.fzy.project.service.InformationService;
import com.fzy.project.mapper.InformationMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【information(系统通知)】的数据库操作Service实现
* @createDate 2023-04-23 16:15:25
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

}




