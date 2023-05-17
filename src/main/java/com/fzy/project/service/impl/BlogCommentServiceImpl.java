package com.fzy.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.exception.BusinessException;
import com.fzy.project.model.dto.blog.BlogCommentReportRequest;
import com.fzy.project.model.dto.comment.CommentAddRequest;
import com.fzy.project.model.dto.comment.CommentDeleteRequest;
import com.fzy.project.model.entity.Blog;
import com.fzy.project.model.entity.BlogComment;
import com.fzy.project.model.entity.User;
import com.fzy.project.model.vo.BlogCommentDetailVO;
import com.fzy.project.model.vo.BlogCommentVO;
import com.fzy.project.model.vo.UserDataVO;
import com.fzy.project.service.BlogCommentService;
import com.fzy.project.mapper.BlogCommentMapper;
import com.fzy.project.service.BlogService;
import com.fzy.project.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 徐小帅
* @description 针对表【blog_comment(文章评论)】的数据库操作Service实现
* @createDate 2023-04-21 00:43:10
*/
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment>
    implements BlogCommentService{

    @Resource
    private UserService userService;

    @Resource
    private BlogCommentMapper blogCommentMapper;

    @Resource
    private BlogService blogService;

    /**
     * 添加评论
     * @param commentAddRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> addComment(CommentAddRequest commentAddRequest) {
        Blog blog = blogService.getById(commentAddRequest.getBlogId());
        if(blog==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"文章已删除不能添加评论");
        }
        Long userId = commentAddRequest.getUserId();
        if(userId==null || userId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        BlogComment blogComment = new BlogComment();
        BeanUtil.copyProperties(commentAddRequest,blogComment);
        Long answerId = commentAddRequest.getAnswerId();

        if(answerId==null){//一级评论
            boolean isSuccess = this.save(blogComment);
            return ResultUtils.success(isSuccess);
        }
        BlogComment comment = this.getById(answerId);//拿到回答的评论id
        if(comment==null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"回答的评论已删除");
        }
        if(comment.getParentId()!=0){//二级或三级评论
            blogComment.setParentId(commentAddRequest.getCommentId());
        }

        boolean isSuccess = this.save(blogComment);
        if(isSuccess){
            blogService.update().setSql("blog_comments=blog_comments+1").eq("id",commentAddRequest.getBlogId()).update();
        }
        return ResultUtils.success(isSuccess);
    }

    /**
     * 获取评论的细节
     * @param id
     * @return
     */
    @Override
    public BaseResponse<List<BlogCommentDetailVO>> getDetail(Long id) {//id是1级的评论id
        Long parentUserId = this.getById(id).getUserId();//可能有用，先留着
        //查询一级评论id下的所有评论
        LambdaQueryWrapper<BlogComment> blogCommentLQ = new LambdaQueryWrapper<>();
        blogCommentLQ.eq(BlogComment::getParentId,id);
        blogCommentLQ.eq(BlogComment::getBlogCommentStatus,0).or().eq(BlogComment::getBlogCommentStatus,1);
        blogCommentLQ.orderByDesc(BlogComment::getCreateTime);
        List<BlogComment> comments = this.list(blogCommentLQ);
        //对每个评论做提取
        List<BlogCommentDetailVO> commentDetailVOS = comments.stream().map(comment -> {
            if (comment.getBlogCommentStatus() == 2) {//被禁用的评论
                return null;
            }
            BlogCommentDetailVO detailVO = new BlogCommentDetailVO();
            Long answerId = comment.getAnswerId();
            if (answerId == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            BlogComment answerComment = this.getById(answerId);
            if (answerComment == null || answerComment.getBlogCommentStatus() == 2) {
                //如果回答的评论删除了，那我回复的也消失了
                //如果要回答的评论被禁用了，那我的回复也消失
//                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"评论已经被删除了");
                return null;
            }
            Long userAnswer = answerComment.getUserId();
            if (answerId != id) {//三级评论
                User anserUser = userService.getById(userAnswer);
                if (anserUser == null) { //回复的评论的用户消失了 那就留着评论内容 改姓名
                    detailVO.setAnswerName("用户已注销");
                } else {
                    detailVO.setAnswerName(anserUser.getUserName());
                    detailVO.setAnswerAvatar(anserUser.getAvatarUrl());
                }
            }

            //二级评论
            Long userId = comment.getUserId();
            if (userId == null) { //用户不存在
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User user = userService.getById(userId);
            if (user == null) { //如果用户注销了
                detailVO.setUserName("用户已注销");
            } else {
                detailVO.setUserName(user.getUserName());
                detailVO.setUserAvatar(user.getAvatarUrl());
            }
            detailVO.setCreateTime(DateUtil.formatTime(comment.getCreateTime()));
            detailVO.setCommentId(comment.getId());
            return detailVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(commentDetailVOS);
    }

    /**
     * 展示一级评论
     * @param id
     * @return
     */
    @Override
    public BaseResponse<List<BlogCommentVO>> listComment(Long id) {
        Blog blog = blogService.getById(id);
        if(blog==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"文章已删除不能添加评论");
        }
        LambdaQueryWrapper<BlogComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogComment::getBlogCommentStatus,0).or().eq(BlogComment::getBlogCommentStatus,1);
        queryWrapper.eq(BlogComment::getBlogId,id);
        queryWrapper.eq(BlogComment::getParentId,0);
        queryWrapper.orderByDesc(BlogComment::getCreateTime);
        List<BlogComment> list = this.list(queryWrapper);
        List<BlogCommentVO> commentVOS = list.stream().map(comment -> {
            Long count = getCount(comment.getId());
            BlogCommentVO blogCommentVO = new BlogCommentVO();
            blogCommentVO.setComments(count);
            blogCommentVO.setCommentId(comment.getId());
            blogCommentVO.setCommentContent(comment.getBlogCommentContent());
            blogCommentVO.setCreateTime(DateUtil.formatTime(comment.getCreateTime()));
            Long userId = comment.getUserId();
            if (userId == null || userId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User user = userService.getById(userId);
            if (user == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            blogCommentVO.setUserName(user.getUserName());
            blogCommentVO.setUserAvatar(user.getAvatarUrl());
            return blogCommentVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(commentVOS);
    }

    /**
     * 删除评论
     * @param commentDeleteRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> deleComment(CommentDeleteRequest commentDeleteRequest) {
        Long commentId = commentDeleteRequest.getCommentId();
        if(commentId==null|| commentId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = this.removeById(commentId);
        if(isSuccess){
        blogService.update().setSql("blog_comments=blog_comments-1").eq("id",commentDeleteRequest.getBlogId()).update();
        }
        return ResultUtils.success(isSuccess);
    }

    /**
     * 举报评论
     * @param blogCommentReportRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> reportComment(BlogCommentReportRequest blogCommentReportRequest) {
        Long commentId = blogCommentReportRequest.getCommentId();
        if(commentId==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        BlogComment comment = this.getById(commentId);
        comment.setBlogCommentStatus(1);
        boolean isSuccess = this.updateById(comment);
        return ResultUtils.success(isSuccess);
    }


    /**
     * 查询属于一个一级评论的子评论有多少
     * @param id
     * @return
     */
    private Long getCount(Long id){
        LambdaQueryWrapper<BlogComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogComment::getParentId,id);
        Long count = blogCommentMapper.selectCount(wrapper);
        return count;
    }


//    /**
//     * 给评论设置头像
//     * @param userId
//     * @param vo
//     * @return
//     */
//    private boolean setNameAndAvatar(Long userId,BlogCommentDetailVO vo){
//        User user = userService.getById(userId);
//        if(user==null){
//            return false;
//        }
//        vo.setUserAvatar(user.getAvatarUrl());
//        vo.setUserName(user.getUserName());
//        return true;
//    }
}




