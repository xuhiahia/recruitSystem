package com.fzy.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.model.dto.notice.NoticeAddRequest;
import com.fzy.project.model.dto.notice.NoticeDeleteRequest;
import com.fzy.project.model.dto.notice.NoticeQueryRequest;
import com.fzy.project.model.entity.Admins;
import com.fzy.project.model.entity.Notice;
import com.fzy.project.model.vo.NoticeVO;
import com.fzy.project.service.AdminsService;
import com.fzy.project.service.NoticeService;
import com.fzy.project.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 徐小帅
* @description 针对表【notice(管理员通知表)】的数据库操作Service实现
* @createDate 2023-04-23 04:15:51
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

    @Resource
    private AdminsService adminsService;
    @Resource
    private NoticeService noticeService;

    /**
     * 添加通知
     * @param noticeAddRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> addNotice(NoticeAddRequest noticeAddRequest) {
        Notice notice = new Notice();
        notice.setAdminId(notice.getAdminId());
        BeanUtil.copyProperties(noticeAddRequest,notice);
        boolean isSuccess = noticeService.save(notice);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除通知
     * @param noticeDeleteRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> deleNotice(NoticeDeleteRequest noticeDeleteRequest) {
        List<Long> noticeIds = noticeDeleteRequest.getIds();
        if(noticeIds==null||noticeIds.size()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = removeBatchByIds(noticeIds);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 展示管理员的通知列表
     * @param noticeQueryRequest
     * @return
     */
    @Override
    public BaseResponse<List<NoticeVO>> listNotice(NoticeQueryRequest noticeQueryRequest) {
        LambdaQueryWrapper<Notice> query = setQuery(noticeQueryRequest);
        List<Notice> notices = list(query);
        List<NoticeVO> noticeVOS = notices.stream().map(notice -> {
            NoticeVO noticeVO = new NoticeVO();
            BeanUtil.copyProperties(notice, noticeVO, "createTime");
            String time = DateUtil.formatTime(notice.getCreateTime());
//            Admins admin = adminsService.getById(notice.getAdminId());
            noticeVO.setCreateTime(time);
            noticeVO.setAdminName("管理员");

            return noticeVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(noticeVOS);
    }

    private LambdaQueryWrapper<Notice> setQuery(NoticeQueryRequest noticeQueryRequest){
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        String noticeTitle = noticeQueryRequest.getNoticeTitle();
        if(StrUtil.isNotBlank(noticeTitle)){
            queryWrapper.like(Notice::getNoticeTitle,noticeTitle);
        }
//        String adminIdStr = noticeQueryRequest.getAdminId();
//        if(adminIdStr!=null){
//            queryWrapper.eq(Notice::getAdminId,Long.parseLong(adminIdStr));
//        }
        return queryWrapper;
    }

}




