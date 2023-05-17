package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.dto.chief.ChiefAddRequest;
import org.springframework.stereotype.Service;

import com.fzy.project.mapper.ChiefMapper;
import com.fzy.project.model.entity.Chief;
import com.fzy.project.service.ChiefService;

/**
* @author 徐小帅
* @description 针对表【chief(招聘岗位表)】的数据库操作Service实现
* @createDate 2023-04-18 14:08:56
*/
@Service
public class ChiefServiceImpl extends ServiceImpl<ChiefMapper, Chief>
    implements ChiefService{


}




