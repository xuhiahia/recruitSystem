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
import com.fzy.project.mapper.CompanyMapper;
import com.fzy.project.mapper.UserChiefMapper;
import com.fzy.project.model.dto.chief.ChiefAddRequest;
import com.fzy.project.model.dto.chief.ChiefDeleteRequest;
import com.fzy.project.model.dto.chief.ChiefQueryRequest;
import com.fzy.project.model.dto.chief.ChiefUpdateRequest;
import com.fzy.project.model.dto.company.*;
import com.fzy.project.model.dto.note.NotePassRequest;
import com.fzy.project.model.dto.note.NoteQueryRequest;
import com.fzy.project.model.entity.*;
import com.fzy.project.model.vo.*;
import com.fzy.project.service.*;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static com.fzy.project.constant.CompanyConstant.PASS;
import static com.fzy.project.constant.CompanyConstant.REAL_HC;

/**
* @author 徐小帅
* @description 针对表【company】的数据库操作Service实现
* @createDate 2023-04-16 23:28:17
*/
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company>
    implements CompanyService {
    private static final String SALT = "fzy";
    @Resource
    private ChiefService chiefService;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private UserTaskService userTaskService;
    @Resource
    private UserChiefMapper userChiefMapper;
    @Resource
    private  UserChiefService userChiefService;

    @Resource
    private CompanyTaskService companyTaskService;

    @Resource
    private UserService userService;
    @Resource
    private NoteService noteService;
    @Override
    public BaseResponse<Boolean> addChief(ChiefAddRequest chiefAddRequest) {
        Chief chief = new Chief();
        chief.setChiefRealHc(REAL_HC);
        BeanUtil.copyProperties(chiefAddRequest,chief,"chiefHc");
        String chiefHc = chiefAddRequest.getChiefHc();
        if(StrUtil.isNotBlank(chiefHc)){
            chief.setChiefHc(Integer.parseInt(chiefAddRequest.getChiefHc()));
        }
        boolean isSuccess = chiefService.save(chief);
        return ResultUtils.success(isSuccess);
    }

    @Override
    public BaseResponse<List<ChiefVO>> listChief(ChiefQueryRequest chiefQueryRequest) {
        LambdaQueryWrapper<Chief> chiefLQ = setQuery(chiefQueryRequest);
        if(chiefLQ==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
//        chiefLQ.eq(Chief::getChiefStatus,PASS);
        List<Chief> chiefs = chiefService.list(chiefLQ);
//        List<ChiefVO> chiefVOS = chiefs.stream().map(chief -> {
//            ChiefVO chiefVO = new ChiefVO();
//            BeanUtil.copyProperties(chief, chiefVO, "chiefHc","chiefRealHc");
//            Long companyId = chief.getCompanyId();
//            LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Company::getId, companyId);
//            Company company = companyMapper.selectOne(queryWrapper);
//            if(company==null){
//                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"公司不存在");
//            }
//            chiefVO.setCompanyAddress(company.getAddress());
//            chiefVO.setCompanyName(company.getCompanyName());
//            chiefVO.setCompanyIndustry(company.getCompanyIndustry());
//            return chiefVO;
//        }).collect(Collectors.toList());
        List<ChiefVO> chiefVOS = chiefs.stream().map(it -> {
            ChiefVO chiefVO = new ChiefVO();
            BeanUtil.copyProperties(it, chiefVO);
            Integer status = it.getChiefStatus();
            chiefVO.setChiefStatus(status == 1 ? "通过" : "审核");
            return chiefVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(chiefVOS);
    }

    public LambdaQueryWrapper<Chief> setQuery(ChiefQueryRequest chiefQueryRequest){
        LambdaQueryWrapper<Chief> chiefLQ = new LambdaQueryWrapper<>();
        Long id = chiefQueryRequest.getCompanyId();
        if(id==null||id<=0){
            return null;
        }
        chiefLQ.eq(Chief::getCompanyId,id);
        String chiefAddress = chiefQueryRequest.getChiefAddress();
        String chiefName = chiefQueryRequest.getChiefName();
        String chiefStatus = chiefQueryRequest.getChiefStatus();
        if(StrUtil.isNotBlank(chiefAddress)){
            chiefLQ.like(Chief::getChiefAddress,chiefAddress);
        }
        if(StrUtil.isNotBlank(chiefName)){
            chiefLQ.like(Chief::getChiefName,chiefName);
        }
        if(StrUtil.isNotBlank(chiefStatus)){
            chiefLQ.eq(Chief::getChiefStatus,"审核".equals(chiefStatus)?0:1);
        }
        return chiefLQ;
    }
    @Override
    public BaseResponse<Boolean> deleChief(ChiefDeleteRequest chiefDeleteRequest) {
        List<Long> ids = chiefDeleteRequest.getIds();
        if(ids==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = chiefService.removeBatchByIds(ids);
        return ResultUtils.success(isSuccess);
    }

    @Override
    public BaseResponse<Boolean> updateChief(ChiefUpdateRequest chiefUpdateRequest) {
        Long id = chiefUpdateRequest.getId();
        if(id==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Chief chief = new Chief();
        BeanUtil.copyProperties(chiefUpdateRequest,chief,"chiefHc","chiefRealHc;");
        String chiefHc = chiefUpdateRequest.getChiefHc();
        if(StrUtil.isNotBlank(chiefHc)){
            chief.setChiefHc(Integer.parseInt(chiefHc));
        }
        String realHc = chiefUpdateRequest.getChiefRealHc();
        if(StrUtil.isNotBlank(chiefHc)){
            chief.setChiefHc(Integer.parseInt(realHc));
        }
        boolean isSuccess = chiefService.updateById(chief);
        return ResultUtils.success(isSuccess);
    }

    @Override
    public BaseResponse<ChiefVO> getChiefById(Long id) {
        Chief chief = chiefService.getById(id);
        ChiefVO chiefVO = new ChiefVO();
        BeanUtil.copyProperties(chief,chiefVO,"chiefHc","chiefRealHc");
        chiefVO.setChiefHc(chief.getChiefHc()+"");
        chiefVO.setChiefRealHc(chief.getChiefRealHc()+"");
        return ResultUtils.success(chiefVO);
    }

    @Override
    public BaseResponse<List<NoteVO>> listNote(NoteQueryRequest noteQueryRequest) {
        Long companyId = noteQueryRequest.getCompanyId();
        if(companyId==null||companyId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<UserChief> UCquery = new LambdaQueryWrapper<>();
        UCquery.eq(UserChief::getCompanyId,companyId);
        List<UserChief> userChiefs = userChiefService.list(UCquery);
        String name = noteQueryRequest.getChiefName();
        if(StrUtil.isNotBlank(name)){
            userChiefs = userChiefMapper.queryUC(name, companyId);
        }
        List<NoteVO> noteVOS = userChiefs.stream().map(item -> {
            Long chiefId = item.getChiefId();
            if (chiefId == null || chiefId <= 0) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "岗位错误");
            }
//            //设置岗位信息
            Chief chief = chiefService.getById(chiefId);
            String chiefName = chief.getChiefName();
            NoteVO noteVO = new NoteVO();
            noteVO.setChiefName(chiefName);
            Integer status = item.getUserChiefStatus();
            noteVO.setUserChiefStatus(status == 0 ? "待定" : "通过");
//            //设置
            Long userId = item.getUserId();
            if (userId == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
            }
            User user = userService.getById(userId);
            noteVO.setGender(user.getGender() == 0 ? "女" : "男");
            noteVO.setAge(user.getAge()+"");
            noteVO.setUserName(user.getUserName());
            Long noteId = user.getNoteId();
            if(noteId==null){ //
                noteVO.setNoteDescription("该用户还未完善简历");
                return noteVO;
            }
            Note note = noteService.getById(noteId);
            BeanUtil.copyProperties(note, noteVO,"noteId");
            noteVO.setNoteId(note.getId());
            //
            noteVO.setUserId(userId);
            //
            return noteVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(noteVOS);
    }

    @Override
    public BaseResponse<NoteDetailVO> noteDetail(Long noteId) {
        Note note = noteService.getById(noteId);
        NoteDetailVO noteDetailVO = new NoteDetailVO();
        Long userId = note.getUserId();
        if(userId==null||userId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(userId);
        noteDetailVO.setAge(user.getAge()+"");
        if(user==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"用户不存在");
        }
        noteDetailVO.setUserName(user.getUserName());
        noteDetailVO.setGender(user.getGender()==0?"女":"男");
        BeanUtil.copyProperties(note,noteDetailVO,"noteId","jobStatus");
        noteDetailVO.setNoteId(note.getId());
        Integer jobStatus = note.getJobStatus();
        if(jobStatus==null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        String jobStatusStr="";
        if(jobStatus==0){
            jobStatusStr="离校随时到岗";
        }else if(jobStatus==1){
            jobStatusStr="在校月内到岗";
        }else if (jobStatus==2){
            jobStatusStr="在校考虑机会";
        }else {
            jobStatusStr="在校暂不考虑";
        }
        noteDetailVO.setJobStatus(jobStatusStr);
        return ResultUtils.success(noteDetailVO);
    }

    @Override
    public BaseResponse<Boolean> passNote(NotePassRequest notePassRequest) {
        Long ids = notePassRequest.getNoteId();
        if(ids==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
            UserChief userChief = userChiefService.getById(ids);
            userChief.setUserChiefStatus(1);
        boolean isSuccess = userChiefService.updateById(userChief);
        return ResultUtils.success(isSuccess);
    }

    @Override
    public BaseResponse<CompanyVO> registerCompany(CompanyRegisterRequest companyRegisterRequest) {

        if(!companyRegisterRequest.getCompanyPwd().equals(companyRegisterRequest.getCompanyCheckPwd())){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"密码不一致");
        }
        if (companyRegisterRequest.getCompanyAccount().length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if (companyRegisterRequest.getCompanyPwd().length() < 8 || companyRegisterRequest.getCompanyCheckPwd().length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }
        Company company = new Company();
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + companyRegisterRequest.getCompanyPwd()).getBytes());
        BeanUtil.copyProperties(companyRegisterRequest,company,"scale");
//        if(StrUtil.isNotBlank(companyRegisterRequest.getCompanyScale())){
//           company.setScale( "小".equals(companyRegisterRequest.getCompanyScale())?0:"中".equals(companyRegisterRequest.getCompanyScale())?1:2);
//        }
        LambdaQueryWrapper<Company> companyLQ = new LambdaQueryWrapper<>();
        companyLQ.eq(Company::getCompanyAccount,companyRegisterRequest.getCompanyAccount());
        Company one = this.getOne(companyLQ);
        if(one!=null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"已经存在账号");
        }
        company.setCompanyPwd(encryptPassword);
        boolean isSuccess = this.save(company);
        if(isSuccess){
            CompanyVO companyVO = new CompanyVO();
            BeanUtil.copyProperties(company,companyVO);
            return ResultUtils.success(companyVO);
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR,"注册失败");
    }

    @Override
    public BaseResponse<Boolean> updateDataCompany(CompanyUpdateDataRequest companyUpdateDataRequest) {
        Long companyId = companyUpdateDataRequest.getId();
        if(companyId==null||companyId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Company company = this.getById(companyId);
        if(company==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"公司已下架");
        }
        BeanUtil.copyProperties(companyUpdateDataRequest,company,"companyScale");
        String scaleStr = companyUpdateDataRequest.getCompanyScale();
        String address = companyUpdateDataRequest.getCompanyAddress();
        if(StrUtil.isNotBlank(address)){
            company.setAddress(address);
        }
        if(scaleStr!=null || StrUtil.isNotBlank(scaleStr)){
            Integer scale = scaleStr.equals("小") ? 0 : scaleStr.equals("中") ? 1 : 2;
            company.setScale(scale);
        }

        String oldPwd = companyUpdateDataRequest.getOldPwd();
        String newPwd = companyUpdateDataRequest.getNewPwd();
        if(StrUtil.isNotBlank(oldPwd)&&StrUtil.isNotBlank(newPwd)) {
            //修改密码
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + oldPwd).getBytes());
            String companyPwd = company.getCompanyPwd();
            if (!encryptPassword.equals(companyPwd)) {//密码不正确
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "密码不正确");
            }
            if(newPwd.length()<=8){
                return ResultUtils.error(ErrorCode.OPERATION_ERROR,"密码过短");
            }
            String newPassword = DigestUtils.md5DigestAsHex((SALT + newPwd).getBytes());
            company.setCompanyPwd(newPassword);
        }
        boolean isSuccess = this.updateById(company);
        return ResultUtils.success(isSuccess);
    }

    @Override
    public BaseResponse<CompanyDataVO> getCompanyById(Long id) {
        Company company = this.getById(id);
        if(company==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        CompanyDataVO companyVO = new CompanyDataVO();
        BeanUtils.copyProperties(company,companyVO,"scale");
        if(company.getScale()!=null){
            companyVO.setScale(company.getScale()==0?"小":company.getScale()==1?"中":"大");
        }
        return ResultUtils.success(companyVO);
    }


    /**
     * 公司发布任务
     * @param companyAddTaskRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> addTask(CompanyAddTaskRequest companyAddTaskRequest) {
        Long companyId = companyAddTaskRequest.getCompanyId();
        if(companyId==null||companyId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Company company = this.getById(companyId);
        if(company==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"公司已经下架");
        }
        CompanyTask companyTask = new CompanyTask();
        BeanUtil.copyProperties(companyAddTaskRequest,companyTask);
        boolean isSuccess = companyTaskService.save(companyTask);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 展示公司任务
     * @param companyTaskQueryRequest
     * @return
     */
    @Override
    public BaseResponse<List<CompanyTaskVO>> listTask(CompanyTaskQueryRequest companyTaskQueryRequest) {
        LambdaQueryWrapper<CompanyTask> queryWrapper = setQuery(companyTaskQueryRequest);
        List<CompanyTask> companyTasks = companyTaskService.list(queryWrapper);
        List<CompanyTaskVO> taskVOS = companyTasks.stream().map(task -> {
            CompanyTaskVO companyTaskVO = new CompanyTaskVO();
            Company company = this.getById(task.getCompanyId());
            if(company==null){
                companyTaskVO.setCompanyName("公司已经注销了");
                return companyTaskVO;
            }
            companyTaskVO.setCompanyAvatar(company.getCompanyAvatar());
            companyTaskVO.setCompanyName(company.getCompanyName());
            companyTaskVO.setCompanyId(task.getCompanyId());
            BeanUtil.copyProperties(task, companyTaskVO, "taskStatus");
            String status = task.getTaskStatus() == 0 ? "发布" : "停用";
            companyTaskVO.setTaskStatus(status);
            return companyTaskVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(taskVOS);
    }

    /**
     * 修改企业项目内容
     * @param companyTaskUpdateRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> updateTask(CompanyTaskUpdateRequest companyTaskUpdateRequest) {
        Long taskId = companyTaskUpdateRequest.getId();
        if(taskId==null||taskId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        CompanyTask task = companyTaskService.getById(taskId);
        if(task==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"任务不存在");
        }
        BeanUtil.copyProperties(companyTaskUpdateRequest,task,"taskStatus");
        String taskStatus = companyTaskUpdateRequest.getTaskStatus();
        if(StrUtil.isNotBlank(taskStatus)||taskStatus!=null){
            Integer status = "停用".equals(taskStatus) ? 1 : 0;
            task.setTaskStatus(status);
        }
        boolean isSuccess = companyTaskService.updateById(task);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 展示企业任务信息
     * @param id
     * @return
     */
    @Override
    public BaseResponse<CompanyTaskVO> getTask(Long id) {
        CompanyTask task = companyTaskService.getById(id);
        if (task==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        CompanyTaskVO companyTaskVO = new CompanyTaskVO();
        BeanUtil.copyProperties(task,companyTaskVO,"taskStatus");
        String taskStatus = task.getTaskStatus() == 0 ? "发布" : "停用";
        companyTaskVO.setTaskStatus(taskStatus);
        return ResultUtils.success(companyTaskVO);
    }

    /**
     * 企业查看用户提交的项目
     * @return
     */
    @Override
    public BaseResponse<List<CompanyTaskCommitVO>> getUserCommit() {
        return null;
    }

    /**
     * 企业删除发布的项目
     * @param companyTaskDeleteRequest
     * @return
     */
    @Override
    public BaseResponse<Boolean> deleTask(CompanyTaskDeleteRequest companyTaskDeleteRequest) {
        List<Long> ids = companyTaskDeleteRequest.getIds();
        Long companyId = companyTaskDeleteRequest.getCompanyId();
        Company company = this.getById(companyId);
        if(company==null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"公司已下架");
        }
        boolean isSuccess = companyTaskService.removeBatchByIds(ids);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 查看学生给企业发的任务
     * @param
     * @return
     */
    @Override
    public BaseResponse<List<CompanyTaskCommitVO>> listUserCommit(CompanyGetUserCommitRequest companyGetUserCommitRequest) {
        LambdaQueryWrapper<UserTask> queryWrapper = setQuery(companyGetUserCommitRequest);
        List<UserTask> commits = userTaskService.list(queryWrapper);
        List<CompanyTaskCommitVO> commitVOS = commits.stream().map(commit -> {
            CompanyTaskCommitVO companyTaskCommitVO = new CompanyTaskCommitVO();
            BeanUtil.copyProperties(commit, companyTaskCommitVO);
            Long userId = commit.getUserId();
            User user = userService.getById(userId);
            if (user.getUserName() != null) {
                companyTaskCommitVO.setUserName(user.getUserName());
            }
            Integer gender = user.getGender();
            if(gender!=null){
                companyTaskCommitVO.setGender(gender==1?"男":"女");
            }
            if(user.getUserEmail()!=null){
                companyTaskCommitVO.setUserEmail(user.getUserEmail());
            }
            if(user.getAge()!=null){
                companyTaskCommitVO.setUserAge(user.getAge());
            }
            return companyTaskCommitVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(commitVOS);
    }

    private LambdaQueryWrapper<UserTask> setQuery(CompanyGetUserCommitRequest companyGetUserCommitRequest){
        String companyId = companyGetUserCommitRequest.getCompanyId();
        if(StrUtil.isEmpty(companyId)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"公司已注销");
        }
        LambdaQueryWrapper<UserTask> queryWrapper = new LambdaQueryWrapper<>();
        String taskType = companyGetUserCommitRequest.getTaskType();
        String taskTitle = companyGetUserCommitRequest.getTaskTitle();
        queryWrapper.like(!taskTitle.equals(""),UserTask::getTaskTitle,taskTitle);
        queryWrapper.eq(!taskType.equals(""),UserTask::getTaskType,taskType);
        queryWrapper.eq(UserTask::getCompanyId,Long.parseLong(companyId));
        return queryWrapper;
    }

    private LambdaQueryWrapper<CompanyTask> setQuery(CompanyTaskQueryRequest companyTaskQueryRequest){
        String taskTitle = companyTaskQueryRequest.getTaskTitle();
        String taskType = companyTaskQueryRequest.getTaskType();
        String companyId1 = companyTaskQueryRequest.getCompanyId();
        LambdaQueryWrapper<CompanyTask> queryWrapper = new LambdaQueryWrapper<>();
        if(companyId1 !=null){
            Long companyId = Long.parseLong(companyId1);
            queryWrapper.eq(companyId!=null,CompanyTask::getCompanyId,companyId);
        }
        if(!"全部".equals(taskType)) {
            queryWrapper.eq(!taskType.equals(""), CompanyTask::getTaskType, taskType);
        }
        queryWrapper.like(StrUtil.isNotBlank(taskTitle),CompanyTask::getTaskTitle,taskTitle);
        return queryWrapper;

    }

}




