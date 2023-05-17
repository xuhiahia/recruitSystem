package com.fzy.project.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.common.ErrorCode;
import com.fzy.project.common.ResultUtils;
import com.fzy.project.model.dto.common.AddRelationRequest;
import com.fzy.project.model.dto.common.GetInfoRequest;
import com.fzy.project.model.entity.ChatList;
import com.fzy.project.model.entity.UserChat;
import com.fzy.project.service.ChatListService;
import com.fzy.project.service.CommonService;
import com.fzy.project.service.UserChatService;
import org.aspectj.weaver.NewFieldTypeMunger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.fzy.project.constant.SystemConstant.BASE_PATH;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private CommonService commonService;

    @Resource
    private UserChatService userChatService;

    @Resource
    private ChatListService chatListService;
    private String basePath=BASE_PATH;
    /**
     * 上传路径
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file){
        try{
            String originalFilename = file.getOriginalFilename();
            String newFileName = createNewFileName(originalFilename);
            file.transferTo(new File(newFileName));
            return ResultUtils.success(newFileName);
        }catch (IOException e){
            throw new RuntimeException("文件上传失败");
        }
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleFile(@RequestParam("name") String filename){
        File file = new File(filename);
        if(file.isDirectory()){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"文件名称错误");
        }
        boolean isSuccess = FileUtil.del(file);
        return ResultUtils.success(isSuccess);
    }
    /**
     * 创建文件目录
     * @param originalFilename
     * @return
     */
    private String createNewFileName(String originalFilename) {
        String path="";
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        if(suffix.equals("png")||suffix.equals("jpg")||suffix.equals("bmp")){
            path =basePath + "\\images\\";
        }else if(suffix.equals("pdf")||suffix.equals("doc")||suffix.equals("docx")){
            path = basePath + "\\files\\";
        }else{
            path = basePath + "\\others\\";
        }
        // 判断目录是否存在
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return path+name+"."+suffix;
    }

    @PostMapping("/getInfo")
    public BaseResponse<? extends Object> getInfoById(@RequestBody GetInfoRequest getInfoRequest){
        if(getInfoRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return commonService.getInfo(getInfoRequest);
    }


    @PostMapping("/add/relation")
    public BaseResponse<Boolean> addRelation(@RequestBody AddRelationRequest addRelationRequest){
        if(addRelationRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long receiveId = addRelationRequest.getReceiveId();
        Long sendId = addRelationRequest.getSendId();
        UserChat one = userChatService.getOne(new QueryWrapper<UserChat>().eq("send_user", sendId).eq("receive_user", receiveId));
        if(one!=null){
            return ResultUtils.success(true);
        }
        UserChat userChat = new UserChat();
        UserChat userChat1 = new UserChat();
        userChat.setReceiveUser(receiveId);
        userChat.setSendUser(sendId);
        userChat1.setSendUser(receiveId);
        userChat1.setReceiveUser(sendId);
        //保存用户关系
        userChatService.save(userChat);
        userChatService.save(userChat1);

        ChatList chatList = new ChatList();
        ChatList chatList1 = new ChatList();
        chatList.setReceiveUser(receiveId);
        chatList.setSendUser(sendId);
        chatList.setUserChatId(userChat.getId());
        chatListService.save(chatList);
        chatList1.setSendUser(receiveId);
        chatList1.setReceiveUser(sendId);
        chatList1.setUserChatId(userChat1.getId());
        chatListService.save(chatList1);
        return ResultUtils.success(true);
    }

}
