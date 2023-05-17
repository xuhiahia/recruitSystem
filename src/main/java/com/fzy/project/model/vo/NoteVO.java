package com.fzy.project.model.vo;

import lombok.Data;

@Data
public class NoteVO {

    private Long noteId;

    private String chiefName;
    /**
     * 应聘状态0-待定 1-通过
     */
    private String userChiefStatus;

    private String age;

    private String gender;

    private String noteDescription;

    private String userName;

    private String graduateSchool;

    private String academicDegree;

    private Long userId;
}
