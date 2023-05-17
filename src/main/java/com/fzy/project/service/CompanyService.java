package com.fzy.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.dto.chief.ChiefAddRequest;
import com.fzy.project.model.dto.chief.ChiefDeleteRequest;
import com.fzy.project.model.dto.chief.ChiefQueryRequest;
import com.fzy.project.model.dto.chief.ChiefUpdateRequest;
import com.fzy.project.model.dto.company.*;
import com.fzy.project.model.dto.note.NotePassRequest;
import com.fzy.project.model.dto.note.NoteQueryRequest;
import com.fzy.project.model.entity.Company;
import com.fzy.project.model.vo.*;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【company】的数据库操作Service
* @createDate 2023-04-16 23:28:17
*/
public interface CompanyService extends IService<Company> {
    /**
     * 添加招聘信息
     * @param chiefAddRequest
     * @return
     */
    public BaseResponse<Boolean> addChief(ChiefAddRequest chiefAddRequest);

    /**
     * 展示招聘信息
     * @return
     */
    public BaseResponse<List<ChiefVO>> listChief(ChiefQueryRequest chiefQueryRequest);

    /**
     * 删除岗位信息
     * @param chiefDeleteRequest
     * @return
     */
    public BaseResponse<Boolean> deleChief(ChiefDeleteRequest chiefDeleteRequest);

    /**
     * 修改岗位信息
     * @param chiefUpdateRequest
     * @return
     */
    public BaseResponse<Boolean> updateChief(ChiefUpdateRequest chiefUpdateRequest);

    /**
     * 根据ID查询岗位
     * @param id
     * @return
     */
    public BaseResponse<ChiefVO> getChiefById(Long id);

    /**
     * 查询投递人
     * @param noteQueryRequest
     * @return
     */
    public BaseResponse<List<NoteVO>> listNote(NoteQueryRequest noteQueryRequest);

    /**
     * 简历细节
     * @param noteId
     * @return
     */
    public BaseResponse<NoteDetailVO> noteDetail(Long noteId);

    /**
     * 通过简历
     * @param notePassRequest
     * @return
     */
    public BaseResponse<Boolean> passNote(NotePassRequest notePassRequest);

    /**
     * 注册公司
     * @param companyRegisterRequest
     * @return
     */
    public BaseResponse<CompanyVO> registerCompany(CompanyRegisterRequest companyRegisterRequest);

    /**
     * 企业修改个人信息
     * @param companyUpdateDataRequest
     * @return
     */
    public BaseResponse<Boolean> updateDataCompany(CompanyUpdateDataRequest companyUpdateDataRequest);

    /**
     * 查看公司信息
     * @param id
     * @return
     */
    public BaseResponse<CompanyDataVO> getCompanyById(Long id);

    /**
     * 公司发布任务
     * @param companyAddTaskRequest
     * @return
     */
    public BaseResponse<Boolean> addTask(CompanyAddTaskRequest companyAddTaskRequest);

    /**
     *  展示公司项目
     * @param companyTaskQueryRequest
     * @return
     */
    public BaseResponse<List<CompanyTaskVO>> listTask(CompanyTaskQueryRequest companyTaskQueryRequest);

    /**
     * 修改企业项目内容
     * @param companyTaskUpdateRequest
     * @return
     */
    public BaseResponse<Boolean> updateTask(CompanyTaskUpdateRequest companyTaskUpdateRequest);

    /**
     * 展示任务信息
     * @param id
     * @return
     */
    public BaseResponse<CompanyTaskVO> getTask(Long id);

    /**
     * 查看用户提交后的状态
     * @return
     */
    public BaseResponse<List<CompanyTaskCommitVO>> getUserCommit();

    /**
     * 企业删除项目任务
     * @param companyTaskDeleteRequest
     * @return
     */
    public BaseResponse<Boolean> deleTask(CompanyTaskDeleteRequest companyTaskDeleteRequest);

    /**
     * 查看学生给企业提交的任务
     * @param companyGetUserCommitRequest
     * @return
     */
    public BaseResponse<List<CompanyTaskCommitVO>> listUserCommit(CompanyGetUserCommitRequest companyGetUserCommitRequest);

}
