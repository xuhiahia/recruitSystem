package com.fzy.project.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.model.dto.chief.ChiefAddRequest;
import com.fzy.project.model.dto.chief.ChiefDeleteRequest;
import com.fzy.project.model.dto.chief.ChiefQueryRequest;
import com.fzy.project.model.dto.chief.ChiefUpdateRequest;
import com.fzy.project.model.dto.company.*;
import com.fzy.project.model.dto.note.NotePassRequest;
import com.fzy.project.model.dto.note.NoteQueryRequest;
import com.fzy.project.model.vo.*;
import com.fzy.project.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 添加招聘岗位
     * @param chiefAddRequest
     * @return
     */
    @PutMapping("/add/chief")
    public BaseResponse<Boolean> addChief(@RequestBody ChiefAddRequest chiefAddRequest){
        if(chiefAddRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long companyId = chiefAddRequest.getCompanyId();
        if(companyId==null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"公司不存在");
        }
        return companyService.addChief(chiefAddRequest);
    }

    /**
     * 展示招聘信息
     * @return
     */
    @GetMapping("/list/chief")
    public BaseResponse<List<ChiefVO>> listChief(ChiefQueryRequest chiefQueryRequest){
        return companyService.listChief(chiefQueryRequest);
    }

    /**
     * 删除招聘信息
     * @param chiefDeleteRequest
     * @return
     */
    @DeleteMapping("/dele/chief")
    public BaseResponse<Boolean> deleChief(@RequestBody ChiefDeleteRequest chiefDeleteRequest){
        if(chiefDeleteRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.deleChief(chiefDeleteRequest);
    }

    /**
     * 根据id查岗位信息
     * @param id
     * @return
     */
    @GetMapping("/chief/{id}")
    public BaseResponse<ChiefVO> getChief(@PathVariable("id") Long id){
        if(id==null||id<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.getChiefById(id);
    }
    /**
     * 修改招聘信息
     * @param chiefUpdateRequest
     * @return
     */
    @PostMapping("/update/chief")
    public BaseResponse<Boolean> updateChief(@RequestBody ChiefUpdateRequest chiefUpdateRequest){
        if(chiefUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.updateChief(chiefUpdateRequest);
    }

    /**
     * 查看投递简历人
     * @param noteQueryRequest
     * @return
     */
    @GetMapping("/note")
    public BaseResponse<List<NoteVO>> listNote(NoteQueryRequest noteQueryRequest){
        if(noteQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.listNote(noteQueryRequest);
    }

    /**
     * 查看简历
     * @param noteId
     * @return
     */
   @GetMapping("/note/detail")
    public BaseResponse<NoteDetailVO> detailNote(Long noteId){
        if(noteId==null||noteId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.noteDetail(noteId);
   }

    /**
     * 改变简历状态
     * @param notePassRequest
     * @return
     */
   @PostMapping("/note")
    public BaseResponse<Boolean> PassNote(@RequestBody NotePassRequest notePassRequest){
       if(notePassRequest==null){
           return ResultUtils.error(ErrorCode.PARAMS_ERROR);
       }
       return companyService.passNote(notePassRequest);
   }

    /**
     * 注册公司
     * @param companyRegisterRequest
     * @return
     */
   @PostMapping("/register")
    public BaseResponse<CompanyVO> registerCompany(@RequestBody CompanyRegisterRequest companyRegisterRequest){
       if(companyRegisterRequest==null){
           return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数为空");
       }
       String companyPwd = companyRegisterRequest.getCompanyPwd();
       String companyCheckPwd = companyRegisterRequest.getCompanyCheckPwd();
       if(companyCheckPwd==null||companyPwd==null){
           return ResultUtils.error(ErrorCode.PARAMS_ERROR);
       }
       return companyService.registerCompany(companyRegisterRequest);
   }

    /**
     * 企业修改个人信息
     * @param companyUpdateDataRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateCompany(@RequestBody CompanyUpdateDataRequest companyUpdateDataRequest){
        if(companyUpdateDataRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.updateDataCompany(companyUpdateDataRequest);
    }

    /**
     * 企业查看自己的信息
     * @param id
     * @return
     */
    @GetMapping
    public BaseResponse<CompanyDataVO> getCompanyData(String id){
        if(id==null||id.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long ids = Long.parseLong(id);
        return companyService.getCompanyById(ids);
    }

    /**
     * 添加公司项目
     * @param companyAddTaskRequest
     * @return
     */
    @PutMapping("/add/task")
    public BaseResponse<Boolean> addTask(@RequestBody CompanyAddTaskRequest companyAddTaskRequest ){
        if(companyAddTaskRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.addTask(companyAddTaskRequest);
    }

    /**
     * 公司查看自己的项目
     * @param companyTaskQueryRequest
     * @return
     */
    @GetMapping("/task")
    public BaseResponse<List<CompanyTaskVO>> listTask(CompanyTaskQueryRequest companyTaskQueryRequest){
        if(companyTaskQueryRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.listTask(companyTaskQueryRequest);
    }

    /**
     * 修改项目信息
     * @param companyTaskUpdateRequest
     * @return
     */
    @PostMapping("/task/update")
    public BaseResponse<Boolean> updateTask(@RequestBody CompanyTaskUpdateRequest companyTaskUpdateRequest){
        if(companyTaskUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.updateTask(companyTaskUpdateRequest);
    }

    /**
     * 删除企业发布的任务
     * @param companyTaskDeleteRequest
     * @return
     */
    @DeleteMapping("/task")
    public BaseResponse<Boolean> deleTask(@RequestBody CompanyTaskDeleteRequest companyTaskDeleteRequest){
        if (companyTaskDeleteRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.deleTask(companyTaskDeleteRequest);
    }

    /**
     * 获取任务的具体信息
     * @param id
     * @return
     */
    @GetMapping("/taskInfo")
    public BaseResponse<CompanyTaskVO> getTaskInfo(String id){//taskId
        if(id==null||id.length()<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long taskId = Long.parseLong(id);
        return companyService.getTask(taskId);
    }

    /**
     * 查看用户提交的项目列表
     * @param companyGetUserCommitRequest
     * @return
     */
    @GetMapping("/user/commit")
    public BaseResponse<List<CompanyTaskCommitVO>> listUserCommit(CompanyGetUserCommitRequest companyGetUserCommitRequest){
        if(companyGetUserCommitRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return companyService.listUserCommit(companyGetUserCommitRequest);
    }



}
