package com.fzy.project.service;

import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.dto.blog.BlogCommentReportRequest;
import com.fzy.project.model.dto.comment.CommentAddRequest;
import com.fzy.project.model.dto.comment.CommentDeleteRequest;
import com.fzy.project.model.entity.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fzy.project.model.vo.BlogCommentDetailVO;
import com.fzy.project.model.vo.BlogCommentVO;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【blog_comment(文章评论)】的数据库操作Service
* @createDate 2023-04-21 00:43:10
*/
public interface BlogCommentService extends IService<BlogComment> {

    /**
     * 添加评论
     * @param commentAddRequest
     * @return
     */
    public BaseResponse<Boolean> addComment(CommentAddRequest commentAddRequest);

    /**
     * 获取某个评论的细节
     * @param id
     * @return
     */
    public BaseResponse<List<BlogCommentDetailVO>> getDetail(Long id);

    /**
     * 展示一级评论
     * @param id
     * @return
     */
    public BaseResponse<List<BlogCommentVO>> listComment(Long id);

    /**
     * 删除评论
     * @param commentDeleteRequest
     * @return
     */
    public BaseResponse<Boolean> deleComment(CommentDeleteRequest commentDeleteRequest);

    /**
     * 举报评论
     * @param blogCommentReportRequest
     * @return
     */
    public BaseResponse<Boolean> reportComment(BlogCommentReportRequest blogCommentReportRequest);
}
