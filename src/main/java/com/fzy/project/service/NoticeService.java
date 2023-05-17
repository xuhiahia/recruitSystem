package com.fzy.project.service;

import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.dto.notice.NoticeAddRequest;
import com.fzy.project.model.dto.notice.NoticeDeleteRequest;
import com.fzy.project.model.dto.notice.NoticeQueryRequest;
import com.fzy.project.model.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fzy.project.model.vo.NoticeVO;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【notice(管理员通知表)】的数据库操作Service
* @createDate 2023-04-23 04:15:51
*/
public interface NoticeService extends IService<Notice> {

    /**
     * 管理员添加通知
     * @param noticeAddRequest
     * @return
     */
    public BaseResponse<Boolean> addNotice(NoticeAddRequest noticeAddRequest);

    /**
     * 删除通知
     * @param noticeDeleteRequest
     * @return
     */
    public BaseResponse<Boolean> deleNotice(NoticeDeleteRequest noticeDeleteRequest);

    /**
     * 展示通知
     * @param noticeQueryRequest
     * @return
     */
    public BaseResponse<List<NoticeVO>> listNotice(NoticeQueryRequest noticeQueryRequest);
}
