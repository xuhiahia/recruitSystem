package com.fzy.project.controller;

import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.model.dto.notice.NoticeAddRequest;
import com.fzy.project.model.dto.notice.NoticeDeleteRequest;
import com.fzy.project.model.dto.notice.NoticeQueryRequest;
import com.fzy.project.model.vo.NoticeVO;
import com.fzy.project.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 管理员添加通知
     * @param noticeAddRequest
     * @return
     */
    @PutMapping("/add")
    public BaseResponse<Boolean> addNotice(@RequestBody NoticeAddRequest noticeAddRequest){
        if(noticeAddRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return noticeService.addNotice(noticeAddRequest);
    }

    /**
     * 管理员删除通知
     * @param noticeDeleteRequest
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleNotice(@RequestBody NoticeDeleteRequest noticeDeleteRequest){
        if(noticeDeleteRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
       return noticeService.deleNotice(noticeDeleteRequest);
    }

    /**
     * 展示通知列表
     * @param noticeQueryRequest
     * @return
     */
    @GetMapping
    public BaseResponse<List<NoticeVO>> listNotice(NoticeQueryRequest noticeQueryRequest){
        if(noticeQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return noticeService.listNotice(noticeQueryRequest);
    }



}
