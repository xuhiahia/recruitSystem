package com.fzy.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.exception.BusinessException;
import com.fzy.project.model.dto.blog.BlogAddRequest;
import com.fzy.project.model.dto.blog.BlogCollectRequest;
import com.fzy.project.model.dto.blog.BlogLikeRequest;
import com.fzy.project.model.dto.blog.BlogQueryRequest;
import com.fzy.project.model.entity.Blog;
import com.fzy.project.model.entity.User;
import com.fzy.project.model.entity.UserReport;
import com.fzy.project.model.entity.UserShare;
import com.fzy.project.model.vo.BlogMeVO;
import com.fzy.project.model.vo.BlogVO;
import com.fzy.project.service.BlogService;
import com.fzy.project.mapper.BlogMapper;
import com.fzy.project.service.UserReportService;
import com.fzy.project.service.UserService;
import com.fzy.project.service.UserShareService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.fzy.project.constant.RedisConstant.BLOG_COLLECT_KEY;
import static com.fzy.project.constant.RedisConstant.BLOG_LIKE_KEY;

/**
* @author 徐小帅
* @description 针对表【blog(文章)】的数据库操作Service实现
* @createDate 2023-04-21 11:24:26
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService{

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserShareService userShareService;

    @Resource
    private UserReportService userReportService;

    /**
     * 添加文章
     * @param blogAddRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> addBlog(BlogAddRequest blogAddRequest) {
        List<String> blogImagesUrl = blogAddRequest.getBlogImages();
        Blog blog = new Blog();
        if(blogImagesUrl!=null){
            int i=0;
            List<String> imageList = blogImagesUrl.stream().map(image -> {
                String blogImages = image;
                if (i != blogImagesUrl.size() - 1) {
                    blogImages += ",";
                }
                return blogImages;
            }).collect(Collectors.toList());
            String s = StrUtil.toString(imageList);
            blog.setBlogImages(s);
        }
        BeanUtil.copyProperties(blogAddRequest,blog,"blogImages");
        Long userId = blogAddRequest.getUserId();
        if(userId==null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"用户不可为空");
        }
        boolean isSuccess = this.save(blog);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 展示文章
     * @param blogQueryRequest
     * @return
     */
    @Override
    public BaseResponse<List<BlogVO>> listBlog(BlogQueryRequest blogQueryRequest) {
        LambdaQueryWrapper<Blog> blogLQ = setQuery(blogQueryRequest);
        List<Blog> blogs = this.list(blogLQ);
        List<BlogVO> blogVOS = blogs.stream().map(blog -> {
            BlogVO blogVO = new BlogVO();
            BeanUtil.copyProperties(blog, blogVO, "blogImages");
            String blogImages = blog.getBlogImages();
            if(blogImages!=null){
            String[] split = blogImages.split(",");
            List<String> images = new ArrayList<>();
            Collections.addAll(images, split);
            blogVO.setBlogImages(images);
            }
            Long userId = blog.getUserId();
            if (userId == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
            }
            User user = userService.getById(userId);
            blogVO.setUserId(user.getId());
            blogVO.setUserName(user.getUserName());
            blogVO.setUserAvatar(user.getAvatarUrl());
            isLikedAndCollected(Long.parseLong(blogQueryRequest.getUserId()),blog.getId(),blogVO);
            return blogVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(blogVOS);
    }

    /**
     * 给博客点赞
     * @param blogLikeRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> likeBlog(BlogLikeRequest blogLikeRequest) {
        Long blogId = blogLikeRequest.getBlogId();
        if(blogId==null||blogId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"文章错误");
        }
        String userSet=BLOG_LIKE_KEY+blogId;
        Long userId = blogLikeRequest.getUserId();
        if(userId==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"用户错误");
        }
        Double score = stringRedisTemplate.opsForZSet().score(userSet, userId.toString());
        if(score!=null){
            boolean isSuccess = update().setSql("blog_likes=blog_likes-1").eq("id", blogId).update();
            if(isSuccess){
                stringRedisTemplate.opsForZSet().remove(userSet,userId.toString());
            }
        }else{
            boolean isSuccess = update().setSql("blog_likes=blog_likes+1").eq("id", blogId).update();
            if(isSuccess){
            stringRedisTemplate.opsForZSet().add(userSet,userId.toString(),System.currentTimeMillis());
            }
        }
        return ResultUtils.success(true);
    }


    /**
     * 收藏文章
     * @param blogCollectRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> collectBlog(BlogCollectRequest blogCollectRequest) {
        Long blogId = blogCollectRequest.getBlogId();
        if(blogId==null||blogId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"文章错误");
        }
        String userSet=BLOG_COLLECT_KEY+blogId;
        Long userId = blogCollectRequest.getUserId();
        if(userId==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"用户错误");
        }
        Double score = stringRedisTemplate.opsForZSet().score(userSet, userId.toString());
        LambdaQueryWrapper<UserShare> queryWrapper = setQuery(userId, blogId);
        if(score!=null){//删除
            boolean isSuccess = update().setSql("blog_collections=blog_collections-1").eq("id", blogId).update();
            if(isSuccess){
                boolean remove = userShareService.remove(queryWrapper);
                if(remove){
                    stringRedisTemplate.opsForZSet().remove(userSet,userId.toString());
                }
            }
        }else{//添加
            UserShare userShare = setConfig(userId, blogId);
            boolean isSuccess = update().setSql("blog_collections=blog_collections+1").eq("id", blogId).update();
            if(isSuccess){
                boolean save = userShareService.save(userShare);
                if(save){
                    stringRedisTemplate.opsForZSet().add(userSet,userId.toString(),System.currentTimeMillis());
                }
            }
        }
        return ResultUtils.success(true);
    }

    /**
     * 展示我的文章
     * @param id
     * @return
     */
    @Override
    public BaseResponse<List<BlogMeVO>> listMyBlog(Long id) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<Blog>().eq(Blog::getUserId, id);
        List<Blog> blogs = this.list(queryWrapper);
        List<BlogMeVO> blogVOS = blogs.stream().map(blog -> {
            BlogMeVO blogVO = new BlogMeVO();
            BeanUtil.copyProperties(blog, blogVO, "blogImages","createTime");
            String blogImages = blog.getBlogImages();
            if(blogImages!=null){
                String[] split = blogImages.split(",");
                List<String> images = new ArrayList<>();
                Collections.addAll(images, split);
                blogVO.setBlogImages(images);
            }
            blogVO.setCreateTime(DateUtil.formatTime(blog.getCreateTime()));
            User user = userService.getById(id);
            blogVO.setUserName(user.getUserName());
            blogVO.setUserAvatar(user.getAvatarUrl());
            return blogVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(blogVOS);
    }

    /**
     * 删除文章
     * @param
     * @return
     */
    @Override
    public BaseResponse<Boolean> deleBlog(Long blogId) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getId, blogId);
        boolean isSuccess = this.remove(queryWrapper);
        if (isSuccess) {
            LambdaQueryWrapper<UserShare> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(UserShare::getBlogId,blogId);
            userShareService.remove(queryWrapper2);
        }
        return ResultUtils.success(isSuccess);
    }

    /**
     * 举报评论
     * @param blogId
     * @return
     */
    @Override
    public BaseResponse<Boolean> reportBlog(Long blogId) {
        Blog blog = this.getById(blogId);
        if (blog==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"文章已经下架");
        }
        blog.setBlogStatus(2);
        boolean isSuccess = this.updateById(blog);
        if(isSuccess){ //如果举报成功,存
            UserReport userReport = new UserReport();
            userReport.setUserId(blog.getUserId());
            userReport.setBlogId(blogId);
            boolean isSave = userReportService.save(userReport);
            if(!isSave)return ResultUtils.error(ErrorCode.OPERATION_ERROR,"举报失败");
        }
        return ResultUtils.success(isSuccess);
    }


    private UserShare setConfig(Long userId,Long blogId){
        UserShare userShare = new UserShare();
        userShare.setUserId(userId);
        userShare.setBlogId(blogId);
        userShare.setBlogTitle(this.getById(blogId).getBlogTitle());
        return userShare;
    }
    private LambdaQueryWrapper<UserShare> setQuery(Long userId,Long blogId){
        LambdaQueryWrapper<UserShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserShare::getBlogId,blogId);
        queryWrapper.eq(UserShare::getUserId,userId);
        return queryWrapper;
    }

    /**
     * 判断是否点赞或者收藏过
     * @param userId
     * @param blogId
     * @param blogVO
     */
    private void isLikedAndCollected(Long userId,Long blogId,BlogVO blogVO){
        String userKey=BLOG_LIKE_KEY+blogId;
        Double score = stringRedisTemplate.opsForZSet().score(userKey, userId.toString());
        blogVO.setIsLiked(score!=null);

        String collectionKey=BLOG_COLLECT_KEY+blogId;
        Double score1 = stringRedisTemplate.opsForZSet().score(collectionKey, userId.toString());
        blogVO.setIsCollected(score1!=null);
    }

    private LambdaQueryWrapper<Blog> setQuery(BlogQueryRequest blogQueryRequest){
        LambdaQueryWrapper<Blog> blogLQ = new LambdaQueryWrapper<>();
        String title = blogQueryRequest.getBlogTitle();
        String blogType = blogQueryRequest.getBlogType();
        if(StrUtil.isNotBlank(title)){
            blogLQ.like(Blog::getBlogTitle,title);
        }
        if(StrUtil.isNotBlank(blogType)){
            blogLQ.eq(Blog::getBlogType,blogType);
        }
        blogLQ.eq(Blog::getBlogStatus,1).or().eq(Blog::getBlogStatus,2);
        return blogLQ;
    }



}




