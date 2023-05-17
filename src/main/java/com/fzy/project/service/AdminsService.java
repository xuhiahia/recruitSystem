package com.fzy.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fzy.project.common.BaseResponse;
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
import com.fzy.project.model.entity.Admins;
import com.fzy.project.model.vo.*;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【admins】的数据库操作Service
* @createDate 2023-04-16 23:28:17
*/
public interface AdminsService extends IService<Admins> {
    /**
     * 展示公司列表
     * @param page
     * @param pageSize
     * @return
     */
    public BaseResponse<Page<CompanyVO>> listCompany(Long page, Long pageSize);

    /**
     * 查看公司信息
     * @param id
     * @return
     */
    public BaseResponse<CompanyVO> getCompanyById(Long id);

    /**
     * 条件查询
     * @param companyQueryRequest
     * @return
     */
//    public BaseResponse<Page<CompanyVO>> CompanyLikeList(CompanyQueryRequest companyQueryRequest);

    /**
     * 条件查公司不分页
     * @param companyQueryRequest
     * @return
     */
    public BaseResponse<List<CompanyVO>> CompanyLikeList(CompanyQueryRequest companyQueryRequest);
    /**
     * 删除公司
     * @param companyDeleteRequest
     * @return
     */
    public BaseResponse<Boolean> deleteCompanyById(CompanyDeleteRequest companyDeleteRequest);

    /**
     * 修改公司信息
     * @param companyUpdateRequest
     * @return
     */
    public BaseResponse<Boolean> updateCompany(CompanyUpdateRequest companyUpdateRequest);

    /**
     * 展示学生
     * @param userQueryRequest
     * @return
     */
    public BaseResponse<Page<UserVO>> listUser(UserQueryRequest userQueryRequest);

    /**
     * 模糊查询用户分页
     * @param userQueryRequest
     * @return
     */
//   public BaseResponse<Page<UserVO>> userLikeList(UserQueryRequest userQueryRequest);

    /**
     * 模糊查用户不分页
     * @param userQueryRequest
     * @return
     */
    public BaseResponse<List<UserVO>> userLikeList(UserQueryRequest userQueryRequest);
    /**
     * 删除用户
     */
    public BaseResponse<Boolean> deleUser(UserDeleteRequest userDeleteRequest);

    /**
     * 查询用户
     * @param id
     * @return
     */
    public BaseResponse<UserVO> getUserById(Long id);

    /**
     * 修改用户
     * @param userUpdateRequest
     * @return
     */
    public BaseResponse<Boolean> updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 审核岗位
     * @return
     */
    public BaseResponse<List<ChiefVO>> listUnderCheck(ChiefQueryRequest chiefQueryRequest);

    /**
     * 管理员审核岗位状态
     * @param id
     * @return
     */
    public BaseResponse<Boolean> updateChiefStatus(Long id);

    /**
     * 展示被举报和待审核的文章
     * @return
     */
    public BaseResponse<List<AdminBlogVO>> listReportBlog(AdminExamineRequest adminExamineRequest);

    /**
     * 审核文章
     * @param blogCheckRequest
     * @return
     */
    public BaseResponse<Boolean> checkBlog(BlogCheckRequest blogCheckRequest);

    /**
     * 管理员查看企业项目
     * @param companyTaskQueryRequest
     * @return
     */
    public BaseResponse<List<CompanyTaskVO>> getCompanyTask(CompanyTaskQueryRequest companyTaskQueryRequest);

    /**
     * 管理员审核评论
     * @param blogCommentCheckRequest
     * @return
     */
    public BaseResponse<Boolean> checkComment(BlogCommentCheckRequest blogCommentCheckRequest);

    /**
     * 管理员查看被举报的评论
     * @param commentReportQueryRequest
     * @return
     */
    public BaseResponse<List<AdminCommentVO>> getReportComment(CommentReportQueryRequest commentReportQueryRequest);


    /**
     * 管理员添加新的用户
     * @param adminAddUserRequest
     * @return
     */
    public BaseResponse<Boolean> addNewUser(AdminAddUserRequest adminAddUserRequest);

    /**
     * 管理员添加企业
     * @param adminAddCompanyRequest
     * @return
     */
    public BaseResponse<Boolean> addNewCompany(AdminAddCompanyRequest adminAddCompanyRequest);
}
