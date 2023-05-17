package com.fzy.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.exception.BusinessException;
import com.fzy.project.mapper.AdminsMapper;
import com.fzy.project.mapper.CompanyMapper;
import com.fzy.project.mapper.UserMapper;
import com.fzy.project.model.dto.admin.AdminAddCompanyRequest;
import com.fzy.project.model.dto.admin.AdminAddUserRequest;
import com.fzy.project.model.dto.admin.AdminExamineRequest;
import com.fzy.project.model.dto.blog.BlogCheckRequest;
import com.fzy.project.model.dto.blog.BlogCommentCheckRequest;
import com.fzy.project.model.dto.chief.ChiefCheckRequest;
import com.fzy.project.model.dto.chief.ChiefQueryRequest;
import com.fzy.project.model.dto.comment.CommentReportQueryRequest;
import com.fzy.project.model.dto.company.CompanyDeleteRequest;
import com.fzy.project.model.dto.company.CompanyQueryRequest;
import com.fzy.project.model.dto.company.CompanyTaskQueryRequest;
import com.fzy.project.model.dto.company.CompanyUpdateRequest;
import com.fzy.project.model.dto.user.UserDeleteRequest;
import com.fzy.project.model.dto.user.UserQueryRequest;
import com.fzy.project.model.dto.user.UserUpdateRequest;
import com.fzy.project.model.entity.*;
import com.fzy.project.model.vo.*;
import com.fzy.project.service.*;
import org.apache.coyote.ContinueResponseTiming;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.fzy.project.constant.AdminConstant.ADMIN_PASS;
import static com.fzy.project.constant.CommonConstant.PAGE;
import static com.fzy.project.constant.CommonConstant.PAGE_SIZE;


