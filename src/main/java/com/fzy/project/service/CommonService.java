package com.fzy.project.service;


import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.dto.common.GetInfoRequest;

public interface CommonService {
    /**
     * 根据角色返回个人信息
     * @param getInfoRequest
     * @return
     */
    public BaseResponse<? extends Object> getInfo(GetInfoRequest getInfoRequest);
}
