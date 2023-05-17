package com.fzy.project.controller;

import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.model.dto.blog.*;
import com.fzy.project.model.vo.BlogMeVO;
import com.fzy.project.model.vo.BlogVO;
import com.fzy.project.service.BlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    /**
     * 添加文章
     * @param blogAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addBlog(@RequestBody BlogAddRequest blogAddRequest){
        if(blogAddRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return blogService.addBlog(blogAddRequest);
    }

    /**
     * 展示文章
     * @param blogQueryRequest
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<BlogVO>> listBlog(BlogQueryRequest blogQueryRequest){
        if(blogQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        if(blogQueryRequest.getUserId()==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"userId为空");
        }
        return blogService.listBlog(blogQueryRequest);
    }

    /**
     * 给文章点赞/取消
     * @param blogLikeRequest
     * @return
     */
    @PostMapping("/like")
    public BaseResponse<Boolean>likeBlog(@RequestBody BlogLikeRequest blogLikeRequest){
        if (blogLikeRequest==null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return blogService.likeBlog(blogLikeRequest);
    }

    /**
     * 收藏文章/取消
     * @param blogCollectRequest
     * @return
     */
    @PostMapping("/collect")
    public BaseResponse<Boolean> collectBlog(@RequestBody BlogCollectRequest blogCollectRequest){
        if(blogCollectRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return blogService.collectBlog(blogCollectRequest);
    }

    /**
     * 查看自己的文章
     * @param userId
     * @return
     */
    @GetMapping("/me")
    public BaseResponse<List<BlogMeVO>> listMyBlog(String userId){//userId
        if(userId==null||userId.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long id = Long.parseLong(userId);
        if(id<=0){

        }
        return blogService.listMyBlog(id);
    }

    /**
     * 删除文章
     * @param blogDeleteRequest
     * @return
     */
    @DeleteMapping
    public BaseResponse<Boolean> deleBlog(@RequestBody BlogDeleteRequest blogDeleteRequest){
        if (blogDeleteRequest==null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long blogId = blogDeleteRequest.getBlogId();
        if(blogId==null||blogId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        return blogService.deleBlog(blogId);
    }

    /**
     * 举报blog
     * @param blogReportRequest
     * @return
     */
    @PostMapping ("/report")
    public BaseResponse<Boolean> reportBlog(@RequestBody BlogReportRequest blogReportRequest){
        if(blogReportRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long blogId = blogReportRequest.getBlogId();
        if(blogId==null||blogId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return blogService.reportBlog(blogId);
    }

}