/**
* @author 徐小帅
* @description 针对表【admins】的数据库操作Service实现
* @createDate 2023-04-16 23:28:17
*/
@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins>
    implements AdminsService {

    @Resource
    private BlogService blogService;
    @Resource
    private CompanyService companyService;
    @Resource
    private UserService userService;
    @Resource
    private ChiefService chiefService;

    @Resource
    private UserReportService userReportService;

    @Resource
    private InformationService informationService;

    @Resource
    private CompanyTaskService companyTaskService;

    @Resource
    private BlogCommentService blogCommentService;

    @Resource
    private NoticeService noticeService;

    @Resource NoteService noteService;

    @Resource
    private UserMapper userMapper;

    @Override
    public BaseResponse<Page<CompanyVO>> listCompany(Long page, Long pageSize) {
        //构建page基本信息
        Page<Company> pageInfo = new Page<Company>(page, pageSize);
        //构建查询条件
        LambdaQueryWrapper<Company> companyLQ = new LambdaQueryWrapper<>();
        companyLQ.orderByDesc(Company::getCreateTime);
        List<Company> list = companyService.list(companyLQ);
        //查询
        Page<Company> pageDetail = companyService.page(pageInfo, companyLQ);
        PageDTO<CompanyVO> companyVOPage = new PageDTO(pageDetail.getCurrent(), pageDetail.getSize(), pageDetail.getTotal());
        List<CompanyVO> companyVOList = list.stream().map(item -> {
            CompanyVO companyVO = new CompanyVO();
            BeanUtil.copyProperties(item, companyVO);
            companyVO.setId(item.getId());
            companyVO.setCreateTime(DateUtil.formatTime(item.getCreateTime()));
            companyVO.setCompanyStatus(item.getCompanyStatus() == 0 ? "启用" : "停用");
            companyVO.setScale(item.getScale() == 0 ? "小" : item.getScale() == 1 ? "中" : "大");
            return companyVO;
        }).collect(Collectors.toList());
        companyVOPage.setRecords(companyVOList);
        return ResultUtils.success(companyVOPage);
    }


    @Override
    public BaseResponse<CompanyVO> getCompanyById(Long id) {
        Company company = companyService.getById(id);
        if (company == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        CompanyVO companyVO = new CompanyVO();
        BeanUtils.copyProperties(company, companyVO);
        setCompanyVOConfig(company, companyVO);
        companyVO.setId(id);
        return ResultUtils.success(companyVO);
    }

    //条件查公司不分页
//    @Override
//    public BaseResponse<Page<CompanyVO>> CompanyLikeList(CompanyQueryRequest companyQueryRequest) {
//        LambdaQueryWrapper<Company> companyFilter = companyFilter(companyQueryRequest);
//        Long current=PAGE;
//        Long size=PAGE_SIZE;
//        if(companyQueryRequest!=null){
//            current=companyQueryRequest.getCurrent();
//            size=companyQueryRequest.getPageSize();
//        }
//        //构建page基本信息
//        Page<Company> pageInfo = new Page<Company>(current,size);
//        List<Company> companyListlist = companyService.list(companyFilter);
//        //查询
//        Page<Company> companyPage = companyService.page(pageInfo, companyFilter);
//        PageDTO<CompanyVO> companyVOPage  = new PageDTO(companyPage.getCurrent(),companyPage.getSize(),companyPage.getTotal());
//        List<CompanyVO> companyVOList = companyListlist.stream().map(item -> {
//            CompanyVO companyVO = new CompanyVO();
//            BeanUtil.copyProperties(item, companyVO);
//            companyVO.setKey(item.getId());
//            companyVO.setCreateTime(DateUtil.formatTime(item.getCreateTime()));
//            companyVO.setCompanyStatus(item.getCompanyStatus()==0?"启用":"停用");
//            companyVO.setScale(item.getScale()==0?"小":item.getScale()==1?"中":"大");
//            return companyVO;
//        }).collect(Collectors.toList());
//        companyVOPage.setRecords(companyVOList);
//        return ResultUtils.success(companyVOPage);
//    }

    @Override
    public BaseResponse<List<CompanyVO>> CompanyLikeList(CompanyQueryRequest companyQueryRequest) {
        LambdaQueryWrapper<Company> companyFilter = companyFilter(companyQueryRequest);
        //构建page基本信息
        List<Company> companyListlist = companyService.list(companyFilter);
        //查询
        List<CompanyVO> companyVOList = companyListlist.stream().map(item -> {
            CompanyVO companyVO = new CompanyVO();
            BeanUtil.copyProperties(item, companyVO);
            companyVO.setId(item.getId());
            companyVO.setCreateTime(DateUtil.formatTime(item.getCreateTime()));
            companyVO.setCompanyStatus(item.getCompanyStatus() == 0 ? "启用" : "停用");
            if(item.getScale()!=null){
            companyVO.setScale(item.getScale() == 0 ? "小" : item.getScale() == 1 ? "中" : "大");
            }
            return companyVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(companyVOList);
    }


    public LambdaQueryWrapper<Company> companyFilter(CompanyQueryRequest companyReq) {
        LambdaQueryWrapper<Company> companyLQ = new LambdaQueryWrapper<>();
        companyLQ.like(StrUtil.isNotBlank(companyReq.getCompanyName()), Company::getCompanyName, companyReq.getCompanyName());
        companyLQ.eq(StrUtil.isNotBlank(companyReq.getCompanyAccount()), Company::getCompanyAccount, companyReq.getCompanyAccount());
        String scaleStr = companyReq.getScale();
        if (StrUtil.isNotBlank(scaleStr)) {
            Integer scale = "小".equals(scaleStr) ? 0 : "中".equals(scaleStr) ? 1 : 2;
            companyLQ.eq(Company::getScale, scale);
        }
        companyLQ.eq(StrUtil.isNotBlank(companyReq.getCompanyIndustry()), Company::getCompanyIndustry, companyReq.getCompanyIndustry());
        companyLQ.like(StrUtil.isNotBlank(companyReq.getAddress()), Company::getAddress, companyReq.getAddress());
        String companyStatus = companyReq.getCompanyStatus();
        if (StrUtil.isNotBlank(companyStatus)) {
            Integer status = "启用".equals(companyStatus) ? 0 : 1;
            companyLQ.eq(Company::getCompanyStatus, status);
        }
        return companyLQ;
    }

    public void setCompanyVOConfig(Company company, CompanyVO companyVO) {
        companyVO.setScale(company.getScale() == 0 ? "小" : company.getScale() == 1 ? "中" : "大");
        companyVO.setCreateTime(DateUtil.formatTime(company.getCreateTime()));
    }

    @Override
    public BaseResponse<Boolean> deleteCompanyById(CompanyDeleteRequest companyDeleteRequest) {
        List<Long> ids = companyDeleteRequest.getIds();
        if (ids == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Boolean isSuccess = companyService.removeBatchByIds(ids);
        return ResultUtils.success(isSuccess);
    }

    @Override
    public BaseResponse<Boolean> updateCompany(CompanyUpdateRequest companyUpdateRequest) {
        Company company = companyService.getById(companyUpdateRequest.getId());
        if (company == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        BeanUtils.copyProperties(companyUpdateRequest, company);
        //scale
        String scale = companyUpdateRequest.getScale();
        if (scale.equals("小")) {
            company.setScale(0);
        } else if (scale.equals("中")) {
            company.setScale(1);
        } else {
            company.setScale(2);
        }
        //status
        company.setCompanyStatus("启用".equals(companyUpdateRequest.getCompanyStatus()) ? 0 : 1);
        boolean isSuccess = companyService.updateById(company);
        return ResultUtils.success(isSuccess);
    }

    @Override
    public BaseResponse<Page<UserVO>> listUser(UserQueryRequest userQueryRequest) {
        long current = PAGE;
        long size = PAGE_SIZE;
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setGender(user.getGender() == 1 ? "男" : "女");
            userVO.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
            userVO.setId(user.getId());
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }
//分页模糊找用户
//    @Override
//    public BaseResponse<Page<UserVO>> userLikeList(UserQueryRequest userQueryRequest) {
//        if(userQueryRequest==null){
//            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
//        }
//        LambdaQueryWrapper<User> userLQ = new LambdaQueryWrapper<>();
//        userLQ.like(StrUtil.isNotBlank(userQueryRequest.getUserName()),User::getUserName,userQueryRequest.getUserName());
//        userLQ.eq(StrUtil.isNotBlank(userQueryRequest.getGender()),User::getGender,"男".equals(userQueryRequest.getGender())?1:0);
//        if(StrUtil.isNotBlank(userQueryRequest.getAge())){
//            userLQ.eq(User::getAge,Integer.parseInt(userQueryRequest.getAge()));
//        }
//        userLQ.eq(StrUtil.isNotBlank(userQueryRequest.getPhone()),User::getPhone,userQueryRequest.getPhone());
//        userLQ.orderByDesc(User::getCreateTime);
//        long current =PAGE;
//        long size = PAGE_SIZE;
//        if (userQueryRequest != null) {
//            current = userQueryRequest.getCurrent();
//            size = userQueryRequest.getPageSize();
//        }
//        Page<User> userPageInfo = new Page<>(current,size);
//        Page<User> userPage = userService.page(userPageInfo,userLQ);
//        PageDTO<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(),userPage.getTotal());
//        List<User> users = userPage.getRecords();
//        List<UserVO> userVOList = users.stream().map(user -> {
//            UserVO userVO = new UserVO();
//            BeanUtils.copyProperties(user, userVO);
//            userVO.setGender(user.getGender() == 1 ? "男" : "女");
//            userVO.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
//            userVO.setKey(user.getId());
//            return userVO;
//        }).collect(Collectors.toList());
//        userVOPage.setRecords(userVOList);
//        return ResultUtils.success(userVOPage);
//    }

    //不分页模糊找用户
    public BaseResponse<List<UserVO>> userLikeList(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        LambdaQueryWrapper<User> userLQ = new LambdaQueryWrapper<>();
        userLQ.like(StrUtil.isNotBlank(userQueryRequest.getUserName()), User::getUserName, userQueryRequest.getUserName());
        userLQ.eq(StrUtil.isNotBlank(userQueryRequest.getGender()), User::getGender, "男".equals(userQueryRequest.getGender()) ? 1 : 0);
        if (StrUtil.isNotBlank(userQueryRequest.getAge())) {
            userLQ.eq(User::getAge, Integer.parseInt(userQueryRequest.getAge()));
        }
        userLQ.eq(StrUtil.isNotBlank(userQueryRequest.getPhone()), User::getPhone, userQueryRequest.getPhone());
        userLQ.orderByDesc(User::getCreateTime);
        List<User> users = userService.list(userLQ);
        List<UserVO> userVOList = users.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            Integer gender = user.getGender();
            if(gender!=null){
                userVO.setGender(gender == 1 ? "男" : "女");
            }
            userVO.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
            userVO.setId(user.getId());
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }

    @Override
    public BaseResponse<Boolean> deleUser(UserDeleteRequest userDeleteRequest) {
        List<Long> ids = userDeleteRequest.getIds();
        if (ids == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Boolean isSuccess = userService.removeBatchByIds(ids);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse<UserVO> getUserById(Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        userVO.setCreateTime(DateUtil.formatTime(new Date()));
        userVO.setGender(user.getGender() == 1 ? "男" : "女");
        return ResultUtils.success(userVO);
    }

    /**
     * 修改用户信息
     *
     * @param userUpdateRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> updateUser(UserUpdateRequest userUpdateRequest) {
        Long id = userUpdateRequest.getId();
        if (id == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean isSuccess = userService.updateById(user);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 管理员审核岗位
     *
     * @param chiefQueryRequest
     * @return
     */
    @Override
    public BaseResponse<List<ChiefVO>> listUnderCheck(ChiefQueryRequest chiefQueryRequest) {
        LambdaQueryWrapper<Chief> chiefLQ = setCheckQuery(chiefQueryRequest);
        List<Chief> chiefs = chiefService.list(chiefLQ);
        List<ChiefVO> checkVOS = chiefs.stream().map(chief -> {
            ChiefVO chiefVO = new ChiefVO();
            BeanUtil.copyProperties(chief, chiefVO, "chiefStatus");
            Integer status = chief.getChiefStatus();
            chiefVO.setChiefStatus(status == 1 ? "通过" : "审核");
            Long companyId = chief.getCompanyId();
            Company company = companyService.getById(companyId);
            if (company == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公司不存在");
            }
            chiefVO.setCompanyName(company.getCompanyName());
            chiefVO.setCompanyIndustry(company.getCompanyIndustry());
            chiefVO.setCompanyAddress(company.getAddress());
            return chiefVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(checkVOS);
    }

    @Override
    public BaseResponse<Boolean> updateChiefStatus(Long id) {
        Chief chief = chiefService.getById(id);
        if (chief == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "岗位不存在");
        }
        chief.setChiefStatus(ADMIN_PASS);
        boolean isSuccess = chiefService.updateById(chief);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 展示被举报和待审核的文章
     *
     * @return
     */
    @Override
    public BaseResponse<List<AdminBlogVO>> listReportBlog(AdminExamineRequest adminExamineRequest) {
        LambdaQueryWrapper<Blog> queryWrapper = setQuery(adminExamineRequest);

        List<Blog> blogs = blogService.list(queryWrapper);
        List<AdminBlogVO> adminBlogVOS = blogs.stream().map(blog -> {
            AdminBlogVO adminBlogVO = new AdminBlogVO();
            BeanUtil.copyProperties(blog, adminBlogVO, "blogStatus");
            String status = blog.getBlogStatus() == 0 ? "审核" : "举报";
            adminBlogVO.setBlogStatus(status);
            User user = userService.getById(blog.getUserId());
            if (user == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户已经注销");
            }
            adminBlogVO.setUserName(user.getUserName());
            adminBlogVO.setUserAvatar(user.getAvatarUrl());
            return adminBlogVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(adminBlogVOS);
    }

    private LambdaQueryWrapper<Blog> setQuery(AdminExamineRequest adminExamineRequest){
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        String blogTitle = adminExamineRequest.getBlogTitle();
        String blogType = adminExamineRequest.getBlogType();
        queryWrapper.eq(Blog::getBlogStatus,0);
        if(StrUtil.isNotBlank(blogTitle)){
            queryWrapper.like(Blog::getBlogTitle,blogTitle);
        }
        if(StrUtil.isNotBlank(blogType)){
            queryWrapper.eq(Blog::getBlogType,blogType);
        }
        queryWrapper.or().eq(Blog::getBlogStatus,2);
        if(StrUtil.isNotBlank(blogTitle)){
            queryWrapper.like(Blog::getBlogTitle,blogTitle);
        }
        if(StrUtil.isNotBlank(blogType)){
            queryWrapper.eq(Blog::getBlogType,blogType);
        }
        return queryWrapper;
    }
    /**
     * 审核文章
     *
     * @param blogCheckRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> checkBlog(BlogCheckRequest blogCheckRequest) {
        Long blogId = blogCheckRequest.getBlogId();
        if (blogId == null || blogId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Blog blog = blogService.getById(blogId);
        String choice = blogCheckRequest.getChoice();
        if (choice == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Integer status = "通过".equals(choice) ? 1 : 3;
        blog.setBlogStatus(status);
        boolean isSuccess = blogService.updateById(blog);
        if (isSuccess) { //表示修改状态成功，接下来看是什么状态
            List<UserReport> users = userReportService.list(new QueryWrapper<UserReport>().eq("blog_id", blogId));
            if(users==null){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            if (status == 3) {//举报通过，给被举报的举报的人都发
                if (users.size()!=0) {//是有人举报，但是成功
                    List<Information> ReportMessage = users.stream().map(user -> {
                        Information information = new Information();
                        information.setInformationContent("您举报的文章" + blog.getBlogTitle() + "已经通过审核，该文章已下架，感谢您的监督");
                        information.setInformationTitle("举报成功");
                        information.setUserId(user.getUserId());
                        return information;
                    }).collect(Collectors.toList());
                    Information information = new Information();
                    information.setUserId(blog.getUserId());
                    information.setInformationTitle("举报提醒");
                    information.setInformationContent("您的文章" + blog.getBlogTitle() + "因内容不适被举报，经审核后进行下架处理");
                    ReportMessage.add(information);
                    boolean isSave = informationService.saveBatch(ReportMessage);
                    if (!isSave) throw new BusinessException(ErrorCode.OPERATION_ERROR, "请求失败");
                } else if (users.size()==0) {//是审核不通过
                    Information information = new Information();
                    information.setUserId(blog.getUserId());
                    information.setInformationTitle("审核失败");
                    information.setInformationContent("您发表的文章" + blog.getBlogTitle() + "因内容不适，经审核后未能通过");
                    boolean isSave = informationService.save(information);
                    if (!isSave) throw new BusinessException(ErrorCode.OPERATION_ERROR, "请求失败");
                }
            } else {
                if (users.size()!=0) {//是有人举报，但是不成功
                    List<Information> ReportMessage = users.stream().map(user -> {
                        Information information = new Information();
                        information.setInformationContent("您举报的文章" + blog.getBlogTitle() + "审核后无发现异常，感谢您的监督");
                        information.setInformationTitle("举报失败");
                        information.setUserId(user.getUserId());
                        return information;
                    }).collect(Collectors.toList());
                    boolean isSave = informationService.saveBatch(ReportMessage);
                    if (!isSave) throw new BusinessException(ErrorCode.OPERATION_ERROR, "请求失败");
                } else if (users.size()==0) {//是通过审核
                    Information information = new Information();
                    information.setInformationContent("审核通过");
                    information.setInformationTitle("您发表的文章" + blog.getBlogTitle() + "已经通过审核");
                    information.setUserId(blog.getUserId());
                    boolean isSave = informationService.save(information);
                    if(!isSave)throw new BusinessException(ErrorCode.OPERATION_ERROR,"请求失败");
                }
            }
        }
        return ResultUtils.success(isSuccess);
    }

    /**
     * 管理员查看企业项目
     * @param companyTaskQueryRequest
     * @return
     */
    @Override
    public BaseResponse<List<CompanyTaskVO>> getCompanyTask(CompanyTaskQueryRequest companyTaskQueryRequest) {
        LambdaQueryWrapper<CompanyTask> queryWrapper = setQuery(companyTaskQueryRequest);
        List<CompanyTask> companyTasks = companyTaskService.list(queryWrapper);
        List<CompanyTaskVO> taskVOS = companyTasks.stream().map(task -> {
            CompanyTaskVO companyTaskVO = new CompanyTaskVO();
            Company company = companyService.getById(task.getCompanyId());
            if(company==null){
                companyTaskVO.setCompanyName("公司已经注销了");
                return companyTaskVO;
            }
            companyTaskVO.setCompanyAvatar(company.getCompanyAvatar());
            companyTaskVO.setCompanyName(company.getCompanyName());
            companyTaskVO.setCompanyId(task.getCompanyId());
            BeanUtil.copyProperties(task, companyTaskVO, "taskStatus");
//            String status = task.getTaskStatus() == 0 ? "发布" : "停用";
//            companyTaskVO.setTaskStatus(status);
            return companyTaskVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(taskVOS);

    }

    /**
     * 管理员审核评论
     * @param blogCommentCheckRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> checkComment(BlogCommentCheckRequest blogCommentCheckRequest) {
        Long commentId = blogCommentCheckRequest.getCommentId();
        if(commentId==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String choice = blogCommentCheckRequest.getChoice();
        Integer status="通过".equals(choice)?0:2;
        BlogComment comment = blogCommentService.getById(commentId);
        comment.setBlogCommentStatus(status);
        boolean isSuccess = blogCommentService.updateById(comment); //审核已经结束，接下来进行通知
        Information information = new Information();
        if(isSuccess) {
            if (status == 2) { //禁用评论，给评论的人发
                Long userId = comment.getUserId();
                User user = userService.getById(userId);
                if (user != null) { //不为空给他发，为空说明可能注销了，就不发了
                    Blog blog = blogService.getById(comment.getBlogId());
                    if(blog==null){ //文章删了就不用发了
                        return ResultUtils.success(isSuccess);
                    }
                    information.setInformationContent("您在： " + blog.getBlogTitle() + "  中的评论: " + comment.getBlogCommentContent() + "因内容不当，经审核后进行禁用处理");
                    information.setInformationTitle("评论被举报通知");
                    informationService.save(information);
                }
            }
        }
        return ResultUtils.success(isSuccess);
    }

    /**
     * 管理员查看被举报的评论
     * @param commentReportQueryRequest
     * @return
     */
    @Override
    public BaseResponse<List<AdminCommentVO>> getReportComment(CommentReportQueryRequest commentReportQueryRequest) {
        String blogTitle = commentReportQueryRequest.getBlogTitle();
        if(blogTitle!=null) { //有查询
            List<Blog> blogs = blogService.list(new QueryWrapper<Blog>().like("blog_title", blogTitle));
            ArrayList<AdminCommentVO> adminCommentVOS = new ArrayList<>();
            for (Blog blog : blogs) {
                //拿到被举报的评论
                List<BlogComment> comments = blogCommentService.list(new QueryWrapper<BlogComment>().eq("blog_id", blog.getId()).eq("blog_comment_status", 1));
                List<AdminCommentVO> commentVOS = comments.stream().map((comment -> {
                    AdminCommentVO adminCommentVO = new AdminCommentVO();
                    adminCommentVO.setCreateTime(DateUtil.formatTime(blog.getCreateTime()));
                    adminCommentVO.setCommentContent(comment.getBlogCommentContent());
                    adminCommentVO.setCommentId(comment.getId());
                    Long userId = comment.getUserId();
                    User user = userService.getById(userId);
                    if (user == null) {
                        adminCommentVO.setUserName("用户已注销");
                        return adminCommentVO;
                    }
                    adminCommentVO.setUserName(user.getUserName());
                    adminCommentVO.setUserAvatar(user.getAvatarUrl());
                    return adminCommentVO;
                })).collect(Collectors.toList());
                adminCommentVOS.addAll(commentVOS);
            }
            return ResultUtils.success(adminCommentVOS);
        }else{ //没查询
            List<BlogComment> blogComments = blogCommentService.list(new QueryWrapper<BlogComment>().eq("blog_comment_status", 1));
            List<AdminCommentVO> commentVOS = blogComments.stream().map(comment -> {
                AdminCommentVO adminCommentVO = new AdminCommentVO();
                Blog blog = blogService.getById(comment.getBlogId());
                if(blog==null){
                    adminCommentVO.setBlogTitle("文章已删除");
                    adminCommentVO.setCommentId(comment.getId());
                    Long userId = comment.getUserId();
                    User user = userService.getById(userId);
                    if (user == null) {
                        adminCommentVO.setUserName("用户已注销");
                        return adminCommentVO;
                    }
                    adminCommentVO.setUserName(user.getUserName());
                    adminCommentVO.setUserAvatar(user.getAvatarUrl());
                    return adminCommentVO;
                }
                adminCommentVO.setCreateTime(DateUtil.formatTime(blog.getCreateTime()));
                adminCommentVO.setCommentContent(comment.getBlogCommentContent());
                adminCommentVO.setCommentId(comment.getId());
                Long userId = comment.getUserId();
                User user = userService.getById(userId);
                if (user == null) {
                    adminCommentVO.setUserName("用户已注销");
                    return adminCommentVO;
                }
                adminCommentVO.setUserName(user.getUserName());
                adminCommentVO.setUserAvatar(user.getAvatarUrl());
                adminCommentVO.setBlogTitle(blog.getBlogTitle());
                return adminCommentVO;
            }).collect(Collectors.toList());
            return ResultUtils.success(commentVOS);
        }

    }

    /**
     * 管理员添加用户
     * @param adminAddUserRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> addNewUser(AdminAddUserRequest adminAddUserRequest) {
        User user = new User();
        String userPassword = adminAddUserRequest.getUserPassword();
        String userCheckPassword = adminAddUserRequest.getCheckPassword();
        String userName = adminAddUserRequest.getUserName();
        String userPhone = adminAddUserRequest.getUserPhone();

        if(StrUtil.isBlank(userName)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不能为空");
        }
        if(StrUtil.isBlank(userPassword)||StrUtil.isBlank(userCheckPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
        }
        if (userPhone.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || userCheckPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if(!userPassword.equals(userCheckPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验密码与密码不一致");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", userPhone);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex(("fzy" + userPassword).getBytes());
        // 3. 插入数据
        //11111111111111111111111111111111111111111111111111111111
        user.setUserName(userName);
        user.setPhone(userPhone);
        user.setUserPwd(encryptPassword);
        boolean saveResult = userService.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        //创建简历
        Note note = new Note();
        note.setUserId(user.getId());
        noteService.save(note);
        //创建系统消息
        Information information = new Information();
        information.setInformationTitle("注册成功");
        information.setInformationContent("欢迎您使用该系统，祝您前程似锦");
        information.setUserId(user.getId());
        informationService.save(information);
        return ResultUtils.success(saveResult);
    }

    @Override
    public BaseResponse<Boolean> addNewCompany(AdminAddCompanyRequest adminAddCompanyRequest) {
        String companyPassword = adminAddCompanyRequest.getCompanyPassword();
        String checkPassword = adminAddCompanyRequest.getCheckPassword();
        String companyName = adminAddCompanyRequest.getCompanyName();
        String companyPhone = adminAddCompanyRequest.getCompanyPhone();
        if(!companyPassword.equals(checkPassword)){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"密码不一致");
        }
        if (companyPhone.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if (companyPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }
        Company company = new Company();
        String encryptPassword = DigestUtils.md5DigestAsHex(("fzy" + companyPassword).getBytes());

        company.setCompanyAccount(companyPhone);
        LambdaQueryWrapper<Company> companyLQ = new LambdaQueryWrapper<>();
        companyLQ.eq(Company::getCompanyAccount,companyPhone);
        Company one = companyService.getOne(companyLQ);
        if(one!=null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"已经存在账号");
        }
        company.setCompanyPwd(encryptPassword);
        company.setCompanyName(companyName);
        boolean isSuccess = companyService.save(company);
        return ResultUtils.success(isSuccess);
    }

    private LambdaQueryWrapper<CompanyTask> setQuery(CompanyTaskQueryRequest companyTaskQueryRequest){
        String taskTitle = companyTaskQueryRequest.getTaskTitle();
        String taskType = companyTaskQueryRequest.getTaskType();
        String companyIdStr = companyTaskQueryRequest.getCompanyId();
        LambdaQueryWrapper<CompanyTask> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(companyIdStr)) {
            Long companyId = Long.parseLong(companyIdStr);
            queryWrapper.eq(companyId!=null,CompanyTask::getCompanyId,companyId);
        }

        if(!"全部".equals(taskType)) {
            queryWrapper.eq(!taskType.equals(""), CompanyTask::getTaskType, taskType);
        }
        queryWrapper.like(StrUtil.isNotBlank(taskTitle),CompanyTask::getTaskTitle,taskTitle);
        return queryWrapper;

    }
    private LambdaQueryWrapper<Chief> setCheckQuery (ChiefQueryRequest chiefQueryRequest){
            LambdaQueryWrapper<Chief> chiefLQ = new LambdaQueryWrapper<>();
            String chiefName = chiefQueryRequest.getChiefName();
            String chiefStatus = chiefQueryRequest.getChiefStatus();
            if (StrUtil.isNotBlank(chiefName)) {
                chiefLQ.like(Chief::getChiefName, chiefName);
            }
            if (StrUtil.isNotBlank(chiefStatus)) {
                Integer status = "审核".equals(chiefStatus) ? 0 : 1;
                chiefLQ.eq(Chief::getChiefStatus, status);
            }
            return chiefLQ;
        }



}





