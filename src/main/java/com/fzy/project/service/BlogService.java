package com.fzy.project.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.dto.blog.BlogAddRequest;
import com.fzy.project.model.dto.blog.BlogCollectRequest;
import com.fzy.project.model.dto.blog.BlogLikeRequest;
import com.fzy.project.model.dto.blog.BlogQueryRequest;
import com.fzy.project.model.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fzy.project.model.vo.BlogMeVO;
import com.fzy.project.model.vo.BlogVO;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【blog(文章)】的数据库操作Service
* @createDate 2023-04-21 11:24:26
*/
public interface BlogService extends IService<Blog> {
    /**
     * 添加文章
     * @param blogAddRequest
     * @return
     */
    public BaseResponse<Boolean> addBlog(BlogAddRequest blogAddRequest);

    /**
     * 展示文章
     * @param blogQueryRequest
     * @return
     */
    public BaseResponse<List<BlogVO>> listBlog(BlogQueryRequest blogQueryRequest);

    /**
     * 给文章点赞
     * @param blogLikeRequest
     * @return
     */
    public BaseResponse<Boolean> likeBlog(BlogLikeRequest blogLikeRequest);

    /**
     * 收藏文章
     * @param blogCollectRequest
     * @return
     */
    public BaseResponse<Boolean> collectBlog(BlogCollectRequest blogCollectRequest);


    /**
     * 查看自己的文章
     * @param id
     * @return
     */
    public BaseResponse<List<BlogMeVO>> listMyBlog(Long id);

    /**
     * 删除文章
     * @param
     * @return
     */
    public BaseResponse<Boolean> deleBlog(Long blogId);

    /**
     * 举报文章
     * @param blogId
     * @return
     */
    public BaseResponse<Boolean> reportBlog(Long blogId);
}
