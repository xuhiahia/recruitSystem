package com.fzy.project.controller;

import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.model.dto.blog.BlogCommentReportRequest;
import com.fzy.project.model.dto.comment.CommentAddRequest;
import com.fzy.project.model.dto.comment.CommentDeleteRequest;
import com.fzy.project.model.vo.BlogCommentDetailVO;
import com.fzy.project.model.vo.BlogCommentVO;
import com.fzy.project.service.BlogCommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/blogComment")
public class BlogCommentController {

    @Resource
    private BlogCommentService blogCommentService;

    /**
     * 添加评论
     * @param commentAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addComment(@RequestBody CommentAddRequest commentAddRequest){
        if(commentAddRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return blogCommentService.addComment(commentAddRequest);
    }

    /**
     * 获取评论的细节
     * @param id
     * @return
     */
    @GetMapping("/getDetail")
    public BaseResponse<List<BlogCommentDetailVO>> getDetail(String id){//id是评论头的id
        if(id==null||id.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long ids = Long.parseLong(id);
        return blogCommentService.getDetail(ids);
    }

    /**
     * 展示一级评论
     * @param blogId
     * @return
     */
    @GetMapping
    public BaseResponse<List<BlogCommentVO>> listComments(String blogId){//id是文章id
        if(blogId==null||blogId.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long id = Long.parseLong(blogId);
        return blogCommentService.listComment(id);
    }

    /**
     * 删除评论
     * @param commentDeleteRequest
     * @return
     */
    @DeleteMapping
    public BaseResponse<Boolean> deleComment(@RequestBody CommentDeleteRequest commentDeleteRequest){
        if(commentDeleteRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return blogCommentService.deleComment(commentDeleteRequest);
    }

    /**
     * 举报评论
     * @param blogCommentReportRequest
     * @return
     */
    @PostMapping("/report")
    public BaseResponse<Boolean> reportComment(@RequestBody BlogCommentReportRequest blogCommentReportRequest){
        if(blogCommentReportRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return blogCommentService.reportComment(blogCommentReportRequest);
    }

}
