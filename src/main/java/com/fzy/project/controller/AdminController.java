package com.fzy.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.model.dto.admin.AdminAddCompanyRequest;
import com.fzy.project.model.dto.admin.AdminAddUserRequest;
import com.fzy.project.model.dto.admin.AdminExamineRequest;
import com.fzy.project.model.dto.blog.BlogCheckRequest;
import com.fzy.project.model.dto.blog.BlogCommentCheckRequest;
import com.fzy.project.model.dto.chief.ChiefQueryRequest;
import com.fzy.project.model.dto.chief.ChiefStatusUpdateRequest;
import com.fzy.project.model.dto.comment.CommentReportQueryRequest;
import com.fzy.project.model.dto.company.CompanyDeleteRequest;
import com.fzy.project.model.dto.company.CompanyQueryRequest;
import com.fzy.project.model.dto.company.CompanyTaskQueryRequest;
import com.fzy.project.model.dto.company.CompanyUpdateRequest;
import com.fzy.project.model.dto.user.UserDeleteRequest;
import com.fzy.project.model.dto.user.UserQueryRequest;
import com.fzy.project.model.dto.user.UserUpdateRequest;
import com.fzy.project.model.vo.*;
import com.fzy.project.service.AdminsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.fzy.project.constant.CommonConstant.PAGE;
import static com.fzy.project.constant.CommonConstant.PAGE_SIZE;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminsService adminsService;

    /**
     * 展示公司列表
     * @param
     * @param
     * @return
     */
    @GetMapping("/company")
    public BaseResponse<Page<CompanyVO>> listCompany(CompanyQueryRequest companyQueryRequest){
        if(companyQueryRequest!=null){
            Long page=companyQueryRequest.getCurrent();
            Long pageSize=companyQueryRequest.getPageSize();
            return adminsService.listCompany(page,pageSize);
        }
        return adminsService.listCompany(PAGE,PAGE_SIZE);
    }


    /**
     * 根据id获取公司信息
     * @param id
     * @return
     */
    @GetMapping("/company/{id}")
    public BaseResponse<CompanyVO> getCompany(@PathVariable("id") Long id){
        if(id==null||id<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
         return adminsService.getCompanyById(id);
    }

    /**
     * 根据id删除公司
     * @param companyDeleteRequest
     * @return
     */
    @DeleteMapping("/company")
    public BaseResponse<Boolean> delCompany(@RequestBody CompanyDeleteRequest companyDeleteRequest){
        if(companyDeleteRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.deleteCompanyById(companyDeleteRequest);
    }

    /**
     * 修改公司信息
     * @param companyUpdateRequest
     * @return
     */
    @PostMapping("/company")
    public BaseResponse<Boolean> updateCompany(@RequestBody CompanyUpdateRequest companyUpdateRequest){
        if(companyUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.updateCompany(companyUpdateRequest);
    }

    /**
     * 条件筛选公司
     * @param companyQueryRequest
     * @return
     */
//    @GetMapping("/company/like")
//    public BaseResponse<Page<CompanyVO>> companyLikeList(CompanyQueryRequest companyQueryRequest){
//        if(companyQueryRequest==null){
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
//        }
//        return adminsService.CompanyLikeList(companyQueryRequest);
//    }

    /**
     * 条件查公司不分页
     * @param companyQueryRequest
     * @return
     */
    @GetMapping("/company/like")
    public BaseResponse<List<CompanyVO>> companyLikeList(CompanyQueryRequest companyQueryRequest){
        if(companyQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.CompanyLikeList(companyQueryRequest);
    }
    /**
     * 展示用户
     * @return
     */
    @GetMapping("/user")
    public BaseResponse<Page<UserVO>> listUser(UserQueryRequest userQueryRequest){
        if(userQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        return  adminsService.listUser(userQueryRequest);
    }

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public BaseResponse<UserVO> getUserInfo(@PathVariable("id") Long id){
        if(id<=0 || id==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.getUserById(id);
    }

    /**
     * 修改用户信息
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/user")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        if(userUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.updateUser(userUpdateRequest);
    }

    /**
     * 条件查询用户
     * @param userQueryRequest
     * @return
     */
//    @GetMapping("/user/like")
//    public BaseResponse<Page<UserVO>> userLikeList(UserQueryRequest userQueryRequest){
//        if(userQueryRequest==null){
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"参数为空");
//        }
//        return adminsService.userLikeList(userQueryRequest);
//    }

    /**
     * 条件查用户不分页
     * @param userQueryRequest
     * @return
     */
    @GetMapping("/user/like")
    public BaseResponse<List<UserVO>> userLikeList(UserQueryRequest userQueryRequest){
        if(userQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        return adminsService.userLikeList(userQueryRequest);
    }

    /**
     * 删除用户
     * @param userDeleteRequest
     * @return
     */
    @DeleteMapping("/user")
    public BaseResponse<Boolean> deleUser(@RequestBody UserDeleteRequest userDeleteRequest){
        if(userDeleteRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"数据为空");
        }
        return adminsService.deleUser(userDeleteRequest);
    }

    /**
     * 审核岗位
     * @param
     * @return
     */
    @PostMapping("/check/chief")
    public BaseResponse<Boolean> checkChief(@RequestBody ChiefStatusUpdateRequest chiefStatusUpdateRequest){
        if(chiefStatusUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long id = chiefStatusUpdateRequest.getId();
        return adminsService.updateChiefStatus(id);
    }

    /**
     * 查看审核的岗位
     * @return
     */
    @GetMapping("/check/chief")
    public BaseResponse<List<ChiefVO>> listUnderCheck(ChiefQueryRequest chiefQueryRequest){
        if(chiefQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.listUnderCheck(chiefQueryRequest);
    }

    /**
     * 查看被举报和待审核的文章
     * @return
     */
    @GetMapping("/blog/report")
    public BaseResponse<List<AdminBlogVO>> listReportBlog(AdminExamineRequest adminExamineRequest){
        if(adminExamineRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.listReportBlog(adminExamineRequest);
    }

    /**
     * 审核文章
     * @param blogCheckRequest
     * @return
     */
    @PostMapping("/check/blog")
    public BaseResponse<Boolean> updateBlog(@RequestBody BlogCheckRequest blogCheckRequest){
        if(blogCheckRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.checkBlog(blogCheckRequest);
    }

    /**
     * 管理员查询企业项目
     * @param companyTaskQueryRequest
     * @return
     */
    @GetMapping("/companyTask")
    public BaseResponse<List<CompanyTaskVO>> adminGetTask(CompanyTaskQueryRequest companyTaskQueryRequest){
        if(companyTaskQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.getCompanyTask(companyTaskQueryRequest);
    }

    /**
     * 管理员展示被举报的评论
     * @param commentReportQueryRequest
     * @return
     */
    @GetMapping("/reportComment")
    public BaseResponse<List<AdminCommentVO>> getReportComment(CommentReportQueryRequest commentReportQueryRequest){
        if(commentReportQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.getReportComment(commentReportQueryRequest);
    }

    /**
     * 管理员审核评论
     * @param blogCommentCheckRequest
     * @return
     */
    @PostMapping("/blogComment")
    public BaseResponse<Boolean> checkComment(@RequestBody BlogCommentCheckRequest blogCommentCheckRequest){
        if(blogCommentCheckRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.checkComment(blogCommentCheckRequest);
    }

    /**
     * 管理员添加用户
     * @param adminAddUserRequest
     * @return
     */
    @PutMapping("/add/user")
    public BaseResponse<Boolean> addUser(@RequestBody AdminAddUserRequest adminAddUserRequest){
        if(adminAddUserRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.addNewUser(adminAddUserRequest);
    }

    /**
     * 管理员添加企业
     */
    @PutMapping("/add/company")
    public BaseResponse<Boolean> addCompany(@RequestBody AdminAddCompanyRequest adminAddCompanyRequest){
        if(adminAddCompanyRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return adminsService.addNewCompany(adminAddCompanyRequest);
    }

}
