package com.fzy.project.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class NoteDetailVO extends NoteVO{

    private String noteEducation;

    private String noteItem;

    private String noteExpect;

    private String noteWork;

    /**
     * 0-离校随时到岗  1-在校月内到岗  2-在校考虑机会 3-在校暂不考虑
     */
    private String jobStatus;

    private String graduateYear;

    /**
     * 简历地址
     */
    private String noteUrl;

}
