package com.fzy.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzy.project.common.BaseResponse;
import com.fzy.project.model.entity.Note;
import com.fzy.project.model.vo.NoteVO;
import com.fzy.project.service.NoteService;
import com.fzy.project.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【note(简历内容)】的数据库操作Service实现
* @createDate 2023-04-18 21:57:09
*/
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note>
    implements NoteService{


}




