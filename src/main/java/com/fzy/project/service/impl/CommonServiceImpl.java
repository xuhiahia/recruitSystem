package com.fzy.project.service.impl;

import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.exception.BusinessException;
import com.fzy.project.model.dto.common.GetInfoRequest;
import com.fzy.project.service.CommonService;
import com.fzy.project.service.CompanyService;
import com.fzy.project.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private CompanyService companyService;

    @Resource
    private UserService userService;

    /**
     * 根据角色返回用户信息
     * @param getInfoRequest
     * @return
     */
    @Override
    public BaseResponse<? extends Object> getInfo( GetInfoRequest getInfoRequest) {
        String role = getInfoRequest.getRole();
        if(role==null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"用户角色错误");
        }
        Long id = getInfoRequest.getId();
        if(role.equals("user")){
           return  userService.getUser(id);
        }else if (role.equals("company")){
            return companyService.getCompanyById(id);
        }
//        throw new BusinessException(ErrorCode.OPERATION_ERROR,"发生错误");
        return ResultUtils.success("管理员");
    }
}
